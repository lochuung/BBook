-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               9.0.0 - MySQL Community Server - GPL
-- Server OS:                    Linux
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

-- Dumping structure for table bbook.addresses
CREATE TABLE IF NOT EXISTS `addresses`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `created_date` datetime(6)  DEFAULT NULL,
    `updated_date` datetime(6)  DEFAULT NULL,
    `address_line` varchar(255) DEFAULT NULL,
    `district`     varchar(255) DEFAULT NULL,
    `full_name`    varchar(255) DEFAULT NULL,
    `is_default`   tinyint(1)   DEFAULT '0',
    `phone_number` varchar(255) DEFAULT NULL,
    `province`     varchar(255) DEFAULT NULL,
    `ward`         varchar(255) DEFAULT NULL,
    `user_id`      bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK1fa36y2oqhao3wgg2rw1pi459` (`user_id`),
    CONSTRAINT `FK1fa36y2oqhao3wgg2rw1pi459` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.addresses: ~0 rows (approximately)

-- Dumping structure for table bbook.authors
CREATE TABLE IF NOT EXISTS `authors`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `created_date` datetime(6)  DEFAULT NULL,
    `updated_date` datetime(6)  DEFAULT NULL,
    `description`  varchar(255) DEFAULT NULL,
    `image`        varchar(255) DEFAULT NULL,
    `name`         varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 578
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
-- Dumping structure for table bbook.books
CREATE TABLE IF NOT EXISTS `books`
(
    `id`                 bigint NOT NULL AUTO_INCREMENT,
    `created_date`       datetime(6)     DEFAULT NULL,
    `updated_date`       datetime(6)     DEFAULT NULL,
    `available_quantity` bigint          DEFAULT NULL,
    `average_rating`     double          DEFAULT '0',
    `deleted`            bit(1) NOT NULL DEFAULT b'0',
    `description`        text,
    `isbn`               varchar(255)    DEFAULT NULL,
    `page_count`         int             DEFAULT NULL,
    `price`              double          DEFAULT NULL,
    `published_date`     datetime(6)     DEFAULT NULL,
    `short_description`  text,
    `slug`               varchar(255)    DEFAULT NULL,
    `thumbnail_url`      varchar(255)    DEFAULT NULL,
    `title`              varchar(255)    DEFAULT NULL,
    `total_rating`       bigint          DEFAULT '0',
    `total_sold`         bigint          DEFAULT '0',
    `version`            bigint          DEFAULT NULL,
    `publisher_id`       int             DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_67hmc2c9xvynx28p9f1wl6prb` (`slug`),
    KEY `FKayy5edfrqnegqj3882nce6qo8` (`publisher_id`),
    CONSTRAINT `FKayy5edfrqnegqj3882nce6qo8` FOREIGN KEY (`publisher_id`) REFERENCES `publishers` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 428
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping structure for table bbook.book_author
CREATE TABLE IF NOT EXISTS `book_author`
(
    `book_id`   bigint NOT NULL,
    `author_id` bigint NOT NULL,
    KEY `FKro54jqpth9cqm1899dnuu9lqg` (`author_id`),
    KEY `FK91ierknt446aaqnjl4uxjyls3` (`book_id`),
    CONSTRAINT `FK91ierknt446aaqnjl4uxjyls3` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
    CONSTRAINT `FKro54jqpth9cqm1899dnuu9lqg` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
-- Dumping structure for table bbook.book_genre
CREATE TABLE IF NOT EXISTS `book_genre`
(
    `book_id`  bigint NOT NULL,
    `genre_id` bigint NOT NULL,
    KEY `FKnkh6m50njl8771p0ll3lylqp2` (`genre_id`),
    KEY `FKq0f88ptislu8lv230mvgonssl` (`book_id`),
    CONSTRAINT `FKnkh6m50njl8771p0ll3lylqp2` FOREIGN KEY (`genre_id`) REFERENCES `genres` (`id`),
    CONSTRAINT `FKq0f88ptislu8lv230mvgonssl` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- Dumping structure for table bbook.coupons
CREATE TABLE IF NOT EXISTS `coupons`
(
    `id`              bigint NOT NULL AUTO_INCREMENT,
    `created_date`    datetime(6)  DEFAULT NULL,
    `updated_date`    datetime(6)  DEFAULT NULL,
    `code`            varchar(255) DEFAULT NULL,
    `description`     varchar(255) DEFAULT NULL,
    `expired_date`    datetime(6)  DEFAULT NULL,
    `max_order_value` double       DEFAULT NULL,
    `max_use`         int          DEFAULT NULL,
    `min_order_value` double       DEFAULT NULL,
    `type`            tinyint      DEFAULT NULL,
    `value`           double       DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `coupons_chk_1` CHECK ((`type` between 0 and 1))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.coupons: ~0 rows (approximately)

-- Dumping structure for table bbook.flyway_schema_history
CREATE TABLE IF NOT EXISTS `flyway_schema_history`
(
    `installed_rank` int           NOT NULL,
    `version`        varchar(50)            DEFAULT NULL,
    `description`    varchar(200)  NOT NULL,
    `type`           varchar(20)   NOT NULL,
    `script`         varchar(1000) NOT NULL,
    `checksum`       int                    DEFAULT NULL,
    `installed_by`   varchar(100)  NOT NULL,
    `installed_on`   timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `execution_time` int           NOT NULL,
    `success`        tinyint(1)    NOT NULL,
    PRIMARY KEY (`installed_rank`),
    KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.flyway_schema_history: ~0 rows (approximately)

-- Dumping structure for table bbook.genres
CREATE TABLE IF NOT EXISTS `genres`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `created_date` datetime(6)  DEFAULT NULL,
    `updated_date` datetime(6)  DEFAULT NULL,
    `name`         varchar(255) DEFAULT NULL,
    `slug`         varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_fun8evwjl5g8ps273eipowm7f` (`slug`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 35
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping structure for table bbook.images
CREATE TABLE IF NOT EXISTS `images`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT,
    `created_date` datetime(6) DEFAULT NULL,
    `updated_date` datetime(6) DEFAULT NULL,
    `name`         varchar(255) NOT NULL,
    `url`          varchar(255) NOT NULL,
    `book_id`      bigint      DEFAULT NULL,
    `review_id`    bigint      DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_s1hn0flcjavvrkvbn1pd8dts2` (`name`),
    KEY `FKrgvkymonf2suo7xn6xo6bh53` (`book_id`),
    KEY `FKldf2s8wlcsqgmmrokc6syxw5q` (`review_id`),
    CONSTRAINT `FKldf2s8wlcsqgmmrokc6syxw5q` FOREIGN KEY (`review_id`) REFERENCES `reviews` (`id`),
    CONSTRAINT `FKrgvkymonf2suo7xn6xo6bh53` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.images: ~0 rows (approximately)

-- Dumping structure for table bbook.online_payments
CREATE TABLE IF NOT EXISTS `online_payments`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `created_date` datetime(6)                                      DEFAULT NULL,
    `updated_date` datetime(6)                                      DEFAULT NULL,
    `order_code`   varchar(255)                                     DEFAULT NULL,
    `payment_id`   varchar(255)                                     DEFAULT NULL,
    `status`       enum ('CANCELLED','PAID','PENDING','PROCESSING') DEFAULT NULL,
    `order_id`     bigint                                           DEFAULT NULL,
    `user_id`      bigint                                           DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKpsmvmibtff9iob5rpcmhp642g` (`order_id`),
    KEY `FKc697qen5xiog00tl4n38nj69a` (`user_id`),
    CONSTRAINT `FKc697qen5xiog00tl4n38nj69a` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKpsmvmibtff9iob5rpcmhp642g` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.online_payments: ~0 rows (approximately)

-- Dumping structure for table bbook.orders
CREATE TABLE IF NOT EXISTS `orders`
(
    `id`             bigint NOT NULL AUTO_INCREMENT,
    `created_date`   datetime(6)                                                         DEFAULT NULL,
    `updated_date`   datetime(6)                                                         DEFAULT NULL,
    `discount`       double                                                              DEFAULT NULL,
    `note`           varchar(255)                                                        DEFAULT NULL,
    `payment_type`   enum ('COD','ONLINE')                                               DEFAULT NULL,
    `quantity`       bigint                                                              DEFAULT NULL,
    `shipping_price` double                                                              DEFAULT NULL,
    `state`          enum ('CANCELLED','DELIVERED','NEW','PAYMENT','PENDING','SHIPPING') DEFAULT NULL,
    `subtotal`       double                                                              DEFAULT NULL,
    `total_price`    double                                                              DEFAULT NULL,
    `address_id`     bigint                                                              DEFAULT NULL,
    `coupon_id`      bigint                                                              DEFAULT NULL,
    `user_id`        bigint                                                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKhlglkvf5i60dv6dn397ethgpt` (`address_id`),
    KEY `FKn1d1gkxckw648m2n2d5gx0yx5` (`coupon_id`),
    KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
    CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKhlglkvf5i60dv6dn397ethgpt` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`),
    CONSTRAINT `FKn1d1gkxckw648m2n2d5gx0yx5` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.orders: ~0 rows (approximately)

-- Dumping structure for table bbook.order_items
CREATE TABLE IF NOT EXISTS `order_items`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `created_date` datetime(6) DEFAULT NULL,
    `updated_date` datetime(6) DEFAULT NULL,
    `price`        double      DEFAULT NULL,
    `quantity`     bigint      DEFAULT NULL,
    `total_price`  double      DEFAULT NULL,
    `book_id`      bigint      DEFAULT NULL,
    `order_id`     bigint      DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKi4ptndslo2pyfp9r1x0eulh9g` (`book_id`),
    KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
    CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
    CONSTRAINT `FKi4ptndslo2pyfp9r1x0eulh9g` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.order_items: ~0 rows (approximately)

-- Dumping structure for table bbook.privileges
CREATE TABLE IF NOT EXISTS `privileges`
(
    `id`   bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping structure for table bbook.publishers
CREATE TABLE IF NOT EXISTS `publishers`
(
    `id`           int NOT NULL AUTO_INCREMENT,
    `created_date` datetime(6)  DEFAULT NULL,
    `updated_date` datetime(6)  DEFAULT NULL,
    `name`         varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.publishers: ~0 rows (approximately)

-- Dumping structure for table bbook.reviews
CREATE TABLE IF NOT EXISTS `reviews`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `created_date` datetime(6)  DEFAULT NULL,
    `updated_date` datetime(6)  DEFAULT NULL,
    `description`  varchar(255) DEFAULT NULL,
    `rating`       int          DEFAULT NULL,
    `book_id`      bigint       DEFAULT NULL,
    `user_id`      bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK6a9k6xvev80se5rreqvuqr7f9` (`book_id`),
    KEY `FKcgy7qjc1r99dp117y9en6lxye` (`user_id`),
    CONSTRAINT `FK6a9k6xvev80se5rreqvuqr7f9` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
    CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.reviews: ~0 rows (approximately)

-- Dumping structure for table bbook.roles
CREATE TABLE IF NOT EXISTS `roles`
(
    `id`   bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping structure for table bbook.roles_privileges
CREATE TABLE IF NOT EXISTS `roles_privileges`
(
    `role_id`      bigint NOT NULL,
    `privilege_id` bigint NOT NULL,
    KEY `FK5duhoc7rwt8h06avv41o41cfy` (`privilege_id`),
    KEY `FK629oqwrudgp5u7tewl07ayugj` (`role_id`),
    CONSTRAINT `FK5duhoc7rwt8h06avv41o41cfy` FOREIGN KEY (`privilege_id`) REFERENCES `privileges` (`id`),
    CONSTRAINT `FK629oqwrudgp5u7tewl07ayugj` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping structure for table bbook.users
CREATE TABLE IF NOT EXISTS `users`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `created_date` datetime(6)             DEFAULT NULL,
    `updated_date` datetime(6)             DEFAULT NULL,
    `birthday`     varchar(255)            DEFAULT NULL,
    `email`        varchar(255)            DEFAULT NULL,
    `enabled`      bit(1) NOT NULL,
    `name`         varchar(255)            DEFAULT NULL,
    `is_using2fa`  bit(1) NOT NULL,
    `password`     varchar(255)            DEFAULT NULL,
    `phone_number` varchar(255)            DEFAULT NULL,
    `provider`     enum ('LOCAL','OAUTH2') DEFAULT NULL,
    `secret`       varchar(255)            DEFAULT NULL,
    `status`       varchar(255)            DEFAULT NULL,
    `username`     varchar(255)            DEFAULT NULL,
    `avatar_id`    bigint                  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
    UNIQUE KEY `UK_9q63snka3mdh91as4io72espi` (`phone_number`),
    UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`),
    KEY `FK2lttmx8vn9eecykig5sch3v0h` (`avatar_id`),
    CONSTRAINT `FK2lttmx8vn9eecykig5sch3v0h` FOREIGN KEY (`avatar_id`) REFERENCES `images` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Dumping structure for table bbook.users_roles
CREATE TABLE IF NOT EXISTS `users_roles`
(
    `user_id` bigint NOT NULL,
    `role_id` bigint NOT NULL,
    KEY `FKj6m8fwv7oqv74fcehir1a9ffy` (`role_id`),
    KEY `FK2o0jvgh89lemvvo17cbqvdxaa` (`user_id`),
    CONSTRAINT `FK2o0jvgh89lemvvo17cbqvdxaa` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FKj6m8fwv7oqv74fcehir1a9ffy` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

/*!40103 SET TIME_ZONE = IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE = IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS = IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES = IFNULL(@OLD_SQL_NOTES, 1) */;
