CREATE TABLE countries
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(128) NOT NULL,
    phone_code INTEGER      NOT NULL
);

CREATE TABLE users
(
    id           SERIAL PRIMARY KEY,
    username     VARCHAR(64)                                                           NOT NULL UNIQUE,
    password     VARCHAR(255)                                                          NOT NULL,
    role         INTEGER CHECK ( role >= 0 AND role < 2 )                              NOT NULL,
    is_enabled   BOOLEAN                                                               NOT NULL,
    first_name   VARCHAR(64)                                                           NOT NULL,
    last_name    VARCHAR(64)                                                           NOT NULL,
    patronymic   VARCHAR(64),
    phone_number BIGINT                                                                NOT NULL CHECK (phone_number BETWEEN 1000000000 AND 9999999999),
    country_id   INTEGER REFERENCES countries (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    is_image_presented BOOLEAN DEFAULT false NOT NULL
);

CREATE TABLE users_images
(
    user_id INTEGER REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE PRIMARY KEY,
    image   BYTEA NOT NULL
);

CREATE TABLE refresh_tokens
(
    id         VARCHAR(255) PRIMARY KEY,
    user_id    INTEGER REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    token      VARCHAR(512)                                                      NOT NULL,
    expires_on TIMESTAMP                                                         NOT NULl
);

