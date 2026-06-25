-- StratLab initial schema baseline
-- This migration establishes the foundational table structure.
-- Full domain tables will be added in subsequent migrations.

CREATE TABLE IF NOT EXISTS teams (
    id          BIGSERIAL       PRIMARY KEY,
    name        VARCHAR(100)    NOT NULL,
    tag         VARCHAR(10)     NOT NULL,
    description TEXT,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

COMMENT ON TABLE  teams      IS 'Valorant teams registered in StratLab';
COMMENT ON COLUMN teams.name IS 'Full team name';
COMMENT ON COLUMN teams.tag  IS 'Short team identifier tag (e.g. TSM, SEN)';
