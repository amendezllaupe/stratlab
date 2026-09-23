package com.stratlab.common.exception;

public class HistoricalImportPayloadTooLargeException extends RuntimeException {

    public HistoricalImportPayloadTooLargeException() {
        super("Historical import payload exceeds the configured size limit.");
    }
}
