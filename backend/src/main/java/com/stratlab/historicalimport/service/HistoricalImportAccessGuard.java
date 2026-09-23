package com.stratlab.historicalimport.service;

import com.stratlab.common.exception.HistoricalImportAccessDeniedException;
import com.stratlab.historicalimport.config.HistoricalImportProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class HistoricalImportAccessGuard {

    private final HistoricalImportProperties properties;

    public HistoricalImportAccessGuard(HistoricalImportProperties properties) {
        this.properties = properties;
    }

    public void requireAuthorized(String suppliedKey) {
        String configuredKey = properties.accessKey();
        if (configuredKey == null || configuredKey.isBlank() || suppliedKey == null
                || !MessageDigest.isEqual(configuredKey.getBytes(StandardCharsets.UTF_8), suppliedKey.getBytes(StandardCharsets.UTF_8))) {
            throw new HistoricalImportAccessDeniedException();
        }
    }
}
