-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.4.0 - MySQL Community Server - GPL
-- Server OS:                    Linux
-- HeidiSQL Version:             12.7.0.6850
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table bbook.addresses
CREATE TABLE IF NOT EXISTS `addresses` (
                                           `is_default` tinyint(1) DEFAULT '0',
                                           `created_date` datetime(6) DEFAULT NULL,
                                           `id` bigint NOT NULL AUTO_INCREMENT,
                                           `updated_date` datetime(6) DEFAULT NULL,
                                           `user_id` bigint DEFAULT NULL,
                                           `address` varchar(255) DEFAULT NULL,
                                           `district` varchar(255) DEFAULT NULL,
                                           `full_name` varchar(255) DEFAULT NULL,
                                           `phone_number` varchar(255) DEFAULT NULL,
                                           `province` varchar(255) DEFAULT NULL,
                                           `ward` varchar(255) DEFAULT NULL,
                                           PRIMARY KEY (`id`),
                                           KEY `FK1fa36y2oqhao3wgg2rw1pi459` (`user_id`),
                                           CONSTRAINT `FK1fa36y2oqhao3wgg2rw1pi459` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.addresses: ~0 rows (approximately)

-- Dumping structure for table bbook.authors
CREATE TABLE IF NOT EXISTS `authors` (
                                         `created_date` datetime(6) DEFAULT NULL,
                                         `id` bigint NOT NULL AUTO_INCREMENT,
                                         `updated_date` datetime(6) DEFAULT NULL,
                                         `description` varchar(255) DEFAULT NULL,
                                         `image` varchar(255) DEFAULT NULL,
                                         `name` varchar(255) DEFAULT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.authors: ~0 rows (approximately)

-- Dumping structure for table bbook.books
CREATE TABLE IF NOT EXISTS `books` (
                                       `pages` int DEFAULT NULL,
                                       `price` double DEFAULT NULL,
                                       `publisher_id` int DEFAULT NULL,
                                       `available_quantity` bigint DEFAULT NULL,
                                       `created_date` datetime(6) DEFAULT NULL,
                                       `genre_id` bigint DEFAULT NULL,
                                       `id` bigint NOT NULL AUTO_INCREMENT,
                                       `published_date` datetime(6) DEFAULT NULL,
                                       `updated_date` datetime(6) DEFAULT NULL,
                                       `cover_image` varchar(255) DEFAULT NULL,
                                       `description` varchar(255) DEFAULT NULL,
                                       `title` varchar(255) DEFAULT NULL,
                                       PRIMARY KEY (`id`),
                                       KEY `FK9hsvoalyniowgt8fbufidqj3x` (`genre_id`),
                                       KEY `FKayy5edfrqnegqj3882nce6qo8` (`publisher_id`),
                                       CONSTRAINT `FK9hsvoalyniowgt8fbufidqj3x` FOREIGN KEY (`genre_id`) REFERENCES `genres` (`id`),
                                       CONSTRAINT `FKayy5edfrqnegqj3882nce6qo8` FOREIGN KEY (`publisher_id`) REFERENCES `publishers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.books: ~0 rows (approximately)

-- Dumping structure for table bbook.book_authors
CREATE TABLE IF NOT EXISTS `book_authors` (
                                              `author_id` bigint NOT NULL,
                                              `book_id` bigint NOT NULL,
                                              PRIMARY KEY (`author_id`,`book_id`),
                                              KEY `FKbhqtkv2cndf10uhtknaqbyo0a` (`book_id`),
                                              CONSTRAINT `FKbhqtkv2cndf10uhtknaqbyo0a` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
                                              CONSTRAINT `FKo86065vktj3hy1m7syr9cn7va` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.book_authors: ~0 rows (approximately)

-- Dumping structure for table bbook.coupons
CREATE TABLE IF NOT EXISTS `coupons` (
                                         `max_order_value` double DEFAULT NULL,
                                         `max_use` int DEFAULT NULL,
                                         `min_order_value` double DEFAULT NULL,
                                         `type` tinyint DEFAULT NULL,
                                         `value` double DEFAULT NULL,
                                         `created_date` datetime(6) DEFAULT NULL,
                                         `expired_date` datetime(6) DEFAULT NULL,
                                         `id` bigint NOT NULL AUTO_INCREMENT,
                                         `updated_date` datetime(6) DEFAULT NULL,
                                         `user_id` bigint DEFAULT NULL,
                                         `code` varchar(255) DEFAULT NULL,
                                         `description` varchar(255) DEFAULT NULL,
                                         PRIMARY KEY (`id`),
                                         KEY `FKhb27gggactdhu0i65fwiaxb0r` (`user_id`),
                                         CONSTRAINT `FKhb27gggactdhu0i65fwiaxb0r` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                                         CONSTRAINT `coupons_chk_1` CHECK ((`type` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.coupons: ~0 rows (approximately)

-- Dumping structure for table bbook.genres
CREATE TABLE IF NOT EXISTS `genres` (
                                        `created_date` datetime(6) DEFAULT NULL,
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `updated_date` datetime(6) DEFAULT NULL,
                                        `name` varchar(255) DEFAULT NULL,
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.genres: ~0 rows (approximately)

-- Dumping structure for table bbook.images
CREATE TABLE IF NOT EXISTS `images` (
                                        `book_id` bigint DEFAULT NULL,
                                        `created_date` datetime(6) DEFAULT NULL,
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `review_id` bigint DEFAULT NULL,
                                        `updated_date` datetime(6) DEFAULT NULL,
                                        `name` varchar(255) NOT NULL,
                                        `url` varchar(255) NOT NULL,
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `UK_s1hn0flcjavvrkvbn1pd8dts2` (`name`),
                                        KEY `FKrgvkymonf2suo7xn6xo6bh53` (`book_id`),
                                        KEY `FKldf2s8wlcsqgmmrokc6syxw5q` (`review_id`),
                                        CONSTRAINT `FKldf2s8wlcsqgmmrokc6syxw5q` FOREIGN KEY (`review_id`) REFERENCES `reviews` (`id`),
                                        CONSTRAINT `FKrgvkymonf2suo7xn6xo6bh53` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.images: ~0 rows (approximately)

-- Dumping structure for table bbook.orders
CREATE TABLE IF NOT EXISTS `orders` (
                                        `state` tinyint DEFAULT NULL,
                                        `total_price` double DEFAULT NULL,
                                        `total_price_after_discount` double DEFAULT NULL,
                                        `address_id` bigint DEFAULT NULL,
                                        `coupon_id` bigint DEFAULT NULL,
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `user_id` bigint DEFAULT NULL,
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `UK_hlnl243wmdrj3u8nrxw1qs9yt` (`address_id`),
                                        KEY `FKn1d1gkxckw648m2n2d5gx0yx5` (`coupon_id`),
                                        KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
                                        CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                                        CONSTRAINT `FKhlglkvf5i60dv6dn397ethgpt` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`),
                                        CONSTRAINT `FKn1d1gkxckw648m2n2d5gx0yx5` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`),
                                        CONSTRAINT `orders_chk_1` CHECK ((`state` between 0 and 3))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.orders: ~0 rows (approximately)

-- Dumping structure for table bbook.order_items
CREATE TABLE IF NOT EXISTS `order_items` (
                                             `price` double DEFAULT NULL,
                                             `total_price` double DEFAULT NULL,
                                             `book_id` bigint DEFAULT NULL,
                                             `id` bigint NOT NULL AUTO_INCREMENT,
                                             `order_id` bigint DEFAULT NULL,
                                             `quantity` bigint DEFAULT NULL,
                                             PRIMARY KEY (`id`),
                                             KEY `FKi4ptndslo2pyfp9r1x0eulh9g` (`book_id`),
                                             KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
                                             CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
                                             CONSTRAINT `FKi4ptndslo2pyfp9r1x0eulh9g` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.order_items: ~0 rows (approximately)

-- Dumping structure for table bbook.privileges
CREATE TABLE IF NOT EXISTS `privileges` (
                                            `id` bigint NOT NULL AUTO_INCREMENT,
                                            `name` varchar(255) DEFAULT NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.privileges: ~3 rows (approximately)
INSERT IGNORE INTO `privileges` (`id`, `name`) VALUES
                                                   (1, 'read'),
                                                   (2, 'write'),
                                                   (3, 'edit');

-- Dumping structure for table bbook.publishers
CREATE TABLE IF NOT EXISTS `publishers` (
                                            `id` int NOT NULL AUTO_INCREMENT,
                                            `name` varchar(255) DEFAULT NULL,
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.publishers: ~0 rows (approximately)

-- Dumping structure for table bbook.reviews
CREATE TABLE IF NOT EXISTS `reviews` (
                                         `rating` int DEFAULT NULL,
                                         `book_id` bigint DEFAULT NULL,
                                         `created_date` datetime(6) DEFAULT NULL,
                                         `id` bigint NOT NULL AUTO_INCREMENT,
                                         `updated_date` datetime(6) DEFAULT NULL,
                                         `user_id` bigint DEFAULT NULL,
                                         `description` varchar(255) DEFAULT NULL,
                                         PRIMARY KEY (`id`),
                                         KEY `FK6a9k6xvev80se5rreqvuqr7f9` (`book_id`),
                                         KEY `FKcgy7qjc1r99dp117y9en6lxye` (`user_id`),
                                         CONSTRAINT `FK6a9k6xvev80se5rreqvuqr7f9` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`),
                                         CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.reviews: ~0 rows (approximately)

-- Dumping structure for table bbook.roles
CREATE TABLE IF NOT EXISTS `roles` (
                                       `id` bigint NOT NULL AUTO_INCREMENT,
                                       `name` varchar(255) DEFAULT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.roles: ~2 rows (approximately)
INSERT IGNORE INTO `roles` (`id`, `name`) VALUES
                                              (1, 'USER'),
                                              (2, 'ADMIN');

-- Dumping structure for table bbook.roles_privileges
CREATE TABLE IF NOT EXISTS `roles_privileges` (
                                                  `privilege_id` bigint NOT NULL,
                                                  `role_id` bigint NOT NULL,
                                                  KEY `FK5duhoc7rwt8h06avv41o41cfy` (`privilege_id`),
                                                  KEY `FK629oqwrudgp5u7tewl07ayugj` (`role_id`),
                                                  CONSTRAINT `FK5duhoc7rwt8h06avv41o41cfy` FOREIGN KEY (`privilege_id`) REFERENCES `privileges` (`id`),
                                                  CONSTRAINT `FK629oqwrudgp5u7tewl07ayugj` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.roles_privileges: ~2 rows (approximately)
INSERT IGNORE INTO `roles_privileges` (`privilege_id`, `role_id`) VALUES
                                                                      (1, 1),
                                                                      (2, 2);

-- Dumping structure for table bbook.users
CREATE TABLE IF NOT EXISTS `users` (
                                       `enabled` bit(1) NOT NULL,
                                       `is_using2fa` bit(1) NOT NULL,
                                       `avatar_id` bigint DEFAULT NULL,
                                       `created_date` datetime(6) DEFAULT NULL,
                                       `id` bigint NOT NULL AUTO_INCREMENT,
                                       `updated_date` datetime(6) DEFAULT NULL,
                                       `birthday` varchar(255) DEFAULT NULL,
                                       `email` varchar(255) DEFAULT NULL,
                                       `name` varchar(255) DEFAULT NULL,
                                       `password` varchar(255) DEFAULT NULL,
                                       `phone_number` varchar(255) DEFAULT NULL,
                                       `secret` varchar(255) DEFAULT NULL,
                                       `status` varchar(255) DEFAULT NULL,
                                       `username` varchar(255) DEFAULT NULL,
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
                                       UNIQUE KEY `UK_9q63snka3mdh91as4io72espi` (`phone_number`),
                                       UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`),
                                       KEY `FK2lttmx8vn9eecykig5sch3v0h` (`avatar_id`),
                                       CONSTRAINT `FK2lttmx8vn9eecykig5sch3v0h` FOREIGN KEY (`avatar_id`) REFERENCES `images` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.users: ~2 rows (approximately)
INSERT IGNORE INTO `users` (`enabled`, `is_using2fa`, `avatar_id`, `created_date`, `id`, `updated_date`, `birthday`, `email`, `name`, `password`, `phone_number`, `secret`, `status`, `username`) VALUES
                                                                                                                                                                                                      (b'0', b'0', NULL, '2024-06-23 17:42:31.049617', 1, '2024-06-23 17:42:31.050223', NULL, 'user@cnj.vn', NULL, '$2a$10$ej4w/3SJAiKJgSmRRorQh.1rsqDdrw2b5QdQ03uiKONiYdqAk5fpa', NULL, NULL, NULL, 'user'),
                                                                                                                                                                                                      (b'0', b'0', NULL, '2024-06-23 17:42:31.151873', 2, '2024-06-23 17:42:31.152432', NULL, 'admin@cnj.vn', NULL, '$2a$10$WL83l5kshmTx2yjdPoycTuVal68Ut.O5n5qEh8BormrZVnkbXl9Iy', NULL, NULL, NULL, 'admin');

-- Dumping structure for table bbook.users_roles
CREATE TABLE IF NOT EXISTS `users_roles` (
                                             `role_id` bigint NOT NULL,
                                             `user_id` bigint NOT NULL,
                                             KEY `FKj6m8fwv7oqv74fcehir1a9ffy` (`role_id`),
                                             KEY `FK2o0jvgh89lemvvo17cbqvdxaa` (`user_id`),
                                             CONSTRAINT `FK2o0jvgh89lemvvo17cbqvdxaa` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                                             CONSTRAINT `FKj6m8fwv7oqv74fcehir1a9ffy` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bbook.users_roles: ~2 rows (approximately)
INSERT IGNORE INTO `users_roles` (`role_id`, `user_id`) VALUES
                                                            (1, 1),
                                                            (2, 2);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
