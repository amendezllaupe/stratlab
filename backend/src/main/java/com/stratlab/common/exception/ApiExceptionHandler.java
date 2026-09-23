package com.stratlab.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(HistoricalImportAccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleHistoricalImportAccess(HistoricalImportAccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(HistoricalImportPayloadTooLargeException.class)
    public ResponseEntity<Map<String, String>> handleHistoricalImportPayloadTooLarge(HistoricalImportPayloadTooLargeException exception) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(HistoricalImportValidationException.class)
    public ResponseEntity<Map<String, String>> handleHistoricalImportValidation(HistoricalImportValidationException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleRequestValidation(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "The import request is incomplete or invalid."));
    }
}
