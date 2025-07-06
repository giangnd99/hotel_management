-- Tạo database
DROP DATABASE  IF EXISTS  customer_management;
CREATE DATABASE IF NOT EXISTS customer_management;
USE customer_management;

-- Bảng customer: Lưu thông tin cá nhân khách hàng
CREATE TABLE customer (
    customer_id BINARY(16) PRIMARY KEY,
    user_id BINARY(16),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    address TEXT,
    date_of_birth DATE,
    accumulated_spending DECIMAL(15,2) NOT NULL DEFAULT 0.00 CHECK (accumulated_spending >= 0),
    level ENUM('NONE', 'BRONZE', 'SILVER', 'GOLD', 'PLATINUM', 'DIAMOND') NOT NULL DEFAULT 'NONE',
    behavior_data JSON,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng loyalty: Quản lý điểm tích lũy
CREATE TABLE loyalty (
    loyalty_id BINARY(16) PRIMARY KEY,
    customer_id BINARY(16) NOT NULL,
    points DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (points >= 0),
    last_updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE
);

-- Bảng voucher: Quản lý voucher
CREATE TABLE voucher (
    voucher_id BINARY(16) PRIMARY KEY,
    customer_id BINARY(16) NOT NULL,
    promotion_id INT,
    code VARCHAR(20) NOT NULL UNIQUE,
    discount_percentage DECIMAL(5,2) NOT NULL CHECK (discount_percentage BETWEEN 0 AND 100),
    discount_amount DECIMAL(10,2) DEFAULT 0.00 CHECK (discount_amount >= 0),
    issue_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    status ENUM('ACTIVE', 'USED', 'EXPIRED') NOT NULL DEFAULT 'ACTIVE',
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
    CHECK (expiry_date >= issue_date)
);

-- Bảng loyalty_transaction: Lịch sử giao dịch điểm
CREATE TABLE loyalty_transaction (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id BINARY(16) NOT NULL,
    loyalty_id BINARY(16) NOT NULL,
    points_changed DECIMAL(10,2) NOT NULL,
    transaction_type ENUM('EARN', 'REDEEM', 'ADJUST') NOT NULL,
    transaction_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(255),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (loyalty_id) REFERENCES loyalty(loyalty_id) ON DELETE CASCADE
);

-- Bảng birthday_notification_log: Lịch sử gửi email sinh nhật
CREATE TABLE birthday_notification_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id BINARY(16) NOT NULL,
    voucher_id BINARY(16),
    sent_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('SENT', 'FAILED') NOT NULL,
    message_content TEXT,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (voucher_id) REFERENCES voucher(voucher_id) ON DELETE SET NULL
);
-- Insert dữ liệu mẫu vào bảng customer
INSERT INTO customer (customer_id, user_id, first_Name, last_Name,address, date_of_birth, accumulated_spending, level, behavior_data)
VALUES
(UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), UUID_TO_BIN('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'), 'John',' Doe', '123 hanoi st', '1990-01-01', 250000000.00, 'BRONZE', '{"favorite_room_type": "Deluxe", "last_visit": "2025-05-15"}'),
(UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'), 'Jane',' Roe', '456 saigon st', '1985-05-15', 600000000.00, 'SILVER', '{"favorite_room_type": "Standard", "last_visit": "2025-05-14"}');

-- loyalty
INSERT INTO loyalty (loyalty_id, customer_id, points)
VALUES
(UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), 25000.00),
(UUID_TO_BIN('44444444-4444-4444-4444-444444444444'), UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), 60000.00);

-- voucher
INSERT INTO voucher (voucher_id, customer_id, promotion_id, code, discount_percentage, discount_amount, issue_date, expiry_date, status)
VALUES
(UUID_TO_BIN('55555555-5555-5555-5555-555555555555'), UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), NULL, 'BDAY2025-JD', 10.00, 0.00, '2025-01-01', '2025-01-31', 'active'),
(UUID_TO_BIN('66666666-6666-6666-6666-666666666666'), UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), NULL, 'BDAY2025-JR', 10.00, 0.00, '2025-05-15', '2025-06-14', 'active');

-- loyalty_transaction
INSERT INTO loyalty_transaction (customer_id, loyalty_id, points_changed, transaction_type, transaction_date, description)
VALUES
(UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), 25000.00, 'EARN', '2025-05-16 10:00:00', 'Earned from invoice INV001'),
(UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), UUID_TO_BIN('44444444-4444-4444-4444-444444444444'), -5000.00, 'REDEEM', '2025-05-16 15:00:00', 'Redeemed for promotion LOYALTY500');

-- birthday_notification_log
INSERT INTO birthday_notification_log (customer_id, voucher_id, sent_date, status, message_content)
VALUES
(UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), UUID_TO_BIN('55555555-5555-5555-5555-555555555555'), '2025-01-01 09:00:00', 'SENT', 'Chúc mừng sinh nhật John Doe! Dùng mã BDAY2025-JD để được giảm 10%.'),
(UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), UUID_TO_BIN('66666666-6666-6666-6666-666666666666'), '2025-05-15 09:00:00', 'SENT', 'Chúc mừng sinh nhật Jane Roe! Dùng mã BDAY2025-JR để được giảm 10%.');
