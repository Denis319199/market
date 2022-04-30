CREATE TABLE developers
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(128) NOT NULL,
    address    VARCHAR(256) NOT NULL,
    country_id INTEGER      NOT NULL
);

CREATE TABLE games
(
    id                SERIAL PRIMARY KEY,
    name              VARCHAR(128)                                                           NOT NULL,
    description       VARCHAR(1024),
    developer_id      INTEGER REFERENCES developers (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    release_date      DATE                                                                   NOT NULL,
    price             NUMERIC(50, 2) CHECK (price >= 0)                                      NOT NULL,
    total_image_count INTEGER DEFAULT 0 CHECK ( total_image_count >= 0 )                     NOT NULL
);

CREATE TABLE games_images
(
    id      SERIAL PRIMARY KEY,
    game_id INTEGER REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    image   BYTEA                                                             NOT NULL
);

CREATE TABLE items
(
    id                 SERIAL PRIMARY KEY,
    name               VARCHAR(128)                                                      NOT NULL,
    game_id            INTEGER REFERENCES games (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    is_image_presented BOOLEAN DEFAULT false                                             NOT NULL
);

CREATE TABLE items_images
(
    item_id INTEGER REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE PRIMARY KEY,
    image   BYTEA NOT NULL
);

CREATE TABLE selling_items
(
    id        SERIAL PRIMARY KEY,
    item_id   INTEGER REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    seller_id INTEGER                                                           NOT NULL,
    price     NUMERIC(50, 2)                                                    NOT NULL CHECK (price >= 0)
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
    price         NUMERIC(50, 2)                                                    NOT NULL CHECK (price >= 0)
);