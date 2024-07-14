DELIMITER //

CREATE PROCEDURE IF NOT EXISTS checkout_order(IN order_id BIGINT, IN address_id BIGINT, IN payment_type VARCHAR(20))
BEGIN

    DECLARE v_state VARCHAR(20);
    DECLARE v_book_id BIGINT;
    DECLARE v_quantity BIGINT;

    DECLARE done INT DEFAULT 0;
    DECLARE cur CURSOR FOR
        SELECT book_id, quantity FROM order_items WHERE order_items.order_id = order_id;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    START TRANSACTION;
    SELECT state INTO v_state FROM orders WHERE orders.id = order_id;

    IF v_state != 'NEW' THEN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Order cannot be checked out';
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

        IF @v_available_quantity < v_quantity THEN
            ROLLBACK;
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Not enough stock';
        END IF;

        UPDATE books
        SET available_quantity = @v_available_quantity - v_quantity,
            total_sold         = @v_total_sold + v_quantity
        WHERE books.id = v_book_id;
    END LOOP;

    CLOSE cur;

    UPDATE orders
    SET address_id = address_id,
        orders.payment_type
                   = payment_type
    WHERE orders.id = order_id;

    IF payment_type = 'COD' THEN
        UPDATE orders SET state = 'PENDING' WHERE orders.id = order_id;
    ELSE
        UPDATE orders SET state = 'PAYMENT' WHERE orders.id = order_id;
    END IF;
    COMMIT;
end //

DELIMITER ;