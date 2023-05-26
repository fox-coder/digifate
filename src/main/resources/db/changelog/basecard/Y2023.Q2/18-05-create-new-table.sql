--changeset helen:table_base_card_initial
CREATE TABLE IF NOT EXISTS base_card
(
    id            SERIAL       NOT NULL,
    card_category VARCHAR(255) NOT NULL,
    title         VARCHAR(255) NOT NULL,
    in_use        BOOLEAN      NOT NULL DEFAULT FALSE
);