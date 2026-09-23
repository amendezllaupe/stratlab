package com.stratlab.historicalimport.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stratlab.historical-import")
public record HistoricalImportProperties(
        boolean enabled,
        String accessKey,
        long maxPayloadBytes
) {
}
