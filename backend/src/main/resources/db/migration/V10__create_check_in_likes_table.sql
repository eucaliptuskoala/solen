CREATE TABLE check_in_likes (
    id BIGSERIAL PRIMARY KEY,
    check_in_id BIGINT NOT NULL REFERENCES check_ins(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (check_in_id, user_id)
);
