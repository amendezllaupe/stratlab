package com.stratlab.historicalimport.entity;

import com.stratlab.player.entity.Player;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "player_season_statistics")
public class PlayerSeasonStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "historical_import_id", nullable = false, unique = true)
    private HistoricalImport historicalImport;

    @Column(name = "season_id", nullable = false, length = 100)
    private String seasonId;

    @Column(name = "season_name", nullable = false, length = 150)
    private String seasonName;

    @Column(nullable = false, length = 80)
    private String playlist;

    @Column(name = "matches_played", nullable = false)
    private int matchesPlayed;

    @Column(name = "matches_won", nullable = false)
    private int matchesWon;

    @Column(name = "matches_lost", nullable = false)
    private int matchesLost;

    @Column(name = "matches_tied", nullable = false)
    private int matchesTied;

    @Column(name = "win_percentage", nullable = false, precision = 6, scale = 3)
    private BigDecimal winPercentage;

    protected PlayerSeasonStatistics() {
    }

    public PlayerSeasonStatistics(
            Player player,
            HistoricalImport historicalImport,
            TrackerSeasonSnapshot snapshot
    ) {
        this.player = player;
        this.historicalImport = historicalImport;
        this.seasonId = snapshot.seasonId();
        this.seasonName = snapshot.seasonName();
        this.playlist = snapshot.playlist();
        this.matchesPlayed = snapshot.matchesPlayed();
        this.matchesWon = snapshot.matchesWon();
        this.matchesLost = snapshot.matchesLost();
        this.matchesTied = snapshot.matchesTied();
        this.winPercentage = snapshot.winPercentage();
    }
}
