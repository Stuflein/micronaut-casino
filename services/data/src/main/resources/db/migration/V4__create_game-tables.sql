CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS game(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v1mc(),
    game_type VARCHAR(36),
    player uuid,
    wager BIGINT,
    player_win BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    finished_at TIMESTAMP
);