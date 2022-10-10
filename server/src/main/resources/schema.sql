CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name    VARCHAR(50)                                         NOT NULL,
    email   VARCHAR(50)                                         NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name        VARCHAR(120)                                        NOT NULL,
    CONSTRAINT UQ_CAT UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations
(
    location_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    lat         FLOAT,
    lon         FLOAT
);

CREATE TABLE IF NOT EXISTS compilations
(
    compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    pinned         BOOLEAN,
    title          VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS events
(
    event_id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    annotation         VARCHAR(2000),
    category_id        BIGINT,
    confirmed_requests BIGINT,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    description        VARCHAR(7000),
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    initiator          BIGINT,
    location_id        BIGINT,
    paid               BOOLEAN,
    participant_limit  BIGINT,
    published_On       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state              VARCHAR(20),
    title              VARCHAR(120),
    views              BIGINT,
    CONSTRAINT fk_user FOREIGN KEY (initiator) REFERENCES users (user_id),
    CONSTRAINT fk_categories FOREIGN KEY (category_id) REFERENCES categories (category_id),
    CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES locations (location_id)
);

CREATE TABLE IF NOT EXISTS comp_events
(
    comp_id_event BIGINT NOT NULL,
    event_id_comp BIGINT NOT NULL,

    CONSTRAINT fk_comp FOREIGN KEY (comp_id_event) REFERENCES compilations (compilation_id),
    CONSTRAINT fk_event FOREIGN KEY (event_id_comp) REFERENCES events (event_id)
);

CREATE TABLE IF NOT EXISTS requests
(
    request_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    created    TIMESTAMP WITHOUT TIME ZONE,
    event      BIGINT,
    requester  BIGINT,
    status     VARCHAR(20),
    CONSTRAINT fk_requester FOREIGN KEY (requester) REFERENCES users (user_id),
    CONSTRAINT fk_event FOREIGN KEY (event) REFERENCES events (event_id)
);

CREATE TABLE IF NOT EXISTS comments
(
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    text       VARCHAR(3000),
    event_id   BIGINT,
    author_id  BIGINT,
    created    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users (user_id),
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events (event_id)
);

CREATE TABLE IF NOT EXISTS bad_words
(
    word_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    word    VARCHAR(20)
);
