CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    email VARCHAR(80) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_admin BOOLEAN NOT NULL
);

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT REFERENCES categories(id) ON DELETE SET NULL
);

CREATE TABLE practices (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(255) NOT NULL,
    streak INT NOT NULL,
    last_updated_streak TIMESTAMP,
    threshold_days INT NOT NULL,
    category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
    creator_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE check_ins (
    id BIGSERIAL PRIMARY KEY,
    practice_id BIGINT NOT NULL REFERENCES practices(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    streak_value INT NOT NULL,
    content TEXT,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    mood VARCHAR(20),
    created_at TIMESTAMP NOT NULL,
    UNIQUE (practice_id, date)
);

CREATE TABLE check_in_likes (
    id BIGSERIAL PRIMARY KEY,
    check_in_id BIGINT NOT NULL REFERENCES check_ins(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (check_in_id, user_id)
);

CREATE TABLE tokens (
    id BIGSERIAL PRIMARY KEY,
    creator_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token VARCHAR(36) NOT NULL,
    expiration TIMESTAMP NOT NULL
);
