DROP TABLE IF EXISTS endpoints_hit CASCADE;
--DROP TABLE IF EXISTS requests CASCADE;
--DROP TABLE IF EXISTS items CASCADE;
--DROP TABLE IF EXISTS bookings CASCADE;
--DROP TABLE IF EXISTS comments CASCADE;
--
CREATE TABLE IF NOT EXISTS endpoints_hit (
  id    BIGSERIAL    PRIMARY KEY,
  app  VARCHAR(120) NOT NULL,
  uri VARCHAR(512) NOT NULL,
  ip VARCHAR(50) NOT NULL,
  timestamp TIMESTAMP   WITHOUT TIME ZONE NOT NULL
);
--
--CREATE TABLE IF NOT EXISTS requests (
--    id           BIGSERIAL PRIMARY KEY,
--    description  TEXT      NOT NULL,
--    created TIMESTAMP   WITHOUT TIME ZONE NOT NULL,
--    requestor_id BIGINT    REFERENCES users (id) ON DELETE CASCADE
--);
--
--CREATE TABLE IF NOT EXISTS items (
--    id           BIGSERIAL    PRIMARY KEY,
--    name         VARCHAR(120) NOT NULL,
--    description  TEXT         NOT NULL,
--    is_available BOOLEAN      DEFAULT TRUE,
--    owner_id     BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
--    request_id   BIGINT       REFERENCES requests(id) ON DELETE CASCADE
--);
--
--CREATE TABLE IF NOT EXISTS bookings (
--    id         BIGSERIAL   PRIMARY KEY,
--    start_date TIMESTAMP   WITHOUT TIME ZONE NOT NULL,
--    end_date   TIMESTAMP   WITHOUT TIME ZONE NOT NULL,
--    item_id    INT NOT NULL REFERENCES items(id) ON DELETE CASCADE,
--    booker_id  INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
--    status     VARCHAR(20) NOT NULL
--);
--
--CREATE TABLE IF NOT EXISTS comments (
--    id        BIGSERIAL PRIMARY KEY,
--    text      TEXT NOT NULL,
--    item_id   INT  NOT NULL REFERENCES items(id) ON DELETE CASCADE,
--    author_id INT  NOT NULL REFERENCES users(id) ON DELETE CASCADE,
--    created   TIMESTAMP WITHOUT TIME ZONE NOT NULL
--);