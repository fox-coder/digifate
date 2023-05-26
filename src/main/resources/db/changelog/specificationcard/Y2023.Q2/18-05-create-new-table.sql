--changeset helen:table_specification_card_initial
CREATE TABLE IF NOT EXISTS specification_card
(
    id                             SERIAL       NOT NULL,
    card_category                  VARCHAR(255) NOT NULL,
    title                          VARCHAR(255) NOT NULL,
    in_use                         BOOLEAN      NOT NULL DEFAULT FALSE,
    first_question                 VARCHAR(255),
    first_question_category_array  VARCHAR(255),
    second_question                VARCHAR(255),
    second_question_category_array VARCHAR(255)
);