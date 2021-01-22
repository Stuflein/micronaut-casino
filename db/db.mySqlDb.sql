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
CREATE TABLE IF NOT EXISTS security
(
    id       VARCHAR(36) PRIMARY KEY NOT NULL,
    password VARCHAR(60)             NOT NULL
);
CREATE TABLE IF NOT EXISTS token
(
    id            VARCHAR(36) PRIMARY KEY NOT NULL,
    refresh_token VARCHAR(255)            NOT NULL,
    revoked       BOOLEAN default false
);

DROP TABLE account;
DROP TABLE security;
DROP TABLE token;


INSERT INTO account (id, username, email, credit, player, guest)
VALUES ('bd301596-7ebc-4581-b02d-40194c6099c6', 'test', 'test', 12, true, false);