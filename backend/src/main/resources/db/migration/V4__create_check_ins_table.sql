CREATE TABLE check_ins (
    id BIGSERIAL PRIMARY KEY,
    practice_id BIGINT NOT NULL REFERENCES practices(id),
    date DATE NOT NULL,
    streak_value INT NOT NULL,
    content TEXT,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    mood VARCHAR(20),
    created_at TIMESTAMP NOT NULL,
    UNIQUE (practice_id, date)
);
