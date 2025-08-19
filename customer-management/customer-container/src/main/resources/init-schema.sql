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
    sex             ENUM('FEMALE','MALE','OTHER')                                    NOT NULL DEFAULT 'OTHER',
    address         TEXT,
    image_url       VARCHAR(100),
    date_of_birth   DATE,
    level           ENUM ('NONE', 'BRONZE', 'SILVER', 'GOLD', 'PLATINUM', 'DIAMOND') NOT NULL DEFAULT 'NONE',
    behavior_data   JSON,
    active          BOOLEAN                                                          NOT NULL DEFAULT TRUE,
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

INSERT INTO customer (customer_id, user_id, first_name, last_name, sex, address, image_url, date_of_birth, level, behavior_data, active)
VALUES
    (UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), UUID_TO_BIN('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'),'Nguyen','Van A','MALE','123A Lê Lợi, Q1, TP.HCM',NULL,'2000-01-01','BRONZE','{"favoriteRoomTypes":"Deluxe","frequentlyUsedServices":"SPA"}',TRUE),
    (UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), UUID_TO_BIN('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12'),'Tran','Thi B','FEMALE','234B Hai Bà Trưng, Q3, TP.HCM',NULL,'1995-03-12','SILVER','{"favoriteRoomTypes":"Standard","frequentlyUsedServices":"Gym"}',TRUE),
    (UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), UUID_TO_BIN('cccccccc-cccc-cccc-cccc-cccccccccccc'),'Le','Van C','MALE','56C Pasteur, Q1, TP.HCM',NULL,'1990-07-15','GOLD','{"favoriteRoomTypes":"Suite","frequentlyUsedServices":"Bar"}',TRUE),
    (UUID_TO_BIN('44444444-4444-4444-4444-444444444444'), UUID_TO_BIN('dddddddd-dddd-dddd-dddd-dddddddddddd'),'Pham','Thi D','FEMALE','89D Nguyễn Huệ, Q1, TP.HCM',NULL,'1988-11-30','PLATINUM','{"favoriteRoomTypes":"Deluxe","frequentlyUsedServices":"SPA"}',FALSE),
    (UUID_TO_BIN('55555555-5555-5555-5555-555555555555'), UUID_TO_BIN('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee'),'Hoang','Van E','MALE','101E Điện Biên Phủ, Q.Bình Thạnh, TP.HCM',NULL,'1992-05-25','DIAMOND','{"favoriteRoomTypes":"Standard","frequentlyUsedServices":"Pool"}',TRUE),
    (UUID_TO_BIN('66666666-6666-6666-6666-666666666666'), UUID_TO_BIN('ffffffff-ffff-ffff-ffff-ffffffffffff'),'Do','Thi F','FEMALE','202F Nguyễn Văn Cừ, Q5, TP.HCM',NULL,'1998-09-09','BRONZE','{"favoriteRoomTypes":"Suite","frequentlyUsedServices":"Karaoke"}',TRUE),
    (UUID_TO_BIN('77777777-7777-7777-7777-777777777777'), UUID_TO_BIN('12121212-1212-1212-1212-121212121212'),'Bui','Van G','MALE','303G Lê Văn Sỹ, Q.Phú Nhuận, TP.HCM',NULL,'2001-12-01','SILVER','{"favoriteRoomTypes":"Deluxe","frequentlyUsedServices":"Gym"}',FALSE),
    (UUID_TO_BIN('88888888-8888-8888-8888-888888888888'), UUID_TO_BIN('13131313-1313-1313-1313-131313131313'),'Ngo','Thi H','FEMALE','404H Trần Hưng Đạo, Q5, TP.HCM',NULL,'1993-02-20','GOLD','{"favoriteRoomTypes":"Standard","frequentlyUsedServices":"SPA"}',TRUE),
    (UUID_TO_BIN('99999999-9999-9999-9999-999999999999'), UUID_TO_BIN('14141414-1414-1414-1414-141414141414'),'Vo','Van I','MALE','505I Nguyễn Trãi, Q1, TP.HCM',NULL,'1985-08-18','PLATINUM','{"favoriteRoomTypes":"Suite","frequentlyUsedServices":"Pool"}',TRUE),
    (UUID_TO_BIN('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01'), UUID_TO_BIN('15151515-1515-1515-1515-151515151515'),'Dang','Thi J','FEMALE','606J CMT8, Q10, TP.HCM',NULL,'1999-04-04','DIAMOND','{"favoriteRoomTypes":"Deluxe","frequentlyUsedServices":"Karaoke"}',TRUE);

