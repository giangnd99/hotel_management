DROP SCHEMA IF EXISTS room_management CASCADE;

CREATE SCHEMA room_management;
-- Tạo extension để hỗ trợ UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create table for MaintenanceTypeEntity
CREATE TABLE room_management.maintenance_type_entity
(
    maintenance_type_id INT PRIMARY KEY,
    name                VARCHAR(255)
);

-- Create table for FurnitureEntity
CREATE TABLE room_management.furniture_entity
(
    furniture_id INT PRIMARY KEY,
    name         VARCHAR(255),
    price        DECIMAL(19, 2)
);

-- Create table for RoomEntity
CREATE TABLE room_management.room_entity
(
    room_id      UUID PRIMARY KEY,
    room_number  VARCHAR(255),
    floor        INT,
    area         VARCHAR(255),
    room_status  VARCHAR(255)
);

-- Create table for RoomTypeEntity
CREATE TABLE room_management.room_type_entity
(
    room_type_id  INT PRIMARY KEY,
    room_id       UUID,
    type_name     VARCHAR(255),
    description   TEXT,
    base_price    DECIMAL(19, 2),
    max_occupancy INT,
    FOREIGN KEY (room_id) REFERENCES room_management.room_entity (room_id)
);

-- Create table for RoomTypeFurnitureEntity
CREATE TABLE room_management.room_type_furniture_entity
(
    room_type_furniture_id INT PRIMARY KEY,
    room_type_id           INT,
    furniture_id           INT,
    require_quantity       INT,
    FOREIGN KEY (room_type_id) REFERENCES room_management.room_type_entity (room_type_id),
    FOREIGN KEY (furniture_id) REFERENCES room_management.furniture_entity (furniture_id)
);

-- Create table for RoomMaintenanceEntity
CREATE TABLE room_management.room_maintenance_entity
(
    maintenance_id      INT PRIMARY KEY,
    room_id             UUID,
    staff_id            VARCHAR(255),
    maintenance_date    TIMESTAMP,
    maintenance_type_id INT,
    description         TEXT,
    status              VARCHAR(255),
    FOREIGN KEY (room_id) REFERENCES room_management.room_entity (room_id),
    FOREIGN KEY (maintenance_type_id) REFERENCES room_management.maintenance_type_entity (maintenance_type_id)
);

-- Create indexes for better query performance
CREATE INDEX idx_room_type ON room_management.room_type_entity (room_id);
CREATE INDEX idx_room_status ON room_management.room_entity (room_status);
CREATE INDEX idx_maintenance_room ON room_management.room_maintenance_entity (room_id);
CREATE INDEX idx_maintenance_type ON room_management.room_maintenance_entity (maintenance_type_id);
CREATE INDEX idx_room_type_furniture ON room_management.room_type_furniture_entity (room_type_id, furniture_id);

-- Bật pgcrypto (chỉ cần 1 lần trong DB)
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Đặt DEFAULT cho room_id để tự sinh UUID (không cần truyền room_id khi INSERT)
ALTER TABLE room_management.room_entity
    ALTER COLUMN room_id SET DEFAULT gen_random_uuid();

-- Maintenance Types (10 bản ghi)
INSERT INTO room_management.maintenance_type_entity (maintenance_type_id, name)
VALUES
    (1,'Điện'),(2,'Nước'),(3,'Điều hòa'),(4,'Internet'),(5,'Sơn sửa'),
    (6,'Vệ sinh'),(7,'Nội thất'),(8,'Khóa cửa'),(9,'Chiếu sáng'),(10,'Khác')
ON CONFLICT DO NOTHING;

-- Furniture (10 bản ghi)
INSERT INTO room_management.furniture_entity (furniture_id, name, price)
VALUES
    (1,'Giường',5000000),(2,'Tủ quần áo',3000000),(3,'Bàn làm việc',1500000),
    (4,'Ghế Sofa',4000000),(5,'TV',6000000),(6,'Bàn trang điểm',2000000),
    (7,'Tủ lạnh',4500000),(8,'Máy lạnh',10000000),(9,'Két sắt',2500000),
    (10,'Đèn ngủ',500000)
ON CONFLICT DO NOTHING;

-- Rooms (10 bản ghi) — KHÔNG truyền room_id, DB tự sinh
INSERT INTO room_management.room_entity (room_number, floor, area, room_status)
VALUES
    ('101',1,'25m2','VACANT'),
    ('102',1,'28m2','BOOKED'),
    ('103',1,'22m2','VACANT'),
    ('201',2,'30m2','MAINTENANCE'),
    ('202',2,'26m2','BOOKED'),
    ('203',2,'24m2','VACANT'),
    ('301',3,'35m2','CHECKED_IN'),
    ('302',3,'27m2','BOOKED'),
    ('303',3,'29m2','CLEANING'),
    ('401',4,'40m2','CHECKED_OUT')
