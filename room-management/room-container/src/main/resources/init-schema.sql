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