-- Insert 10 Loyalty Points
INSERT INTO loyalty_point (loyalty_id, customer_id, points)
VALUES
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0001'), UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), 1200.50),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0002'), UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), 2400.00),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0003'), UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), 3600.75),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0004'), UUID_TO_BIN('44444444-4444-4444-4444-444444444444'), 1500.00),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0005'), UUID_TO_BIN('55555555-5555-5555-5555-555555555555'), 4700.25),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0006'), UUID_TO_BIN('66666666-6666-6666-6666-666666666666'), 520.00),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0007'), UUID_TO_BIN('77777777-7777-7777-7777-777777777777'), 987.45),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0008'), UUID_TO_BIN('88888888-8888-8888-8888-888888888888'), 3320.60),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0009'), UUID_TO_BIN('99999999-9999-9999-9999-999999999999'), 7500.10),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0010'), UUID_TO_BIN('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01'), 1999.99);

-- Insert 10 Loyalty Transactions
INSERT INTO loyalty_transaction (loyalty_id, points_changed, transaction_type, transaction_date, description)
VALUES
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0001'), 200.00, 'EARN','2025-01-01 10:00:00','Earned invoice INV001'),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0002'), -100.00,'REDEEM','2025-01-05 14:00:00','Redeem voucher'),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0003'), 300.50,'EARN','2025-02-01 09:30:00','Earned booking BK001'),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0004'), -50.00,'REDEEM','2025-02-10 16:00:00','Gift redeem'),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0005'), 400.00,'EARN','2025-03-01 11:00:00','Earned invoice INV002'),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0006'), -75.00,'REDEEM','2025-03-05 13:00:00','Used for discount'),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0007'), 120.00,'EARN','2025-04-01 10:00:00','Earned invoice INV003'),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0008'), -30.00,'REDEEM','2025-04-15 15:30:00','Redeemed for gift'),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0009'), 600.00,'EARN','2025-05-01 10:00:00','Earned invoice INV004'),
    (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbb0010'), -90.00,'REDEEM','2025-05-10 18:00:00','Gift voucher used');

-- Insert 10 Birthday Logs
INSERT INTO birthday_notification_log (customer_id, sent_date, status, message_content)
VALUES
    (UUID_TO_BIN('11111111-1111-1111-1111-111111111111'),'2025-01-01 09:00:00','SENT','Chúc mừng sinh nhật A! Ưu đãi 10%.'),
    (UUID_TO_BIN('22222222-2222-2222-2222-222222222222'),'2025-03-12 09:00:00','SENT','Chúc mừng sinh nhật B! Ưu đãi đặc biệt.'),
    (UUID_TO_BIN('33333333-3333-3333-3333-333333333333'),'2025-07-15 09:00:00','SENT','Chúc mừng sinh nhật C! Nhận voucher.'),
    (UUID_TO_BIN('44444444-4444-4444-4444-444444444444'),'2025-11-30 09:00:00','SENT','Chúc mừng sinh nhật D! Ưu đãi Diamond.'),
    (UUID_TO_BIN('55555555-5555-5555-5555-555555555555'),'2025-05-25 09:00:00','SENT','Chúc mừng sinh nhật E! Ưu đãi Spa.'),
    (UUID_TO_BIN('66666666-6666-6666-6666-666666666666'),'2025-09-09 09:00:00','SENT','Chúc mừng sinh nhật F! Giảm giá 15%.'),
    (UUID_TO_BIN('77777777-7777-7777-7777-777777777777'),'2025-12-01 09:00:00','SENT','Chúc mừng sinh nhật G! Ưu đãi Gym.'),
    (UUID_TO_BIN('88888888-8888-8888-8888-888888888888'),'2025-02-20 09:00:00','SENT','Chúc mừng sinh nhật H! Ưu đãi Pool.'),
    (UUID_TO_BIN('99999999-9999-9999-9999-999999999999'),'2025-08-18 09:00:00','SENT','Chúc mừng sinh nhật I! Ưu đãi Karaoke.'),
    (UUID_TO_BIN('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01'),'2025-04-04 09:00:00','SENT','Chúc mừng sinh nhật J! Ưu đãi phòng Suite.');
