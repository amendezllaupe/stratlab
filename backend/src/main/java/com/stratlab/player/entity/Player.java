package com.stratlab.player.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "riot_tag", nullable = false, unique = true, length = 100)
    private String riotTag;

    @Column(name = "riot_tag_normalized", nullable = false, unique = true, length = 100)
    private String riotTagNormalized;

    protected Player() {
    }

    public Player(String displayName, String riotTag, String riotTagNormalized) {
        this.displayName = displayName;
        this.riotTag = riotTag;
        this.riotTagNormalized = riotTagNormalized;
    }

    public Long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRiotTag() {
        return riotTag;
    }

    public String getRiotTagNormalized() {
        return riotTagNormalized;
    }
}
