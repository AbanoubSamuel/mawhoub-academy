--liquibase formatted sql

--changeset abanoub-samuel:baseline-schema
--comment: Baseline = existing mawhoub schema exactly as in prod (MariaDB 11.8 dump of 2025-12-06): 15 tables, original engines/charsets/collations, indexes, auto-increment columns (counters omitted) and foreign keys. No data. On a schema that already has these tables (prod) it is only marked as run.
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name IN ('activity', 'allowed_origins', 'business_info', 'card', 'comment', 'content', 'course', 'document', 'notification', 'password_reset_token', 'purchase', 'school', 'specialty', 'study', 'user')

-- Tables

CREATE TABLE `activity` (
  `id` int(11) NOT NULL,
  `activity` varchar(255) DEFAULT NULL,
  `target_id` int(11) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `allowed_origins` (
  `id` bigint(20) NOT NULL,
  `origin` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `business_info` (
  `home_video_url` varchar(255) DEFAULT NULL,
  `user_guide_video_url` varchar(255) DEFAULT NULL,
  `user_guide_image_url` varchar(255) DEFAULT NULL,
  `times_of_work` varchar(255) DEFAULT NULL,
  `whatsapp_number` varchar(255) DEFAULT NULL,
  `viber_number` varchar(255) DEFAULT NULL,
  `mobile_number` varchar(255) DEFAULT NULL,
  `tel_number` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `id` int(11) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `card` (
  `card_id` int(11) NOT NULL,
  `card_key` varchar(32) NOT NULL,
  `price` int(11) NOT NULL,
  `creation_time_date` datetime NOT NULL,
  `is_deleted` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

CREATE TABLE `comment` (
  `comment_id` int(11) NOT NULL,
  `content_id` int(11) NOT NULL,
  `parent_comment_id` int(11) DEFAULT NULL,
  `author_id` int(11) NOT NULL,
  `description` text NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `creation_time_date` datetime NOT NULL,
  `anonymous` tinyint(1) NOT NULL,
  `seen_by_teacher` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

CREATE TABLE `content` (
  `content_id` int(11) NOT NULL,
  `title` mediumtext NOT NULL,
  `description` mediumtext NOT NULL,
  `length` int(11) NOT NULL,
  `video_id` text NOT NULL,
  `is_free` tinyint(1) NOT NULL,
  `creation_time_date` datetime NOT NULL,
  `visibility` int(11) NOT NULL,
  `order_index` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `video_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

CREATE TABLE `course` (
  `course_id` int(11) NOT NULL,
  `author_id` int(11) NOT NULL,
  `school_id` int(11) DEFAULT NULL,
  `name` mediumtext NOT NULL,
  `description` mediumtext NOT NULL,
  `price` int(11) NOT NULL,
  `original_price` int(11) NOT NULL DEFAULT 7000,
  `creation_time_date` datetime NOT NULL,
  `visibility` int(11) NOT NULL,
  `category` mediumtext NOT NULL,
  `field_id` int(11) NOT NULL,
  `grade` text NOT NULL,
  `semester` int(11) NOT NULL DEFAULT 0,
  `created_by` int(11) DEFAULT NULL,
  `study_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

CREATE TABLE `document` (
  `document_id` int(11) NOT NULL,
  `content_id` int(11) NOT NULL,
  `document_name` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE `notification` (
  `id` int(11) NOT NULL,
  `creation_timestamp` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  `content` text NOT NULL,
  `is_read` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

CREATE TABLE `password_reset_token` (
  `id` bigint(20) NOT NULL,
  `expiry_date` datetime NOT NULL,
  `token` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `purchase` (
  `purchase_id` int(11) NOT NULL,
  `card_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `activation_time_date` datetime NOT NULL,
  `is_deleted` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

CREATE TABLE `school` (
  `id` int(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `visibility` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

CREATE TABLE `specialty` (
  `id` int(11) NOT NULL,
  `specialty_order` int(11) NOT NULL,
  `title` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

CREATE TABLE `study` (
  `id` int(11) NOT NULL,
  `creation_date_time` datetime DEFAULT NULL,
  `lecturer_name` varchar(255) DEFAULT NULL,
  `original_price` double NOT NULL,
  `price` double NOT NULL,
  `purchases_count` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `visibility` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `full_name` mediumtext NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `email` varchar(64) NOT NULL,
  `token` varchar(1024) NOT NULL,
  `biography` mediumtext NOT NULL,
  `role` int(11) NOT NULL,
  `password` varchar(256) NOT NULL,
  `validated_email` tinyint(1) NOT NULL,
  `picture_url` text NOT NULL,
  `creation_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;



-- Primary keys, indexes, unique keys

ALTER TABLE `activity`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `allowed_origins`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `business_info`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `card`
  ADD PRIMARY KEY (`card_id`),
  ADD KEY `idx_card_id` (`card_id`);

ALTER TABLE `comment`
  ADD PRIMARY KEY (`comment_id`),
  ADD KEY `FKh1gtv412u19wcbx22177xbkjp` (`author_id`),
  ADD KEY `FKr9jchxqv43o7l4tv7qkoqr4di` (`content_id`),
  ADD KEY `FKhvh0e2ybgg16bpu229a5teje7` (`parent_comment_id`);

ALTER TABLE `content`
  ADD PRIMARY KEY (`content_id`);

ALTER TABLE `course`
  ADD PRIMARY KEY (`course_id`),
  ADD KEY `FKmaatvkqyrdpwoiq3opi1obk7b` (`author_id`),
  ADD KEY `fk_school` (`school_id`),
  ADD KEY `FKcs54bsmb6dm2uwqc4jdpqulio` (`created_by`),
  ADD KEY `idx_course_school_visibility` (`school_id`,`visibility`),
  ADD KEY `idx_course_visibility_school` (`visibility`,`school_id`),
  ADD KEY `idx_course_author_visibility` (`author_id`,`visibility`),
  ADD KEY `idx_course_school_visibility_` (`school_id`,`visibility`);

ALTER TABLE `document`
  ADD PRIMARY KEY (`document_id`),
  ADD KEY `FK76vrf9tgsc8stqo2g2pcff694` (`content_id`);

ALTER TABLE `notification`
  ADD PRIMARY KEY (`id`),
  ADD KEY `creation_timestamp` (`creation_timestamp`,`user_id`);

ALTER TABLE `password_reset_token`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `purchase`
  ADD PRIMARY KEY (`purchase_id`),
  ADD KEY `FK1p8k2mby007nu77e2jbtd34ux` (`course_id`),
  ADD KEY `FK86i0stm7cqsglqptdvjij1k3m` (`user_id`),
  ADD KEY `idx_purchase_card_id` (`card_id`),
  ADD KEY `idx_purchase_deleted` (`is_deleted`);

ALTER TABLE `school`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `specialty`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `study`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);



-- Auto-increment columns

ALTER TABLE `activity`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `allowed_origins`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `business_info`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT;

ALTER TABLE `card`
  MODIFY `card_id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `comment`
  MODIFY `comment_id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `content`
  MODIFY `content_id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `course`
  MODIFY `course_id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `document`
  MODIFY `document_id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `notification`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `password_reset_token`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `purchase`
  MODIFY `purchase_id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `school`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `specialty`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `study`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT;



-- Foreign keys

ALTER TABLE `comment`
  ADD CONSTRAINT `FKh1gtv412u19wcbx22177xbkjp` FOREIGN KEY (`author_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `FKhvh0e2ybgg16bpu229a5teje7` FOREIGN KEY (`parent_comment_id`) REFERENCES `comment` (`comment_id`),
  ADD CONSTRAINT `FKr9jchxqv43o7l4tv7qkoqr4di` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`);

ALTER TABLE `course`
  ADD CONSTRAINT `FKcs54bsmb6dm2uwqc4jdpqulio` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `FKmaatvkqyrdpwoiq3opi1obk7b` FOREIGN KEY (`author_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `fk_school` FOREIGN KEY (`school_id`) REFERENCES `school` (`id`);

ALTER TABLE `document`
  ADD CONSTRAINT `FK76vrf9tgsc8stqo2g2pcff694` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`);

ALTER TABLE `purchase`
  ADD CONSTRAINT `FK1p8k2mby007nu77e2jbtd34ux` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`),
  ADD CONSTRAINT `FK86i0stm7cqsglqptdvjij1k3m` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);
