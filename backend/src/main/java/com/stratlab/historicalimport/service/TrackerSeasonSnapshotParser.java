package com.stratlab.historicalimport.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.stratlab.common.exception.HistoricalImportValidationException;
import com.stratlab.historicalimport.entity.TrackerSeasonSnapshot;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class TrackerSeasonSnapshotParser {

    public TrackerSeasonSnapshot parse(JsonNode payload) {
        JsonNode season = payload.path("data").path(0);
        if (!"season".equals(season.path("type").asText())) {
            throw new HistoricalImportValidationException("Expected a Tracker season statsv2 payload.");
        }

        JsonNode attributes = season.path("attributes");
        JsonNode metadata = season.path("metadata");
        JsonNode stats = season.path("stats");

        TrackerSeasonSnapshot snapshot = new TrackerSeasonSnapshot(
                requiredText(attributes, "seasonId"),
                requiredText(metadata, "shortName"),
                requiredText(attributes, "playlist"),
                nonNegativeWholeNumber(stats, "matchesPlayed", true),
                nonNegativeWholeNumber(stats, "matchesWon", false),
                nonNegativeWholeNumber(stats, "matchesLost", false),
                nonNegativeWholeNumber(stats, "matchesTied", false),
                statValue(stats, "matchesWinPct", false)
        );
        validateConsistency(snapshot);
        return snapshot;
    }

    private String requiredText(JsonNode node, String field) {
        String value = node.path(field).asText();
        if (value.isBlank()) {
            throw new HistoricalImportValidationException("Tracker payload is missing " + field + ".");
        }
        return value;
    }

    private int nonNegativeWholeNumber(JsonNode stats, String field, boolean required) {
        BigDecimal value = statValue(stats, field, required);
        try {
            int result = value.setScale(0, RoundingMode.UNNECESSARY).intValueExact();
            if (result < 0) {
                throw new HistoricalImportValidationException("Tracker stat " + field + " cannot be negative.");
            }
            return result;
        } catch (ArithmeticException exception) {
            throw new HistoricalImportValidationException("Tracker stat " + field + " must be a non-negative whole number.");
        }
    }

    private BigDecimal statValue(JsonNode stats, String field, boolean required) {
        JsonNode statistic = stats.path(field);
        JsonNode value = statistic.isNumber() ? statistic : statistic.path("value");
        if (!value.isNumber()) {
            if (!required && statistic.isMissingNode()) {
                return BigDecimal.ZERO;
            }
            throw new HistoricalImportValidationException("Tracker payload is missing numeric stat " + field + ".");
        }
        return value.decimalValue();
    }

    private void validateConsistency(TrackerSeasonSnapshot snapshot) {
        long outcomes = (long) snapshot.matchesWon() + snapshot.matchesLost() + snapshot.matchesTied();
        if (outcomes > snapshot.matchesPlayed()) {
            throw new HistoricalImportValidationException("Tracker match outcome totals exceed matches played.");
        }
        if (snapshot.winPercentage().compareTo(BigDecimal.ZERO) < 0 || snapshot.winPercentage().compareTo(new BigDecimal("100")) > 0) {
            throw new HistoricalImportValidationException("Tracker win percentage must be between 0 and 100.");
        }
    }
}
