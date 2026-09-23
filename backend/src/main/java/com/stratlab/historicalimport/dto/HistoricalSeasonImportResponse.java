package com.stratlab.historicalimport.dto;

public record HistoricalSeasonImportResponse(
        Long importId,
        String playerRiotTag,
        String seasonName,
        int matchesPlayed,
        boolean created
) {
}
