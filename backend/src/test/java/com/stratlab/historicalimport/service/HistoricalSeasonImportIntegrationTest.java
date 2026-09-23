package com.stratlab.historicalimport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stratlab.historicalimport.dto.HistoricalSeasonImportRequest;
import com.stratlab.historicalimport.dto.HistoricalSeasonImportResponse;
import com.stratlab.historicalimport.repository.HistoricalImportRepository;
import com.stratlab.historicalimport.repository.PlayerSeasonStatisticsRepository;
import com.stratlab.player.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HistoricalSeasonImportIntegrationTest {

    @Autowired
    private HistoricalSeasonImportService historicalSeasonImportService;

    @Autowired
    private HistoricalImportRepository historicalImportRepository;

    @Autowired
    private PlayerSeasonStatisticsRepository playerSeasonStatisticsRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanImportedData() {
        playerSeasonStatisticsRepository.deleteAll();
        historicalImportRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Test
    void importsTrackerSeasonOnceAndReturnsExistingImportForTheSamePlayerPayload() throws Exception {
        HistoricalSeasonImportRequest request = new HistoricalSeasonImportRequest(
                "Hypocrown",
                " Hypocrown#CAP ",
                "valorant-analisis:data/source/tracker/snapshots/hypocrown/season.json",
                objectMapper.readTree("""
                        {"data":[{
                          "type":"season",
                          "attributes":{"seasonId":"act-v26-a5","playlist":"competitive"},
                          "metadata":{"shortName":"V26: A5 Competitive"},
                          "stats":{
                            "matchesPlayed":{"value":32},
                            "matchesWon":{"value":17},
                            "matchesLost":{"value":15},
                            "matchesWinPct":{"value":53.125}
                          }
                        }]}"""
                )
        );

        HistoricalSeasonImportResponse created = historicalSeasonImportService.importTrackerSeason(request);
        HistoricalSeasonImportResponse duplicate = historicalSeasonImportService.importTrackerSeason(request);

        assertThat(created.created()).isTrue();
        assertThat(duplicate.created()).isFalse();
        assertThat(duplicate.importId()).isEqualTo(created.importId());
        assertThat(duplicate.playerRiotTag()).isEqualTo("Hypocrown#CAP");
        assertThat(playerRepository.count()).isEqualTo(1);
        assertThat(historicalImportRepository.count()).isEqualTo(1);
        assertThat(playerSeasonStatisticsRepository.count()).isEqualTo(1);
    }
}
