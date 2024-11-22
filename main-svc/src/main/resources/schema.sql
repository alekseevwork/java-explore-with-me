DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;

CREATE TABLE IF NOT EXISTS categories (
    id    BIGSERIAL   PRIMARY KEY,
    name  VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id       BIGSERIAL    PRIMARY KEY,
    name     VARCHAR(250) NOT NULL,
    email    VARCHAR(254) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations (
    id      BIGSERIAL   PRIMARY KEY,
    title   VARCHAR(50) NOT NULL,
    pinned  BOOLEAN     NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id                 BIGSERIAL     PRIMARY KEY,
    annotation         VARCHAR(2000) NOT NULL,
    initiator_id       BIGINT        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    category_id        BIGINT        NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    created_on         TIMESTAMP     WITHOUT TIME ZONE NOT NULL,
    description        VARCHAR(7000) NOT NULL,
    event_date         TIMESTAMP     WITHOUT TIME ZONE NOT NULL,
    lat                FLOAT         NOT NULL,
    lon                FLOAT         NOT NULL,
    paid               BOOLEAN       NOT NULL,
    participant_limit  INT           NOT NULL,
    request_moderation BOOLEAN       NOT NULL,
    published_on       TIMESTAMP     WITHOUT TIME ZONE,
    title              VARCHAR(120)  NOT NULL,
    state              VARCHAR(50)   NOT NULL
);

CREATE TABLE IF NOT EXISTS requests (
    id           BIGSERIAL PRIMARY KEY,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id     BIGINT    NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    requester_id BIGINT    NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status       VARCHAR(100)
);

create TABLE IF NOT EXISTS compilations_events (
    compilation_id BIGINT NOT NULL REFERENCES compilations(id) ON DELETE CASCADE,
    event_id       BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE
);
