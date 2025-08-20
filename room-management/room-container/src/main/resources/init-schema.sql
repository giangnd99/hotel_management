CREATE SCHEMA IF NOT EXISTS room_management;
SET search_path TO room_management, public;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- Thêm các lệnh DROP TABLE IF EXISTS để đảm bảo tính idempotent
DROP TABLE IF EXISTS room_type_furniture CASCADE;
DROP TABLE IF EXISTS furniture CASCADE;
DROP TABLE IF EXISTS room_maintenance CASCADE;
DROP TABLE IF EXISTS room_cleaning CASCADE;
DROP TABLE IF EXISTS room_services CASCADE;
DROP TABLE IF EXISTS check_ins CASCADE;
DROP TABLE IF EXISTS rooms CASCADE;
DROP TABLE IF EXISTS room_types CASCADE;
DROP TABLE IF EXISTS maintenance_types CASCADE;
DROP TABLE IF EXISTS guests CASCADE;
-- Room Status Enum
DROP TYPE IF EXISTS room_status CASCADE;
CREATE TYPE room_status AS ENUM (
    'VACANT',      -- Trống
    'BOOKED',      -- Đã đặt
    'CHECKED_IN',  -- Đã check-in
    'MAINTENANCE', -- Bảo trì
    'CLEANING',    -- Đang dọn dẹp
    'CHECKED_OUT'  -- Đã check-out
);

-- Maintenance Status Enum
DROP TYPE IF EXISTS maintenance_status CASCADE;
CREATE TYPE maintenance_status AS ENUM (
    'REQUESTED',   -- Yêu cầu
    'ASSIGNED',    -- Đã phân công
    'IN_PROGRESS', -- Đang thực hiện
    'COMPLETED',   -- Hoàn thành
    'CANCELLED'    -- Đã hủy
);

-- Maintenance Priority Enum
DROP TYPE IF EXISTS maintenance_priority CASCADE;
CREATE TYPE maintenance_priority AS ENUM (
    'LOW',         -- Thấp
    'MEDIUM',      -- Trung bình
    'HIGH',        -- Cao
    'URGENT',      -- Khẩn cấp
    'CRITICAL'     -- Quan trọng
);

-- Issue Type Enum
DROP TYPE IF EXISTS issue_type CASCADE;
CREATE TYPE issue_type AS ENUM (
    'PLUMBING',    -- Hệ thống nước
    'ELECTRICAL',  -- Điện
    'HVAC',        -- Điều hòa
    'STRUCTURAL',  -- Kết cấu
    'APPLIANCE',   -- Thiết bị
    'OTHER'        -- Khác
);

-- Cleaning Type Enum
DROP TYPE IF EXISTS cleaning_type CASCADE;
CREATE TYPE cleaning_type AS ENUM (
    'DAILY',           -- Hàng ngày
    'DEEP_CLEANING',   -- Dọn dẹp sâu
    'POST_CHECKOUT',   -- Sau khi check-out
    'MAINTENANCE'      -- Bảo trì
);

-- Cleaning Priority Enum
DROP TYPE IF EXISTS cleaning_priority CASCADE;
CREATE TYPE cleaning_priority AS ENUM (
    'LOW',     -- Thấp
    'MEDIUM',  -- Trung bình
    'HIGH',    -- Cao
    'URGENT'   -- Khẩn cấp
);

-- Cleaning Status Enum
DROP TYPE IF EXISTS cleaning_status CASCADE;
CREATE TYPE cleaning_status AS ENUM (
    'REQUESTED',   -- Yêu cầu
    'ASSIGNED',    -- Đã phân công
    'IN_PROGRESS', -- Đang thực hiện
    'COMPLETED',   -- Hoàn thành
    'CANCELLED'    -- Đã hủy
);

-- Service Status Enum
DROP TYPE IF EXISTS service_status CASCADE;
CREATE TYPE service_status AS ENUM (
    'REQUESTED',   -- Yêu cầu
    'IN_PROGRESS', -- Đang thực hiện
    'COMPLETED',   -- Hoàn thành
    'CANCELLED'    -- Đã hủy
);

