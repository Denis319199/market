CREATE TABLE countries
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(128)      NOT NULL,
    phone_code  INTEGER           NOT NULL,
    users_count INTEGER DEFAULT 0 NOT NULL CHECK (users_count >= 0)
);

CREATE TABLE users
(
    id           SERIAL PRIMARY KEY,
    username     VARCHAR(64)                                                           NOT NULL UNIQUE,
    password     VARCHAR(255)                                                          NOT NULL,
    role         INTEGER                                                               NOT NULL,
    is_enabled   BOOLEAN                                                               NOT NULL,
    first_name   VARCHAR(64)                                                           NOT NULL,
    last_name    VARCHAR(64)                                                           NOT NULL,
    patronymic   VARCHAR(64),
    phone_number BIGINT                                                                NOT NULL CHECK (phone_number BETWEEN 1000000000 AND 9999999999),
    country_id   INTEGER REFERENCES countries (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL
);