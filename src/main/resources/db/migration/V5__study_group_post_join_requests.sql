CREATE TABLE study_group_post_join_requests
(
    id         BIGSERIAL PRIMARY KEY,
    post_id    BIGINT  NOT NULL REFERENCES study_group_posts (id) ON DELETE CASCADE,
    user_id    BIGINT  NOT NULL REFERENCES user_accounts (id) ON DELETE CASCADE,
    status     INTEGER NOT NULL DEFAULT 0,
    message    TEXT,
    created_at TIMESTAMPTZ      DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ      DEFAULT CURRENT_TIMESTAMP,

    CHECK (status IN (0, 1, 2, 3)),
    UNIQUE (post_id, user_id)
);

CREATE INDEX idx_study_group_post_join_requests_user_id ON study_group_post_join_requests (user_id);
CREATE INDEX idx_study_group_post_join_requests_post_id ON study_group_post_join_requests (post_id);
