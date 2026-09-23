package com.stratlab.historicalimport.service;

import com.stratlab.common.exception.HistoricalImportAccessDeniedException;
import com.stratlab.historicalimport.config.HistoricalImportProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HistoricalImportAccessGuardTest {

    @Test
    void acceptsConfiguredImportAccessKey() {
        HistoricalImportAccessGuard guard = new HistoricalImportAccessGuard(new HistoricalImportProperties(true, "local-key", 1024));

        assertThatCode(() -> guard.requireAuthorized("local-key")).doesNotThrowAnyException();
    }

    @Test
    void rejectsMissingOrIncorrectImportAccessKey() {
        HistoricalImportAccessGuard guard = new HistoricalImportAccessGuard(new HistoricalImportProperties(true, "local-key", 1024));

        assertThatThrownBy(() -> guard.requireAuthorized("wrong-key"))
                .isInstanceOf(HistoricalImportAccessDeniedException.class);
        assertThatThrownBy(() -> guard.requireAuthorized(null))
                .isInstanceOf(HistoricalImportAccessDeniedException.class);
    }
}
