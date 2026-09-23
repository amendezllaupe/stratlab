package com.stratlab.historicalimport.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stratlab.common.exception.HistoricalImportValidationException;
import com.stratlab.historicalimport.config.HistoricalImportProperties;
import com.stratlab.historicalimport.dto.HistoricalSeasonImportRequest;
import com.stratlab.historicalimport.dto.HistoricalSeasonImportResponse;
import com.stratlab.historicalimport.entity.HistoricalImport;
import com.stratlab.historicalimport.entity.PlayerSeasonStatistics;
import com.stratlab.historicalimport.entity.TrackerSeasonSnapshot;
import com.stratlab.historicalimport.repository.HistoricalImportRepository;
import com.stratlab.historicalimport.repository.PlayerSeasonStatisticsRepository;
import com.stratlab.player.entity.Player;
import com.stratlab.player.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

@Service
public class HistoricalSeasonImportService {

    private static final String TRACKER_PROVIDER = "tracker-manual-export";
    private static final String TRACKER_SEASON_SOURCE_TYPE = "tracker-statsv2-season";

    private final PlayerRepository playerRepository;
    private final HistoricalImportRepository historicalImportRepository;
    private final PlayerSeasonStatisticsRepository playerSeasonStatisticsRepository;
    private final TrackerSeasonSnapshotParser trackerSeasonSnapshotParser;
    private final HistoricalImportProperties historicalImportProperties;
    private final ObjectMapper objectMapper;

    public HistoricalSeasonImportService(
            PlayerRepository playerRepository,
            HistoricalImportRepository historicalImportRepository,
            PlayerSeasonStatisticsRepository playerSeasonStatisticsRepository,
            TrackerSeasonSnapshotParser trackerSeasonSnapshotParser,
            HistoricalImportProperties historicalImportProperties,
            ObjectMapper objectMapper
    ) {
        this.playerRepository = playerRepository;
        this.historicalImportRepository = historicalImportRepository;
        this.playerSeasonStatisticsRepository = playerSeasonStatisticsRepository;
        this.trackerSeasonSnapshotParser = trackerSeasonSnapshotParser;
        this.historicalImportProperties = historicalImportProperties;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public HistoricalSeasonImportResponse importTrackerSeason(HistoricalSeasonImportRequest request) {
        String normalizedRiotTag = normalizeRiotTag(request.riotTag());
        String displayName = request.playerDisplayName().trim();
        String sourceReference = request.sourceReference().trim();
        TrackerSeasonSnapshot snapshot = trackerSeasonSnapshotParser.parse(request.payload());
        String payload = serialize(request);
        validateSafeImport(sourceReference, request.payload(), payload);
        String checksum = checksum(normalizedRiotTag + "\n" + canonicalize(request.payload()));

        historicalImportRepository.acquireTransactionLock("import:" + checksum);
        HistoricalImport existingImport = historicalImportRepository.findByChecksum(checksum).orElse(null);
        if (existingImport != null) {
            Player existingPlayer = playerRepository.findByRiotTagNormalized(normalizedRiotTag)
                    .orElseThrow(() -> new HistoricalImportValidationException("Existing import has no registered player identity."));
            return new HistoricalSeasonImportResponse(
                    existingImport.getId(), existingPlayer.getRiotTag(), snapshot.seasonName(), snapshot.matchesPlayed(), false
            );
        }

        historicalImportRepository.acquireTransactionLock("player:" + normalizedRiotTag);
        Player player = playerRepository.findByRiotTagNormalized(normalizedRiotTag)
                .orElseGet(() -> playerRepository.save(new Player(displayName, request.riotTag().trim(), normalizedRiotTag)));

        HistoricalImport historicalImport = historicalImportRepository.save(new HistoricalImport(
                TRACKER_PROVIDER,
                TRACKER_SEASON_SOURCE_TYPE,
                sourceReference,
                checksum,
                payload
        ));
        playerSeasonStatisticsRepository.save(new PlayerSeasonStatistics(player, historicalImport, snapshot));

        return new HistoricalSeasonImportResponse(
                historicalImport.getId(), player.getRiotTag(), snapshot.seasonName(), snapshot.matchesPlayed(), true
        );
    }

    private String serialize(HistoricalSeasonImportRequest request) {
        try {
            return objectMapper.writeValueAsString(request.payload());
        } catch (JsonProcessingException exception) {
            throw new HistoricalImportValidationException("Tracker payload cannot be serialized.");
        }
    }

    private void validateSafeImport(String sourceReference, com.fasterxml.jackson.databind.JsonNode jsonPayload, String payload) {
        if (payload.getBytes(StandardCharsets.UTF_8).length > historicalImportProperties.maxPayloadBytes()) {
            throw new com.stratlab.common.exception.HistoricalImportPayloadTooLargeException();
        }
        if (sourceReference.contains("?") || sourceReference.contains("#") || hasUriUserInfo(sourceReference)) {
            throw new HistoricalImportValidationException("Source reference must not include URL query or fragment data.");
        }
        if (jsonPayload.size() != 1 || !jsonPayload.has("data")) {
            throw new HistoricalImportValidationException("Tracker import payload must contain only the response body data field.");
        }
        rejectSensitiveFields(jsonPayload);
    }

    private boolean hasUriUserInfo(String sourceReference) {
        try {
            return new URI(sourceReference).getUserInfo() != null;
        } catch (URISyntaxException exception) {
            return false;
        }
    }

    private void rejectSensitiveFields(com.fasterxml.jackson.databind.JsonNode node) {
        if (node.isObject()) {
            Iterator<Map.Entry<String, com.fasterxml.jackson.databind.JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, com.fasterxml.jackson.databind.JsonNode> field = fields.next();
                String key = field.getKey().replace("-", "").replace("_", "").toLowerCase();
                if (key.contains("authorization") || key.contains("cookie") || key.contains("token")
                        || key.contains("apikey") || key.contains("secret") || key.contains("password")
                        || key.contains("credential") || key.contains("session") || key.contains("accesskey")
                        || key.contains("jwt") || key.equals("headers")) {
                    throw new HistoricalImportValidationException("Tracker payload contains a forbidden sensitive field.");
                }
                rejectSensitiveFields(field.getValue());
            }
        } else if (node.isArray()) {
            for (com.fasterxml.jackson.databind.JsonNode item : node) {
                rejectSensitiveFields(item);
            }
        }
    }

    private String canonicalize(com.fasterxml.jackson.databind.JsonNode node) {
        try {
            return objectMapper.writeValueAsString(canonicalNode(node));
        } catch (JsonProcessingException exception) {
            throw new HistoricalImportValidationException("Tracker payload cannot be canonicalized.");
        }
    }

    private com.fasterxml.jackson.databind.JsonNode canonicalNode(com.fasterxml.jackson.databind.JsonNode node) {
        if (node.isObject()) {
            ObjectNode result = JsonNodeFactory.instance.objectNode();
            TreeMap<String, com.fasterxml.jackson.databind.JsonNode> sorted = new TreeMap<>();
            node.fields().forEachRemaining(entry -> sorted.put(entry.getKey(), entry.getValue()));
            sorted.forEach((key, value) -> result.set(key, canonicalNode(value)));
            return result;
        }
        if (node.isArray()) {
            ArrayNode result = JsonNodeFactory.instance.arrayNode();
            node.forEach(item -> result.add(canonicalNode(item)));
            return result;
        }
        return node;
    }

    private String checksum(String payload) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(payload.getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 must be available in the Java runtime.", exception);
        }
    }

    private String normalizeRiotTag(String riotTag) {
        return riotTag.trim().toLowerCase(Locale.ROOT);
    }
}
