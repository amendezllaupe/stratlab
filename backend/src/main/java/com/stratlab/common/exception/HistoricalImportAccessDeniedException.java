package com.stratlab.common.exception;

public class HistoricalImportAccessDeniedException extends RuntimeException {

    public HistoricalImportAccessDeniedException() {
        super("Historical import access is not authorized.");
    }
}