ON CONFLICT DO NOTHING;

-- Room Types (10 bản ghi)
INSERT INTO room_management.room_type_entity
(room_type_id, room_id, type_name, description, base_price, max_occupancy)
VALUES
    (1, (SELECT room_id FROM room_management.room_entity WHERE room_number='101'), 'Standard',  'Phòng tiêu chuẩn tầng 1',  500000, 2),
    (2, (SELECT room_id FROM room_management.room_entity WHERE room_number='102'), 'Deluxe',    'Phòng deluxe tầng 1',      800000, 2),
    (3, (SELECT room_id FROM room_management.room_entity WHERE room_number='103'), 'Standard',  'Phòng tiêu chuẩn nhỏ',      450000, 2),
    (4, (SELECT room_id FROM room_management.room_entity WHERE room_number='201'), 'Suite',     'Phòng suite tầng 2',       1200000, 4),
    (5, (SELECT room_id FROM room_management.room_entity WHERE room_number='202'), 'Deluxe',    'Phòng deluxe view city',    900000, 2),
    (6, (SELECT room_id FROM room_management.room_entity WHERE room_number='203'), 'Standard',  'Phòng tiêu chuẩn tầng 2',   500000, 2),
    (7, (SELECT room_id FROM room_management.room_entity WHERE room_number='301'), 'Executive', 'Phòng executive',          1500000, 4),
    (8, (SELECT room_id FROM room_management.room_entity WHERE room_number='302'), 'Deluxe',    'Phòng deluxe tầng 3',       850000, 2),
    (9, (SELECT room_id FROM room_management.room_entity WHERE room_number='303'), 'Standard',  'Phòng tiêu chuẩn tầng 3',   550000, 2),
    (10,(SELECT room_id FROM room_management.room_entity WHERE room_number='401'), 'Suite',     'Phòng suite rộng',         2000000, 5)
ON CONFLICT DO NOTHING;

-- RoomTypeFurniture (10 bản ghi)
INSERT INTO room_management.room_type_furniture_entity
(room_type_furniture_id, room_type_id, furniture_id, require_quantity)
VALUES
    (1,1,1,1),(2,1,2,1),(3,2,1,1),(4,2,5,1),(5,3,1,1),
    (6,4,1,2),(7,4,8,2),(8,5,1,1),(9,6,1,1),(10,7,1,2)
ON CONFLICT DO NOTHING;

-- Room Maintenance (10 bản ghi)
INSERT INTO room_management.room_maintenance_entity
(maintenance_id, room_id, staff_id, maintenance_date, maintenance_type_id, description, status)
VALUES
    (1, (SELECT room_id FROM room_management.room_entity WHERE room_number='101'), 'staff01','2025-01-05 10:00:00',1,'Sửa điện','DONE'),
    (2, (SELECT room_id FROM room_management.room_entity WHERE room_number='102'), 'staff02','2025-01-07 15:00:00',2,'Ống nước rò','DONE'),
    (3, (SELECT room_id FROM room_management.room_entity WHERE room_number='103'), 'staff03','2025-01-10 11:30:00',3,'Máy lạnh bảo dưỡng','PENDING'),
    (4, (SELECT room_id FROM room_management.room_entity WHERE room_number='201'), 'staff04','2025-02-01 09:00:00',4,'Kiểm tra internet','DONE'),
    (5, (SELECT room_id FROM room_management.room_entity WHERE room_number='202'), 'staff05','2025-02-15 13:00:00',5,'Sơn lại tường','DONE'),
    (6, (SELECT room_id FROM room_management.room_entity WHERE room_number='203'), 'staff06','2025-02-20 08:00:00',6,'Vệ sinh toàn bộ phòng','PENDING'),
    (7, (SELECT room_id FROM room_management.room_entity WHERE room_number='301'), 'staff07','2025-03-05 14:00:00',7,'Thay sofa mới','DONE'),
    (8, (SELECT room_id FROM room_management.room_entity WHERE room_number='302'), 'staff08','2025-03-10 10:15:00',8,'Sửa khóa cửa','DONE'),
    (9, (SELECT room_id FROM room_management.room_entity WHERE room_number='303'), 'staff09','2025-03-20 16:30:00',9,'Đèn hỏng','PENDING'),
    (10,(SELECT room_id FROM room_management.room_entity WHERE room_number='401'), 'staff10','2025-04-01 09:45:00',10,'Khác','DONE')
ON CONFLICT DO NOTHING;
