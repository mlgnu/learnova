CREATE TABLE user_accounts (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(30) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL ,
    password_hash VARCHAR(100) NOT NULL,
    display_name VARCHAR(30) NOT NULL,
    bio TEXT,
    status INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CHECK (char_length(username) BETWEEN 3 AND 30),
    CHECK (char_length(display_name) BETWEEN 1 AND 50),
    CHECK (char_length(email) >= 5),
    CHECK (char_length(password_hash) >= 20),
    CHECK (status IN (1, 2, 3, 4)),
    CHECK (created_at <= updated_at),
    CHECK (char_length(bio) <= 2000)
);

COMMENT ON COLUMN user_accounts.status IS '{1: active, 2: disabled, 3: banned, 4: deleted';