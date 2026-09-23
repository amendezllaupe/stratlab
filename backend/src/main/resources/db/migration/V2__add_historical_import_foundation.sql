CREATE TABLE players (
    id           BIGSERIAL PRIMARY KEY,
    team_id      BIGINT REFERENCES teams(id),
    display_name VARCHAR(100) NOT NULL,
    riot_tag     VARCHAR(100) NOT NULL UNIQUE,
    riot_tag_normalized VARCHAR(100) NOT NULL UNIQUE,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE historical_imports (
    id               BIGSERIAL PRIMARY KEY,
    provider         VARCHAR(40) NOT NULL,
    source_type      VARCHAR(80) NOT NULL,
    source_reference VARCHAR(500),
    checksum         VARCHAR(64) NOT NULL UNIQUE,
    raw_payload      TEXT NOT NULL,
    imported_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE player_season_statistics (
    id                   BIGSERIAL PRIMARY KEY,
    player_id            BIGINT NOT NULL REFERENCES players(id),
    historical_import_id BIGINT NOT NULL UNIQUE REFERENCES historical_imports(id),
    season_id            VARCHAR(100) NOT NULL,
    season_name          VARCHAR(150) NOT NULL,
    playlist             VARCHAR(80) NOT NULL,
    matches_played       INTEGER NOT NULL,
    matches_won          INTEGER NOT NULL DEFAULT 0,
    matches_lost         INTEGER NOT NULL DEFAULT 0,
    matches_tied         INTEGER NOT NULL DEFAULT 0,
    win_percentage       NUMERIC(6, 3) NOT NULL DEFAULT 0,
    created_at           TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT player_season_statistics_non_negative CHECK (
        matches_played >= 0 AND matches_won >= 0 AND matches_lost >= 0 AND matches_tied >= 0
    )
);

CREATE INDEX idx_player_season_statistics_player_id ON player_season_statistics(player_id);
CREATE INDEX idx_player_season_statistics_season_id ON player_season_statistics(season_id);

COMMENT ON TABLE historical_imports IS 'Immutable audit trail for externally imported historical data.';
COMMENT ON COLUMN historical_imports.raw_payload IS 'Original JSON payload only; credentials, headers and HAR metadata are excluded.';
