CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES user_accounts ON DELETE CASCADE,
    role_id SMALLINT NOT NULL REFERENCES roles ON DELETE RESTRICT,

    PRIMARY KEY (user_id, role_id)
);

CREATE INDEX idx_user_roles_role_id ON user_roles(role_id);