package com.stratlab.historicalimport.repository;

import com.stratlab.historicalimport.entity.PlayerSeasonStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerSeasonStatisticsRepository extends JpaRepository<PlayerSeasonStatistics, Long> {
}
