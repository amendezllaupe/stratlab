package com.stratlab.historicalimport.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HistoricalSeasonImportRequest(
        @NotBlank @Size(max = 100) String playerDisplayName,
        @NotBlank @Size(max = 100) String riotTag,
        @NotBlank @Size(max = 500) String sourceReference,
        @NotNull JsonNode payload
) {
}
