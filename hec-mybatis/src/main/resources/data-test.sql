-- users 테이블에 데이터 삽입
INSERT INTO users (name, created_at, updated_at, is_deleted)
VALUES
    ('김태웅', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);


-- bank_account 테이블에 데이터 삽입
INSERT INTO bank_accounts (user_id, bank, account_number, balance, created_at, updated_at, is_deleted)
VALUES
    (1, 'KB', 1234567890, 1000.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);