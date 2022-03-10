CREATE TABLE developers
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(128) NOT NULL,
    address    VARCHAR(256) NOT NULL,
    country_id INTEGER      NOT NULL
);

CREATE TABLE games
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(128)                                                           NOT NULL,
    developer_id INTEGER REFERENCES developers (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    release_date DATE                                                                   NOT NULL,
    price        MONEY                                                                  NOT NULL CHECK (price >= 0::MONEY)
);

CREATE TABLE games_images
(
    id      SERIAL PRIMARY KEY,
    game_id INTEGER REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    image   BYTEA                                                             NOT NULL
);

CREATE TABLE items
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(128)                                                      NOT NULL,
    game_id INTEGER REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL
);

CREATE TABLE items_images
(
    item_id INTEGER REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE PRIMARY KEY,
    image BYTEA NOT NULL
);

CREATE TABLE selling_items
(
    item_id   INTEGER REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    seller_id INTEGER                                                           NOT NULL,
    price     MONEY                                                             NOT NULL CHECK (price >= 0::MONEY)
);

CREATE TABLE users_items
(
    user_id  INTEGER                                                           NOT NULL,
    item_id  INTEGER REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    quantity INTEGER                                                           NOT NULL CHECK (quantity >= 0),
    PRIMARY KEY (user_id, item_id)
);

CREATE TABLE purchases
(
    id            SERIAL PRIMARY KEY,
    seller_id     INTEGER                                                           NOT NULL,
    customer_id   INTEGER                                                           NOT NULL,
    item_id       INTEGER REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    purchase_date DATE                                                              NOT NULL,
    price         MONEY                                                             NOT NULL CHECK (price >= 0::MONEY)
);