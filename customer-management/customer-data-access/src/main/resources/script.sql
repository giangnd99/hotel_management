-- Tạo database
CREATE DATABASE IF NOT EXISTS customer_management;
USE customer_management;

-- Bảng Customer: Lưu thông tin cá nhân khách hàng
CREATE TABLE Customer (
                          customer_id INT AUTO_INCREMENT PRIMARY KEY,
                          user_id CHAR(36),
                          name VARCHAR(100) NOT NULL,
                          phone VARCHAR(20) NOT NULL,
                          email VARCHAR(100) NOT NULL,
                          address TEXT,
                          date_of_birth DATE,
                          nationality VARCHAR(50),
                          customer_type ENUM('Regular', 'VIP', 'Corporate', 'Other') NOT NULL DEFAULT 'Regular',
                          accumulated_spending DECIMAL(15,2) NOT NULL DEFAULT 0.00 CHECK (accumulated_spending >= 0),
                          level ENUM('None', 'Bronze', 'Silver', 'Gold') NOT NULL DEFAULT 'None',
                          behavior_data JSON,  -- Yêu cầu MySQL >= 5.7
                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng Loyalty: Quản lý điểm tích lũy
CREATE TABLE Loyalty (
                         loyalty_id INT AUTO_INCREMENT PRIMARY KEY,
                         customer_id INT NOT NULL,
                         points DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (points >= 0),
                         last_updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE
);

-- Bảng Voucher: Quản lý voucher (bao gồm voucher sinh nhật)
-- Giả định Promotion là service khác, sẽ mapping sau
CREATE TABLE Voucher (
                         voucher_id INT AUTO_INCREMENT PRIMARY KEY,
                         customer_id INT NOT NULL,
                         promotion_id INT,
                         code VARCHAR(20) NOT NULL UNIQUE,
                         discount_percentage DECIMAL(5,2) NOT NULL CHECK (discount_percentage BETWEEN 0 AND 100),
                         discount_amount DECIMAL(10,2) DEFAULT 0.00 CHECK (discount_amount >= 0),
                         issue_date DATE NOT NULL,
                         expiry_date DATE NOT NULL,
                         status ENUM('Active', 'Used', 'Expired') NOT NULL DEFAULT 'Active',
                         FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE,
    -- FOREIGN KEY (promotion_id) REFERENCES promotion_service.Promotion(promotion_id) ON DELETE SET NULL,
                         CHECK (expiry_date >= issue_date)
);

-- Bảng Loyalty_Transaction: Lịch sử giao dịch điểm
CREATE TABLE Loyalty_Transaction (
                                     transaction_id INT AUTO_INCREMENT PRIMARY KEY,
                                     customer_id INT NOT NULL,
                                     loyalty_id INT NOT NULL,
                                     points_changed DECIMAL(10,2) NOT NULL,
                                     transaction_type ENUM('Earn', 'Redeem', 'Adjust') NOT NULL,
                                     transaction_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     description VARCHAR(255),
                                     FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE,
                                     FOREIGN KEY (loyalty_id) REFERENCES Loyalty(loyalty_id) ON DELETE CASCADE
);

-- Bảng BirthdayNotificationLog: Lịch sử gửi email
CREATE TABLE BirthdayNotificationLog (
                                         log_id INT AUTO_INCREMENT PRIMARY KEY,
                                         customer_id INT NOT NULL,
                                         voucher_id INT,
                                         sent_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         status ENUM('Sent', 'Failed') NOT NULL,
                                         message_content TEXT,
                                         FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE,
                                         FOREIGN KEY (voucher_id) REFERENCES Voucher(voucher_id) ON DELETE SET NULL
);

-- Sample Data
INSERT INTO Customer (user_id, name, phone, email, address, date_of_birth, nationality, customer_type, accumulated_spending, level, behavior_data)
VALUES
    (UUID(), 'John Doe', '+84912345678', 'john.doe@example.com', '123 Hanoi St', '1990-01-01', 'Vietnam', 'VIP', 250000000.00, 'Bronze', '{"favorite_room_type": "Deluxe", "last_visit": "2025-05-15"}'),
    (UUID(), 'Jane Roe', '+84987654321', 'jane.roe@example.com', '456 Saigon St', '1985-05-15', 'USA', 'Regular', 600000000.00, 'Silver', '{"favorite_room_type": "Standard", "last_visit": "2025-05-14"}');

INSERT INTO Loyalty (customer_id, points) VALUES
                                              (1, 25000.00),
                                              (2, 60000.00);

INSERT INTO Voucher (customer_id, promotion_id, code, discount_percentage, discount_amount, issue_date, expiry_date, status) VALUES
                                                                                                                                 (1, NULL, 'BDAY2025-JD', 10.00, 0.00, '2025-01-01', '2025-01-31', 'Active'),
                                                                                                                                 (2, NULL, 'BDAY2025-JR', 10.00, 0.00, '2025-05-15', '2025-06-14', 'Active');

INSERT INTO Loyalty_Transaction (customer_id, loyalty_id, points_changed, transaction_type, transaction_date, description) VALUES
                                                                                                                               (1, 1, 25000.00, 'Earn', '2025-05-16 10:00:00', 'Earned from invoice INV001'),
                                                                                                                               (2, 2, -5000.00, 'Redeem', '2025-05-16 15:00:00', 'Redeemed for promotion LOYALTY500');

INSERT INTO BirthdayNotificationLog (customer_id, voucher_id, sent_date, status, message_content) VALUES
                                                                                                      (1, 1, '2025-01-01 09:00:00', 'Sent', 'Chúc mừng sinh nhật John Doe! Dùng mã BDAY2025-JD để được giảm 10%.'),
                                                                                                      (2, 2, '2025-05-15 09:00:00', 'Sent', 'Chúc mừng sinh nhật Jane Roe! Dùng mã BDAY2025-JR để được giảm 10%.');
