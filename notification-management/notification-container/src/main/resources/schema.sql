-- BƯỚC 1: TẠO CƠ SỞ DỮ LIỆU
DROP DATABASE IF EXISTS `notification_db`;
CREATE DATABASE IF NOT EXISTS `notification_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `notification_db`;

-- BƯỚC 2: TẠO BẢNG `qr_codes`
-- Bảng lưu trữ thông tin về mã QR được tạo
CREATE TABLE IF NOT EXISTS `qr_codes` (
                                          `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          `data` VARCHAR(255) NOT NULL,
    `booking_id` VARCHAR(255) NOT NULL COMMENT 'Liên kết tới Booking ID',
    `qr_code_image_path` TEXT,
    `width` INT NOT NULL DEFAULT 300,
    `height` INT NOT NULL DEFAULT 300,
    `format` ENUM('PNG', 'JPG', 'JPEG', 'GIF') NOT NULL DEFAULT 'PNG' COMMENT 'Định dạng ảnh của QR code',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `description` TEXT,
    `scanned_at` TIMESTAMP NULL,
    `is_scanned` BOOLEAN NOT NULL DEFAULT FALSE,

    -- Indexes
    INDEX `idx_data` (`data`),
    INDEX `idx_booking_id` (`booking_id`),
    INDEX `idx_is_active` (`is_active`),
    INDEX `idx_is_scanned` (`is_scanned`),

    -- Constraints
    CONSTRAINT `chk_width` CHECK (`width` BETWEEN 100 AND 1000),
    CONSTRAINT `chk_height` CHECK (`height` BETWEEN 100 AND 1000)
    );

-- BƯỚC 3: TẠO BẢNG `notifications`
-- Bảng lưu trữ lịch sử các thông báo đã gửi
CREATE TABLE IF NOT EXISTS `notifications` (
                                               `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                               `booking_id` VARCHAR(255) NOT NULL COMMENT 'Liên kết tới Booking ID',
    `user_id` VARCHAR(255) NOT NULL,
    `notification_method` ENUM('EMAIL', 'SMS', 'PUSH') NOT NULL COMMENT 'Phương thức thông báo',
    `message` TEXT NOT NULL,
    `priority` ENUM('HIGH', 'MEDIUM', 'LOW') COMMENT 'Mức độ ưu tiên của thông báo',
    `status` ENUM('SENT', 'FAILED', 'PENDING', 'CANCELLED') NOT NULL COMMENT 'Trạng thái của thông báo',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Indexes
    INDEX `idx_booking_id_notifications` (`booking_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`)
    );