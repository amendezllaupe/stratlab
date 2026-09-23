package com.stratlab.historicalimport.controller;

import com.stratlab.historicalimport.dto.HistoricalSeasonImportRequest;
import com.stratlab.historicalimport.dto.HistoricalSeasonImportResponse;
import com.stratlab.historicalimport.service.HistoricalImportAccessGuard;
import com.stratlab.historicalimport.service.HistoricalSeasonImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/historical-imports")
@ConditionalOnProperty(prefix = "stratlab.historical-import", name = "enabled", havingValue = "true")
@Tag(name = "Historical imports", description = "Controlled migration of externally exported historical data")
public class HistoricalSeasonImportController {

    private final HistoricalSeasonImportService historicalSeasonImportService;
    private final HistoricalImportAccessGuard historicalImportAccessGuard;

    public HistoricalSeasonImportController(
            HistoricalSeasonImportService historicalSeasonImportService,
            HistoricalImportAccessGuard historicalImportAccessGuard
    ) {
        this.historicalSeasonImportService = historicalSeasonImportService;
        this.historicalImportAccessGuard = historicalImportAccessGuard;
    }

    @PostMapping("/tracker/seasons")
    @Operation(summary = "Import a Tracker season snapshot", description = "Imports a manually exported Tracker statsv2 season payload. The endpoint never calls Tracker.gg.")
    public ResponseEntity<HistoricalSeasonImportResponse> importTrackerSeason(
            @RequestHeader("X-StratLab-Import-Key") String importAccessKey,
            @Valid @RequestBody HistoricalSeasonImportRequest request
    ) {
        historicalImportAccessGuard.requireAuthorized(importAccessKey);
        HistoricalSeasonImportResponse response = historicalSeasonImportService.importTrackerSeason(request);
        return ResponseEntity.status(response.created() ? HttpStatus.CREATED : HttpStatus.OK).body(response);
    }
}
