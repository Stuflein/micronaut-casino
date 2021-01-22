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
