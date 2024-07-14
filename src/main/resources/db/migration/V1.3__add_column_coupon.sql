DELIMITER //

CREATE PROCEDURE IF NOT EXISTS add_column_if_not_exists()
BEGIN
    DECLARE column_exists INT;

    -- Kiểm tra xem cột current_use đã tồn tại hay chưa
    SELECT COUNT(*)
    INTO column_exists
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'coupons'
      AND COLUMN_NAME = 'current_use';

    -- Nếu cột chưa tồn tại, thêm cột mới
    IF column_exists = 0 THEN
        ALTER TABLE coupons
            ADD COLUMN current_use INT DEFAULT 0;

        -- Kiểm tra xem cột version đã tồn tại hay chưa
        SELECT COUNT(*)
        INTO column_exists
        FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE()
          AND TABLE_NAME = 'coupons'
          AND COLUMN_NAME = 'version';

        -- Nếu cột chưa tồn tại, thêm cột mới
        IF column_exists = 0 THEN
            ALTER TABLE coupons
                ADD COLUMN version BIGINT DEFAULT 0;
        END IF;
    END IF;
END //

DELIMITER ;

CALL add_column_if_not_exists();

-- remove procedure
DROP PROCEDURE IF EXISTS add_column_if_not_exists;