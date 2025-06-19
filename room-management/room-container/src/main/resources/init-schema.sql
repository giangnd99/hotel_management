-- Create table for MaintenanceTypeEntity
CREATE TABLE maintenance_type_entity (
    maintenance_type_id INT PRIMARY KEY,
    name VARCHAR(255)
);

-- Create table for RoomStatusEntity
CREATE TABLE room_status_entity (
    status_id INT PRIMARY KEY,
    status_name VARCHAR(255)
);

-- Create table for RoomTypeEntity
CREATE TABLE room_type_entity (
    room_type_id INT PRIMARY KEY,
    type_name VARCHAR(255),
    description TEXT,
    base_price DECIMAL(19,2),
    max_occupancy INT
);

-- Create table for FurnitureEntity
CREATE TABLE furniture_entity (
    furniture_id INT PRIMARY KEY,
    inventory_item_id VARCHAR(255)
);

-- Create table for RoomTypeFurnitureEntity
CREATE TABLE room_type_furniture_entity (
    room_type_id INT,
    furniture_id INT,
    require_quantity INT,
    PRIMARY KEY (room_type_id, furniture_id),
    FOREIGN KEY (room_type_id) REFERENCES room_type_entity(room_type_id),
    FOREIGN KEY (furniture_id) REFERENCES furniture_entity(furniture_id)
);

-- Create table for RoomEntity
CREATE TABLE room_entity (
    room_id INT PRIMARY KEY,
    room_type_id INT,
    status_id INT,
    room_number VARCHAR(255),
    floor INT,
    FOREIGN KEY (room_type_id) REFERENCES room_type_entity(room_type_id),
    FOREIGN KEY (status_id) REFERENCES room_status_entity(status_id)
);

-- Create table for RoomMaintenanceEntity
CREATE TABLE room_maintenance_entity (
    maintenance_id INT PRIMARY KEY,
    room_id INT,
    staff_id VARCHAR(255),
    maintenance_date TIMESTAMP,
    maintenance_type_id INT,
    description TEXT,
    status VARCHAR(255),
    FOREIGN KEY (room_id) REFERENCES room_entity(room_id),
    FOREIGN KEY (maintenance_type_id) REFERENCES maintenance_type_entity(maintenance_type_id)
);

-- Create indexes for better query performance
CREATE INDEX idx_room_type ON room_entity(room_type_id);
CREATE INDEX idx_room_status ON room_entity(status_id);
CREATE INDEX idx_maintenance_room ON room_maintenance_entity(room_id);
CREATE INDEX idx_maintenance_type ON room_maintenance_entity(maintenance_type_id);
CREATE INDEX idx_room_type_furniture ON room_type_furniture_entity(room_type_id, furniture_id);