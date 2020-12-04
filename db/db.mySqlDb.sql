CREATE TABLE IF NOT EXISTS account
(
    id               VARCHAR(36) PRIMARY KEY             NOT NULL,
    username         CHAR(255)                           NOT NULL,
    email            CHAR(255)                           NOT NULL,
    credit           BIGINT                              NOT NULL,
    enabled          BOOLEAN   DEFAULT true,
    account_expired  BOOLEAN   DEFAULT false,
    account_locked   BOOLEAN   DEFAULT false,
    password_expired BOOLEAN   DEFAULT false,
    last_login       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    player           BOOLEAN,
    guest            BOOLEAN
);
DROP TABLE account;

## TestAccount
INSERT INTO account (id, username, email, credit, player, guest)
VALUES ('bd301596-7ebc-4581-b02d-40194c6099c6', 'test', 'test', 12, true, false);