-- Guest Status Enum
DROP TYPE IF EXISTS guest_status CASCADE;
CREATE TYPE guest_status AS ENUM (
    'ACTIVE',      -- Hoạt động
    'INACTIVE',    -- Không hoạt động
    'BLACKLISTED'  -- Danh sách đen
);

-- Check-in Status Enum
DROP TYPE IF EXISTS check_in_status CASCADE;
CREATE TYPE check_in_status AS ENUM (
    'PENDING',         -- Chờ xử lý
    'CHECKED_IN',      -- Đã check-in
    'EXTENDED',        -- Gia hạn
    'CHANGED_ROOM',    -- Đổi phòng
    'CHECKED_OUT',     -- Đã check-out
    'CANCELLED'        -- Đã hủy
);

CREATE TABLE room_types (
    room_type_id SERIAL PRIMARY KEY,
    type_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    base_price DECIMAL(10,2) NOT NULL CHECK (base_price > 0),
    max_occupancy INTEGER NOT NULL CHECK (max_occupancy > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Furniture (Nội thất)
CREATE TABLE furniture (
    furniture_id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Room Type Furniture (Yêu cầu nội thất cho loại phòng)
CREATE TABLE room_type_furniture (
    room_type_furniture_id SERIAL PRIMARY KEY,
    room_type_id INTEGER NOT NULL REFERENCES room_types(room_type_id) ON DELETE CASCADE,
    furniture_id INTEGER NOT NULL REFERENCES furniture(furniture_id) ON DELETE CASCADE,
    required_quantity INTEGER NOT NULL CHECK (required_quantity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(room_type_id, furniture_id)
);

-- Bảng Rooms (Phòng)
CREATE TABLE rooms (
    room_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    room_number VARCHAR(10) NOT NULL UNIQUE,
    floor INTEGER NOT NULL CHECK (floor > 0),
    room_type_id INTEGER NOT NULL REFERENCES room_types(room_type_id),
    room_status room_status NOT NULL DEFAULT 'VACANT',
    area VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Maintenance Types (Loại bảo trì)
CREATE TABLE maintenance_types (
    maintenance_type_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Room Maintenance (Bảo trì phòng)
CREATE TABLE room_maintenance (
    maintenance_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    room_id UUID NOT NULL REFERENCES rooms(room_id) ON DELETE CASCADE,
    room_number VARCHAR(10) NOT NULL,
    issue_type issue_type NOT NULL,
    priority maintenance_priority NOT NULL DEFAULT 'MEDIUM',
    status maintenance_status NOT NULL DEFAULT 'REQUESTED',
    description TEXT,
    notes TEXT,
    requested_by VARCHAR(100),
    assigned_to VARCHAR(100),
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    scheduled_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    estimated_cost DECIMAL(10,2) CHECK (estimated_cost >= 0),
    actual_cost DECIMAL(10,2) CHECK (actual_cost >= 0),
    is_urgent BOOLEAN DEFAULT FALSE,
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Room Cleaning (Dọn dẹp phòng)
CREATE TABLE room_cleaning (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    room_id UUID NOT NULL REFERENCES rooms(room_id) ON DELETE CASCADE,
    room_number VARCHAR(10) NOT NULL,
    cleaning_type cleaning_type NOT NULL,
    priority cleaning_priority NOT NULL DEFAULT 'MEDIUM',
    status cleaning_status NOT NULL DEFAULT 'REQUESTED',
    description TEXT,
    notes TEXT,
    requested_by VARCHAR(100),
    assigned_to VARCHAR(100),
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    scheduled_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    is_urgent BOOLEAN DEFAULT FALSE,
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Room Services (Dịch vụ phòng)
CREATE TABLE room_services (
    service_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    room_number VARCHAR(10) NOT NULL,
    guest_id UUID,
    guest_name VARCHAR(200),
    service_type VARCHAR(100) NOT NULL,
    service_name VARCHAR(200) NOT NULL,
    description TEXT,
    quantity INTEGER DEFAULT 1 CHECK (quantity > 0),
    unit_price DECIMAL(10,2) NOT NULL CHECK (unit_price >= 0),
    total_price DECIMAL(10,2) NOT NULL CHECK (total_price >= 0),
    status service_status NOT NULL DEFAULT 'REQUESTED',
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    requested_by VARCHAR(100),
    completed_by VARCHAR(100),
    notes TEXT,
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Guests (Khách hàng)
CREATE TABLE guests (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(255) UNIQUE,
    id_number VARCHAR(50),
    id_type VARCHAR(50),
    nationality VARCHAR(100),
    address TEXT,
    date_of_birth DATE,
    gender VARCHAR(20),
    special_requests TEXT,
    status guest_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Check-ins (Check-in)
CREATE TABLE check_ins (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    booking_id UUID,
    guest_id UUID NOT NULL REFERENCES guests(id),
    room_id UUID NOT NULL REFERENCES rooms(room_id),
    room_number VARCHAR(10) NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE,
    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP,
    number_of_guests INTEGER DEFAULT 1 CHECK (number_of_guests > 0),
    special_requests TEXT,
    status check_in_status NOT NULL DEFAULT 'PENDING',
    checked_in_by VARCHAR(100),
    checked_out_by VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Room indexes
CREATE INDEX idx_rooms_room_number ON rooms(room_number);
CREATE INDEX idx_rooms_floor ON rooms(floor);
CREATE INDEX idx_rooms_room_type_id ON rooms(room_type_id);
CREATE INDEX idx_rooms_room_status ON rooms(room_status);
CREATE INDEX idx_rooms_created_at ON rooms(created_at);

-- Room Type indexes
CREATE INDEX idx_room_types_type_name ON room_types(type_name);
CREATE INDEX idx_room_types_base_price ON room_types(base_price);

-- Furniture indexes
CREATE INDEX idx_furniture_name ON furniture(name);
CREATE INDEX idx_furniture_price ON furniture(price);

-- Room Type Furniture indexes
CREATE INDEX idx_room_type_furniture_room_type_id ON room_type_furniture(room_type_id);
CREATE INDEX idx_room_type_furniture_furniture_id ON room_type_furniture(furniture_id);

-- Maintenance indexes
CREATE INDEX idx_room_maintenance_room_id ON room_maintenance(room_id);
CREATE INDEX idx_room_maintenance_room_number ON room_maintenance(room_number);
CREATE INDEX idx_room_maintenance_status ON room_maintenance(status);
CREATE INDEX idx_room_maintenance_priority ON room_maintenance(priority);
CREATE INDEX idx_room_maintenance_scheduled_at ON room_maintenance(scheduled_at);
CREATE INDEX idx_room_maintenance_assigned_to ON room_maintenance(assigned_to);

-- Cleaning indexes
CREATE INDEX idx_room_cleaning_room_id ON room_cleaning(room_id);
CREATE INDEX idx_room_cleaning_room_number ON room_cleaning(room_number);
CREATE INDEX idx_room_cleaning_status ON room_cleaning(status);
CREATE INDEX idx_room_cleaning_priority ON room_cleaning(priority);
CREATE INDEX idx_room_cleaning_scheduled_at ON room_cleaning(scheduled_at);

-- Service indexes
CREATE INDEX idx_room_services_room_number ON room_services(room_number);
CREATE INDEX idx_room_services_guest_id ON room_services(guest_id);
CREATE INDEX idx_room_services_status ON room_services(status);
CREATE INDEX idx_room_services_requested_at ON room_services(requested_at);

-- Guest indexes
CREATE INDEX idx_guests_full_name ON guests(full_name);
CREATE INDEX idx_guests_phone ON guests(phone);
CREATE INDEX idx_guests_email ON guests(email);
CREATE INDEX idx_guests_id_number ON guests(id_number);
CREATE INDEX idx_guests_status ON guests(status);

-- Check-in indexes
CREATE INDEX idx_check_ins_guest_id ON check_ins(guest_id);
CREATE INDEX idx_check_ins_room_id ON check_ins(room_id);
CREATE INDEX idx_check_ins_room_number ON check_ins(room_number);
CREATE INDEX idx_check_ins_status ON check_ins(status);
CREATE INDEX idx_check_ins_check_in_date ON check_ins(check_in_date);
CREATE INDEX idx_check_ins_check_out_date ON check_ins(check_out_date);
