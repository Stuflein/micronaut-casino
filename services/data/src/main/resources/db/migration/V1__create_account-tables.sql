CREATE TABLE IF NOT EXISTS account
(
    id               VARCHAR(36) PRIMARY KEY             NOT NULL,
    username         CHAR(255) UNIQUE                    NOT NULL,
    email            CHAR(255)                           NOT NULL,
    credit           BIGINT                              NOT NULL,
    enabled          BOOLEAN   DEFAULT true,
    account_expired  BOOLEAN   DEFAULT false,
    account_locked   BOOLEAN   DEFAULT false,
    password_expired BOOLEAN   DEFAULT false,
    last_login       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
--     refresh_token    VARCHAR(255) UNIQUE,
--     token_revoked    BOOLEAN   DEFAULT false,
    player           BOOLEAN,
    guest            BOOLEAN,
    admin            BOOLEAN DEFAULT false
);
