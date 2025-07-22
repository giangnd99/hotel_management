-- Xóa database cũ và tạo lại
DROP DATABASE IF EXISTS customer_management;
CREATE DATABASE IF NOT EXISTS customer_management;
USE customer_management;

-- Bảng Customer
CREATE TABLE customer
(
    customer_id     BINARY(16) PRIMARY KEY,
    user_id         BINARY(16),
    first_name      VARCHAR(100)                                                     NOT NULL,
    last_name       VARCHAR(100)                                                     NOT NULL,
    address         TEXT,
    image_url       VARCHAR(100),
    date_of_birth   DATE,
    level           ENUM ('NONE', 'BRONZE', 'SILVER', 'GOLD', 'PLATINUM', 'DIAMOND') NOT NULL DEFAULT 'NONE',
    behavior_data   JSON,
    created_at      DATETIME                                                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME                                                         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng Loyalty Point
CREATE TABLE loyalty_point
(
    loyalty_id   BINARY(16) PRIMARY KEY,
    customer_id  BINARY(16)     NOT NULL,
    points       DECIMAL(10, 2) NOT NULL DEFAULT 0.00 CHECK (points >= 0),
    last_updated DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE
);

-- Bảng Loyalty Transaction
CREATE TABLE loyalty_transaction
(
    transaction_id   INT AUTO_INCREMENT PRIMARY KEY,
    loyalty_id       BINARY(16)                        NOT NULL,
    points_changed   DECIMAL(10, 2)                    NOT NULL,
    transaction_type ENUM ('EARN', 'REDEEM', 'ADJUST') NOT NULL,
    transaction_date DATETIME                          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description      VARCHAR(255),
    FOREIGN KEY (loyalty_id) REFERENCES loyalty_point (loyalty_id) ON DELETE CASCADE
);

-- Bảng Birthday Notification Log
CREATE TABLE birthday_notification_log
(
    log_id          INT AUTO_INCREMENT PRIMARY KEY,
    customer_id     BINARY(16)              NOT NULL,
    sent_date       DATETIME                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status          ENUM ('SENT', 'FAILED') NOT NULL,
    message_content TEXT,
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE
);

INSERT INTO customer (customer_id, user_id, first_name, last_name, address, date_of_birth, level, behavior_data)
VALUES
    (UUID_TO_BIN('9f8d2531-6724-4a2f-b9cc-66e2fa4b9d01'), UUID_TO_BIN('a84f7cda-1b6b-4fa4-9943-6fdc99a6e97d'), 'Nguyen', 'Van A', '123 Lê Lợi, Q1, TP.HCM', '2000-01-01', 'BRONZE', '{
      "favoriteRoomTypes": "Deluxe",
      "frequentlyUsedServices": "SPA"
    }'),
    (UUID_TO_BIN('b2e5fc6e-69fc-4aeb-b237-65c7277e9b95'), UUID_TO_BIN('84c2e19d-4646-48a5-9e29-b18f3c8b620d'), 'Tran', 'Thi B', '234 Hai Bà Trưng, Q3, TP.HCM', '1995-03-12', 'SILVER', '{
      "favoriteRoomTypes": "Standard",
      "frequentlyUsedServices": "SPA"
    }');


INSERT INTO loyalty_point (loyalty_id, customer_id, points)
VALUES
    (UUID_TO_BIN('468e28fc-4c19-42ae-8457-0492f7000018'), UUID_TO_BIN('9f8d2531-6724-4a2f-b9cc-66e2fa4b9d01'), 24192.14),
    (UUID_TO_BIN('4efdc1a2-8c92-44f1-810d-3c03a85094aa'), UUID_TO_BIN('b2e5fc6e-69fc-4aeb-b237-65c7277e9b95'), 36382.07);


INSERT INTO loyalty_transaction (loyalty_id, points_changed, transaction_type, transaction_date, description)
VALUES
    (UUID_TO_BIN('468e28fc-4c19-42ae-8457-0492f7000018'), 5000.00, 'EARN', '2025-07-01 10:00:00', 'Earned via invoice INV001'),
    (UUID_TO_BIN('4efdc1a2-8c92-44f1-810d-3c03a85094aa'), -2000.00, 'REDEEM', '2025-07-03 15:00:00', 'Redeemed for gift');


INSERT INTO birthday_notification_log (customer_id, sent_date, status, message_content)
VALUES
    (UUID_TO_BIN('9f8d2531-6724-4a2f-b9cc-66e2fa4b9d01'), '2025-01-01 09:00:00', 'SENT', 'Chúc mừng sinh nhật A! Nhận ưu đãi sinh nhật 10%.'),
    (UUID_TO_BIN('b2e5fc6e-69fc-4aeb-b237-65c7277e9b95'), '2025-03-12 09:00:00', 'SENT', 'Chúc mừng sinh nhật B! Đừng quên kiểm tra ưu đãi của bạn.');
