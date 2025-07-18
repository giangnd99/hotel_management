-- Tạo database
DROP DATABASE IF EXISTS customer_management;
CREATE DATABASE IF NOT EXISTS customer_management;
USE customer_management;

CREATE TABLE customer
(
    customer_id          BINARY(16) PRIMARY KEY,
    user_id              BINARY(16),
    first_name           VARCHAR(100)                                                     NOT NULL,
    last_name            VARCHAR(100)                                                     NOT NULL,
    address              TEXT,
    image_url             VARCHAR(100),
    date_of_birth        DATE,
    level                ENUM ('NONE', 'BRONZE', 'SILVER', 'GOLD', 'PLATINUM', 'DIAMOND') NOT NULL DEFAULT 'NONE',
    behavior_data        JSON,
    created_at           DATETIME                                                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           DATETIME                                                         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE loyaltyPoint
(
    loyalty_id   BINARY(16) PRIMARY KEY,
    customer_id  BINARY(16)     NOT NULL,
    points       DECIMAL(10, 2) NOT NULL DEFAULT 0.00 CHECK (points >= 0),
    last_updated DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE
);

-- Bảng loyalty_transaction: Lịch sử giao dịch điểm
CREATE TABLE loyalty_transaction
(
    transaction_id   INT AUTO_INCREMENT PRIMARY KEY,
    customer_id      BINARY(16)                        NOT NULL,
    loyalty_id       BINARY(16)                        NOT NULL,
    points_changed   DECIMAL(10, 2)                    NOT NULL,
    transaction_type ENUM ('EARN', 'REDEEM', 'ADJUST') NOT NULL,
    transaction_date DATETIME                          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description      VARCHAR(255),
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE,
    FOREIGN KEY (loyalty_id) REFERENCES loyaltyPoint (loyalty_id) ON DELETE CASCADE
);

-- Bảng birthday_notification_log: Lịch sử gửi email sinh nhật
CREATE TABLE birthday_notification_log
(
    log_id          INT AUTO_INCREMENT PRIMARY KEY,
    customer_id     BINARY(16)              NOT NULL,
    voucher_id      BINARY(16),
    sent_date       DATETIME                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status          ENUM ('SENT', 'FAILED') NOT NULL,
    message_content TEXT,
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE,
);
-- Insert dữ liệu mẫu vào bảng customer
INSERT INTO customer (customer_id, user_id, first_Name, last_Name, address, date_of_birth, accumulated_spending, level,
                      behavior_data)
VALUES (UUID_TO_BIN('9f8d2531-6724-4a2f-b9cc-66e2fa4b9d01'), UUID_TO_BIN('a84f7cda-1b6b-4fa4-9943-6fdc99a6e97d'),
        'Nguyen', 'Van A', '123 Lê Lợi, Phường 1, Quận 1, TP.HCM', '2000-01-01', 123456789.00, 'BRONZE', '{
    "favoriteRoomTypes": "Deluxe",
    "frequentlyUsedServices": "SPA"
  }'),
       (UUID_TO_BIN('b2e5fc6e-69fc-4aeb-b237-65c7277e9b95'), UUID_TO_BIN('84c2e19d-4646-48a5-9e29-b18f3c8b620d'),
        'Tran', 'Thi B', '234 Hai Bà Trưng, Phường 5, Quận 3, TP.HCM', '1995-03-12', 200000000.00, 'SILVER', '{
         "favoriteRoomTypes": "Standard",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('c48f6bc6-65d7-4788-90dc-6f28472866b3'), UUID_TO_BIN('7b8490fa-0fd3-472b-a0f5-09393b172f33'), 'Le',
        'Van C', '345 Nguyễn Huệ, Phường Bến Nghé, Quận 1, TP.HCM', '1992-08-21', 500000000.00, 'GOLD', '{
         "favoriteRoomTypes": "Suite",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('8e3b9de7-6f1d-4b6c-a473-5ab53a289402'), UUID_TO_BIN('42e676b7-88f9-44b3-a022-2ea185f139b2'),
        'Pham', 'Minh D', '456 Pasteur, Phường 6, Quận 10, TP.HCM', '1988-11-02', 1000000000.00, 'PLATINUM', '{
         "favoriteRoomTypes": "Deluxe",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('31f7f8cf-d238-4a2d-9c60-cb8b194da69f'), UUID_TO_BIN('1936c09b-7168-42eb-912f-e7b511b5d95b'),
        'Hoang', 'Thi E', '567 Trần Hưng Đạo, Phường 2, Quận 5, TP.HCM', '1990-05-17', 75000000.00, 'BRONZE', '{
         "favoriteRoomTypes": "Standard",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('f8b40455-0472-4916-a21e-c48d8f36d73f'), UUID_TO_BIN('ff6c2929-786b-4ddf-a9e3-5181dd4f7904'), 'Bui',
        'Thi F', '678 Nguyễn Thị Minh Khai, Phường Đa Kao, Quận 1, TP.HCM', '1993-09-13', 350000000.00, 'SILVER', '{
         "favoriteRoomTypes": "Deluxe",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('5e7a6e0f-3fc3-4ad4-b6b0-bd6e75818d4c'), UUID_TO_BIN('9a8b6b6f-06d9-4f19-8a8f-2cda8fbb7a52'),
        'Dang', 'Minh G', '789 Cách Mạng Tháng 8, Phường 10, Quận 3, TP.HCM', '1987-12-25', 640000000.00, 'GOLD', '{
         "favoriteRoomTypes": "Suite",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('02a80408-d8c7-4018-81e6-f1a6bcb12cfa'), UUID_TO_BIN('d47e6cf6-bd68-4ea6-91f6-f6ae5301aeee'), 'Vo',
        'Van H', '890 Lý Tự Trọng, Phường Bến Thành, Quận 1, TP.HCM', '1991-04-19', 540000000.00, 'SILVER', '{
         "favoriteRoomTypes": "Standard",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('a6d2d1c6-6f3a-41a3-a7fa-27bc8c8b1f13'), UUID_TO_BIN('33fa7558-5e65-4134-87d7-c18f539e27ab'), 'Ngo',
        'Thanh I', '901 Nguyễn Trãi, Phường 7, Quận 5, TP.HCM', '1994-07-30', 420000000.00, 'GOLD', '{
         "favoriteRoomTypes": "Suite",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('abebc77e-f871-44e2-89de-bbd4cb470123'), UUID_TO_BIN('6f71341a-1850-4a85-bb6e-8f4875db07cd'),
        'Duong', 'Thi J', '123A Võ Văn Tần, Phường 6, Quận 3, TP.HCM', '1996-02-11', 150000000.00, 'BRONZE', '{
         "favoriteRoomTypes": "Standard",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('6bb9f0e7-c6a6-45ae-a9b6-e50b45f30d1d'), UUID_TO_BIN('41a9ec17-05c7-4a64-a6a5-84f1c3a9f292'), 'Luu',
        'Van K', '234B Lê Văn Sỹ, Phường 1, Quận Tân Bình, TP.HCM', '1993-03-03', 860000000.00, 'PLATINUM', '{
         "favoriteRoomTypes": "Deluxe",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('28b08311-9b88-4e5b-bab8-9d05b7f08bb6'), UUID_TO_BIN('dbd1a1d4-0e52-4e0d-a123-7b1b8c176b3e'), 'Mai',
        'Ngoc L', '789A Cộng Hòa, Phường 13, Quận Tân Bình, TP.HCM', '1989-01-08', 370000000.00, 'SILVER', '{
         "favoriteRoomTypes": "Suite",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('701a0227-31d4-4666-9687-34605a1f6c6f'), UUID_TO_BIN('73e62b85-2bb8-437f-b61f-5bff98c1bba7'),
        'Dang', 'Thi M', '123D Trường Chinh, Phường 14, Quận Tân Bình, TP.HCM', '1986-06-06', 290000000.00, 'SILVER', '{
         "favoriteRoomTypes": "Standard",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('fcfc7593-6ee2-4dc3-aef8-071b69eb3e41'), UUID_TO_BIN('f137b1d5-7b9e-4cf1-baf8-0585f9f11193'),
        'Trinh', 'Thanh N', '567B Phạm Văn Đồng, Phường Linh Đông, TP.Thủ Đức, TP.HCM', '1992-10-10', 450000000.00,
        'GOLD', '{
         "favoriteRoomTypes": "Suite",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('c5bb4df3-97b1-47df-95e7-1c2b3433cb92'), UUID_TO_BIN('3729a2d2-9468-491c-80ed-4ae41d7c6601'),
        'Pham', 'Van O', '101A Điện Biên Phủ, Phường 25, Quận Bình Thạnh, TP.HCM', '1997-08-08', 670000000.00,
        'PLATINUM', '{
         "favoriteRoomTypes": "Deluxe",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('1b8c12c5-7d46-4eb6-9db8-f7d16a0fc9d6'), UUID_TO_BIN('9649b260-00b1-4c1e-b844-95f6029fbc6c'), 'Vu',
        'Minh P', '123C Nguyễn Oanh, Phường 17, Quận Gò Vấp, TP.HCM', '1990-09-09', 330000000.00, 'GOLD', '{
         "favoriteRoomTypes": "Standard",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('fe9d1e57-f297-4559-8720-fba394d71f1a'), UUID_TO_BIN('cc7d1a8e-9945-4089-b8fc-8c1f5b1f8a37'),
        'Nguyen', 'Thanh Q', '789C Quang Trung, Phường 11, Quận Gò Vấp, TP.HCM', '1984-04-04', 780000000.00, 'PLATINUM',
        '{
          "favoriteRoomTypes": "Suite",
          "frequentlyUsedServices": "SPA"
        }'),
       (UUID_TO_BIN('3702d19c-746b-49aa-b01a-cb59c6ac66a2'), UUID_TO_BIN('fa2102e1-2459-41c4-a95a-2c8c0f07d1d0'), 'Ha',
        'Van R', '123F Phan Văn Trị, Phường 10, Quận Gò Vấp, TP.HCM', '1998-12-12', 390000000.00, 'SILVER', '{
         "favoriteRoomTypes": "Deluxe",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('9ea47fd1-3d59-4f9b-bcf7-89f7c3d3d3f7'), UUID_TO_BIN('8c19a18a-0d1a-4629-84e0-faa1a6e3eccc'), 'Le',
        'Thi S', '456A Nguyễn Kiệm, Phường 3, Quận Phú Nhuận, TP.HCM', '1995-11-11', 410000000.00, 'GOLD', '{
         "favoriteRoomTypes": "Suite",
         "frequentlyUsedServices": "SPA"
       }'),
       (UUID_TO_BIN('c9f1a82e-8f89-4d10-b6f2-dc6f66a3e2a5'), UUID_TO_BIN('6ae5f71f-040c-4ed6-83d9-b78a27c156e2'),
        'Truong', 'Minh T', '789E Hoàng Văn Thụ, Phường 9, Quận Phú Nhuận, TP.HCM', '1982-07-07', 880000000.00,
        'PLATINUM', '{
         "favoriteRoomTypes": "Deluxe",
         "frequentlyUsedServices": "SPA"
       }');

-- loyaltyPoint
INSERT INTO loyaltyPoint (loyalty_id, customer_id, points)
VALUES (UUID_TO_BIN('468e28fc-4c19-42ae-8457-0492f7000018'), UUID_TO_BIN('9f8d2531-6724-4a2f-b9cc-66e2fa4b9d01'),
        24192.14),
       (UUID_TO_BIN('4efdc1a2-8c92-44f1-810d-3c03a85094aa'), UUID_TO_BIN('b2e5fc6e-69fc-4aeb-b237-65c7277e9b95'),
        36382.07),
       (UUID_TO_BIN('5e66ab27-65a1-4fff-a42b-307d815312fa'), UUID_TO_BIN('c48f6bc6-65d7-4788-90dc-6f28472866b3'),
        73213.10),
       (UUID_TO_BIN('92fac134-327e-46d7-a820-5f9c95511ccb'), UUID_TO_BIN('8e3b9de7-6f1d-4b6c-a473-5ab53a289402'),
        72027.14),
       (UUID_TO_BIN('2381e1d2-4176-4437-a8ec-65db9818e5a2'), UUID_TO_BIN('31f7f8cf-d238-4a2d-9c60-cb8b194da69f'),
        42048.15),
       (UUID_TO_BIN('e8860c04-efde-41b7-99e0-1a184c6d3e07'), UUID_TO_BIN('f8b40455-0472-4916-a21e-c48d8f36d73f'),
        56476.91),
       (UUID_TO_BIN('b13b1f86-59d5-465f-8b9f-e62328b79e42'), UUID_TO_BIN('5e7a6e0f-3fc3-4ad4-b6b0-bd6e75818d4c'),
        96411.89),
       (UUID_TO_BIN('96964e9e-24c4-4c18-aaa5-961b74db3538'), UUID_TO_BIN('02a80408-d8c7-4018-81e6-f1a6bcb12cfa'),
        77832.82),
       (UUID_TO_BIN('0d3ae12b-dcb3-4b7e-b994-8035d18ff358'), UUID_TO_BIN('a6d2d1c6-6f3a-41a3-a7fa-27bc8c8b1f13'),
        34915.13),
       (UUID_TO_BIN('c1f19628-97ee-4a88-a700-c14e69bfa23d'), UUID_TO_BIN('abebc77e-f871-44e2-89de-bbd4cb470123'),
        69746.79),
       (UUID_TO_BIN('81d9f392-8679-47cd-90e3-9ed867d2a30f'), UUID_TO_BIN('6bb9f0e7-c6a6-45ae-a9b6-e50b45f30d1d'),
        52723.25),
       (UUID_TO_BIN('c4301107-64b3-4c67-a60c-df28bb3a4f32'), UUID_TO_BIN('28b08311-9b88-4e5b-bab8-9d05b7f08bb6'),
        27176.60),
       (UUID_TO_BIN('0200833e-61b9-43df-aafe-3bb1fc9e8a99'), UUID_TO_BIN('701a0227-31d4-4666-9687-34605a1f6c6f'),
        66552.12),
       (UUID_TO_BIN('bb2dcf6e-2578-407c-8078-7fbc27ef5c90'), UUID_TO_BIN('fcfc7593-6ee2-4dc3-aef8-071b69eb3e41'),
        21891.98),
       (UUID_TO_BIN('670ba4c3-8e7b-4a29-a073-90e9b366b9d8'), UUID_TO_BIN('c5bb4df3-97b1-47df-95e7-1c2b3433cb92'),
        30291.37),
       (UUID_TO_BIN('be64f232-832b-40ff-9cc1-111ea67e2210'), UUID_TO_BIN('1b8c12c5-7d46-4eb6-9db8-f7d16a0fc9d6'),
        95681.91),
       (UUID_TO_BIN('89f9ff86-67e5-49ae-b26c-7bc6182fcaa0'), UUID_TO_BIN('fe9d1e57-f297-4559-8720-fba394d71f1a'),
        80921.26),
       (UUID_TO_BIN('1e5b7870-bf32-4a14-b5c5-c2d8bb00edcf'), UUID_TO_BIN('3702d19c-746b-49aa-b01a-cb59c6ac66a2'),
        74086.84),
       (UUID_TO_BIN('d93d8a00-8c4d-4564-a121-1e9f5a0a0b8b'), UUID_TO_BIN('9ea47fd1-3d59-4f9b-bcf7-89f7c3d3d3f7'),
        78916.14),
       (UUID_TO_BIN('f10088c3-e7be-4b3f-91e2-0f7c6d9e0242'), UUID_TO_BIN('c9f1a82e-8f89-4d10-b6f2-dc6f66a3e2a5'),
        75818.30);

-- voucher
INSERT INTO voucher (voucher_id, customer_id, promotion_id, code, discount_percentage, discount_amount, issue_date,
                     expiry_date, status)
VALUES (UUID_TO_BIN('55555555-5555-5555-5555-555555555555'), UUID_TO_BIN('c9f1a82e-8f89-4d10-b6f2-dc6f66a3e2a5'), NULL,
        'BDAY2025-JD', 10.00, 0.00, '2025-01-01', '2025-01-31', 'active'),
       (UUID_TO_BIN('66666666-6666-6666-6666-666666666666'), UUID_TO_BIN('9ea47fd1-3d59-4f9b-bcf7-89f7c3d3d3f7'), NULL,
        'BDAY2025-JR', 10.00, 0.00, '2025-05-15', '2025-06-14', 'active');

-- loyalty_transaction
INSERT INTO loyalty_transaction (customer_id, loyalty_id, points_changed, transaction_type, transaction_date,
                                 description)
VALUES (UUID_TO_BIN('c9f1a82e-8f89-4d10-b6f2-dc6f66a3e2a5'), UUID_TO_BIN('f10088c3-e7be-4b3f-91e2-0f7c6d9e0242'),
        25000.00, 'EARN', '2025-05-16 10:00:00', 'Earned from invoice INV001'),
       (UUID_TO_BIN('9ea47fd1-3d59-4f9b-bcf7-89f7c3d3d3f7'), UUID_TO_BIN('d93d8a00-8c4d-4564-a121-1e9f5a0a0b8b'),
        -5000.00, 'REDEEM', '2025-05-16 15:00:00', 'Redeemed for promotion LOYALTY500');

-- birthday_notification_log
INSERT INTO birthday_notification_log (customer_id, sent_date, status, message_content)
VALUES (UUID_TO_BIN('c9f1a82e-8f89-4d10-b6f2-dc6f66a3e2a5'), UUID_TO_BIN('55555555-5555-5555-5555-555555555555'),
        '2025-01-01 09:00:00', 'SENT', 'Chúc mừng sinh nhật John Doe! Dùng mã BDAY2025-JD để được giảm 10%.'),
       (UUID_TO_BIN('9ea47fd1-3d59-4f9b-bcf7-89f7c3d3d3f7'), UUID_TO_BIN('66666666-6666-6666-6666-666666666666'),
        '2025-05-15 09:00:00', 'SENT', 'Chúc mừng sinh nhật Jane Roe! Dùng mã BDAY2025-JR để được giảm 10%.');
