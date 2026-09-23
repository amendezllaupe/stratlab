package com.stratlab.historicalimport.entity;

import java.math.BigDecimal;

public record TrackerSeasonSnapshot(
        String seasonId,
        String seasonName,
        String playlist,
        int matchesPlayed,
        int matchesWon,
        int matchesLost,
        int matchesTied,
        BigDecimal winPercentage
) {
}
