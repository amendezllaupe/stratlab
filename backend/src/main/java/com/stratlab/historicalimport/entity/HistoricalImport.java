package com.stratlab.historicalimport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "historical_imports")
public class HistoricalImport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String provider;

    @Column(name = "source_type", nullable = false, length = 80)
    private String sourceType;

    @Column(name = "source_reference", length = 500)
    private String sourceReference;

    @Column(nullable = false, unique = true, length = 64)
    private String checksum;

    @Column(name = "raw_payload", nullable = false, columnDefinition = "TEXT")
    private String rawPayload;

    @Column(name = "imported_at", nullable = false)
    private OffsetDateTime importedAt;

    protected HistoricalImport() {
    }

    public HistoricalImport(String provider, String sourceType, String sourceReference, String checksum, String rawPayload) {
        this.provider = provider;
        this.sourceType = sourceType;
        this.sourceReference = sourceReference;
        this.checksum = checksum;
        this.rawPayload = rawPayload;
        this.importedAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getChecksum() {
        return checksum;
    }

    public String getSourceType() {
        return sourceType;
    }
}
