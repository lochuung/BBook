DELIMITER //

CREATE PROCEDURE IF NOT EXISTS cancel_order(IN order_id BIGINT)
BEGIN
    DECLARE v_state VARCHAR(20);
    DECLARE v_payment_type VARCHAR(20);
    DECLARE v_book_id BIGINT;
    DECLARE v_quantity BIGINT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
        SELECT book_id, quantity FROM order_items WHERE order_items.order_id = order_id;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    START TRANSACTION;
    SELECT state, payment_type INTO v_state, v_payment_type FROM orders WHERE orders.id = order_id;

    IF v_state != 'PAYMENT' AND (v_state != 'PENDING' OR v_payment_type != 'COD') THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Order cannot be cancelled';
    END IF;

    OPEN cur;
    read_loop:
    LOOP
        FETCH cur INTO v_book_id, v_quantity;
        IF done THEN
            LEAVE read_loop;
        END IF;

        SELECT books.available_quantity, books.total_sold
        INTO @v_available_quantity, @v_total_sold
        FROM books
        WHERE books.id = v_book_id FOR
        UPDATE;

        UPDATE books
        SET available_quantity = @v_available_quantity + v_quantity,
            total_sold         = @v_total_sold - v_quantity
        WHERE books.id = v_book_id;

    END LOOP;

    CLOSE cur;

    SELECT coupons.current_use
    INTO @v_total_use
    FROM coupons
             JOIN orders ON orders.coupon_id = coupons.id
    WHERE orders.id = order_id FOR
    UPDATE;

    UPDATE coupons
    SET current_use = @v_total_use - 1
    WHERE coupons.id = (SELECT coupon_id FROM orders WHERE orders.id = order_id);

    UPDATE orders SET state = 'CANCELLED' WHERE orders.id = order_id;
    COMMIT;

end //

DELIMITER ;