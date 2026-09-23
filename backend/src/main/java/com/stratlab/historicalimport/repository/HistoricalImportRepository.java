package com.stratlab.historicalimport.repository;

import com.stratlab.historicalimport.entity.HistoricalImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HistoricalImportRepository extends JpaRepository<HistoricalImport, Long> {

    Optional<HistoricalImport> findByChecksum(String checksum);

    @Query(value = "SELECT pg_advisory_xact_lock(hashtext(:lockKey))", nativeQuery = true)
    void acquireTransactionLock(@Param("lockKey") String lockKey);
}
