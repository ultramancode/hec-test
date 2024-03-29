-- users 테이블에 데이터 삽입
INSERT INTO users (name, created_at, updated_at, is_deleted)
VALUES
    ('김태웅', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
    ('이태웅', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    ('최태웅', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    ('박태웅', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    ('최태형', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    ('박태형', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    ('김태형', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    ('김정웅', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    ('김정형', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
    ('이정웅', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

-- bank_account 테이블에 데이터 삽입
INSERT INTO bank_accounts (user_id, bank, account_number, balance, created_at, updated_at, is_deleted)
VALUES
    (1L, 'KB', 1234567890, 1000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (2L, 'NH', 9876543210, 2000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE),
    (3L, 'WOORI', 5678901234, 1500, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (4L, 'SHINHAN',3456789012, 3000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (5L, 'KB', 789012345, 2500, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (6L, 'NH', 2345678901, 1800, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (7L, 'WOORI', 9012345678, 2200, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (8L, 'SHINHAN',4567890123, 3200, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (9L, 'KB', 6789012345, 2700, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
    (10L, 'NH', 3456789012, 1900, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);
