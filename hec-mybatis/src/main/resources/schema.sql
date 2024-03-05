CREATE TABLE users
(
    user_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);
CREATE TABLE bank_account
(
    account_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT NOT NULL,
    bank           ENUM('KB', 'NH', 'WOORI', 'SHINHAN') NOT NULL,
    account_number VARCHAR(50),
    balance        DECIMAL(10, 2),
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP,
    is_deleted     BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
)
