CREATE TABLE practices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(255) NOT NULL,
    streak INT NOT NULL,
    last_updated_streak TIMESTAMP,
    threshold_days INT NOT NULL,
    category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
    creator_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);
