CREATE TABLE study_group_posts
(
    id            BIGSERIAL PRIMARY KEY,
    creator_id    BIGINT       REFERENCES user_accounts (id) ON DELETE SET NULL,
    title         VARCHAR(200) NOT NULL,
    description   TEXT         NOT NULL,
    tags          VARCHAR(20)[],
    visibility    INTEGER      NOT NULL DEFAULT 0,
    created_at    TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP,
    deleted_at    TIMESTAMPTZ           DEFAULT NULL,
    search_vector TSVECTOR GENERATED ALWAYS AS (
        setweight(to_tsvector('english', title), 'A') ||
        setweight(to_tsvector('english', description), 'B')
        ) STORED,

    CHECK (visibility IN (0, 1))
);

CREATE INDEX idx_study_group_posts_search ON study_group_posts USING GIN (search_vector);
CREATE INDEX study_group_posts_tags ON study_group_posts USING GIN (tags);
CREATE INDEX idx_study_group_posts_creator_id ON study_group_posts (creator_id);