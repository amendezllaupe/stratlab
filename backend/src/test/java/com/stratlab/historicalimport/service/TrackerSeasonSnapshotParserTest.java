package com.stratlab.historicalimport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stratlab.common.exception.HistoricalImportValidationException;
import com.stratlab.historicalimport.entity.TrackerSeasonSnapshot;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TrackerSeasonSnapshotParserTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TrackerSeasonSnapshotParser parser = new TrackerSeasonSnapshotParser();

    @Test
    void parsesTrackerStatsv2SeasonPayload() throws Exception {
        TrackerSeasonSnapshot snapshot = parser.parse(objectMapper.readTree("""
                {
                  "data": [{
                    "type": "season",
                    "attributes": {"seasonId": "act-v26-a5", "playlist": "competitive"},
                    "metadata": {"shortName": "V26: A5 Competitive"},
                    "stats": {
                      "matchesPlayed": {"value": 32},
                      "matchesWon": {"value": 17},
                      "matchesLost": {"value": 15},
                      "matchesWinPct": {"value": 53.125}
                    }
                  }]
                }
                """));

        assertThat(snapshot.seasonId()).isEqualTo("act-v26-a5");
        assertThat(snapshot.seasonName()).isEqualTo("V26: A5 Competitive");
        assertThat(snapshot.playlist()).isEqualTo("competitive");
        assertThat(snapshot.matchesPlayed()).isEqualTo(32);
        assertThat(snapshot.matchesTied()).isZero();
        assertThat(snapshot.winPercentage()).isEqualByComparingTo(new BigDecimal("53.125"));
    }

    @Test
    void rejectsPayloadsThatAreNotTrackerSeasonPayloads() throws Exception {
        assertThatThrownBy(() -> parser.parse(objectMapper.readTree("{" + "\"data\":[{\"type\":\"loadout\"}]}")))
                .isInstanceOf(HistoricalImportValidationException.class)
                .hasMessage("Expected a Tracker season statsv2 payload.");
    }

    @Test
    void rejectsFractionalMatchCount() throws Exception {
        assertThatThrownBy(() -> parser.parse(objectMapper.readTree("""
                {"data":[{
                  "type":"season",
                  "attributes":{"seasonId":"id","playlist":"competitive"},
                  "metadata":{"shortName":"V26: A5 Competitive"},
                  "stats":{"matchesPlayed":{"value":1.5}}
                }]}""")))
                .isInstanceOf(HistoricalImportValidationException.class)
                .hasMessage("Tracker stat matchesPlayed must be a non-negative whole number.");
    }
}
