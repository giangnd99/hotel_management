-- Thiết lập schema
CREATE SCHEMA IF NOT EXISTS room_management;
SET search_path TO room_management, public;

-- Tạo extension để hỗ trợ UUID và tìm kiếm tương tự
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- =====================================================================================================================
-- Bổ sung các lệnh DROP TABLE để đảm bảo tính idempotent
-- =====================================================================================================================
DROP TABLE IF EXISTS room_type_furniture CASCADE;
DROP TABLE IF EXISTS room_maintenance CASCADE;
DROP TABLE IF EXISTS room_cleaning CASCADE;
DROP TABLE IF EXISTS room_services CASCADE;
DROP TABLE IF EXISTS check_ins CASCADE;
DROP TABLE IF EXISTS rooms CASCADE;
DROP TABLE IF EXISTS room_types CASCADE;
DROP TABLE IF EXISTS maintenance_types CASCADE;
DROP TABLE IF EXISTS furniture CASCADE;
DROP TABLE IF EXISTS guests CASCADE;


-- =====================================================================================================================
-- ENUMS
-- =====================================================================================================================
-- Các kiểu dữ liệu ENUM cho Room Management
DROP TYPE IF EXISTS room_status CASCADE;
CREATE TYPE room_status AS ENUM ('VACANT', 'BOOKED', 'CHECKED_IN', 'MAINTENANCE', 'CLEANING', 'CHECKED_OUT');

DROP TYPE IF EXISTS issue_type CASCADE;
CREATE TYPE issue_type AS ENUM ('PLUMBING', 'ELECTRICAL', 'HVAC', 'STRUCTURAL', 'APPLIANCE', 'OTHER');

DROP TYPE IF EXISTS maintenance_priority CASCADE;
CREATE TYPE maintenance_priority AS ENUM ('LOW', 'MEDIUM', 'HIGH', 'URGENT');

DROP TYPE IF EXISTS maintenance_status CASCADE;
CREATE TYPE maintenance_status AS ENUM ('REQUESTED', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');

DROP TYPE IF EXISTS cleaning_type CASCADE;
CREATE TYPE cleaning_type AS ENUM ('DAILY', 'DEEP_CLEANING', 'POST_CHECK_OUT', 'AS_REQUESTED');

DROP TYPE IF EXISTS cleaning_priority CASCADE;
CREATE TYPE cleaning_priority AS ENUM ('LOW', 'MEDIUM', 'HIGH', 'URGENT');

DROP TYPE IF EXISTS cleaning_status CASCADE;
CREATE TYPE cleaning_status AS ENUM ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');

DROP TYPE IF EXISTS guest_status CASCADE;
CREATE TYPE guest_status AS ENUM ('ACTIVE', 'INACTIVE', 'BANNED');

DROP TYPE IF EXISTS checkin_status CASCADE;
CREATE TYPE checkin_status AS ENUM ('ACTIVE', 'COMPLETED', 'CANCELLED');

DROP TYPE IF EXISTS service_status CASCADE;
CREATE TYPE service_status AS ENUM ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');

-- =====================================================================================================================
-- Bảng FURNITURE (Room Management Schema)
-- =====================================================================================================================
CREATE TABLE furniture
(
    furniture_id SERIAL PRIMARY KEY,
    name         VARCHAR(255)   NOT NULL,
    price        DECIMAL(10, 2) NOT NULL
);

-- =====================================================================================================================
-- Bảng MAINTENANCE_TYPES (Room Management Schema)
-- =====================================================================================================================
CREATE TABLE maintenance_types
(
    maintenance_type_id SERIAL PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    description         TEXT
);

-- =====================================================================================================================
-- Bảng ROOM_TYPES (Room Management Schema)
-- =====================================================================================================================
CREATE TABLE room_types (
                            room_type_id SERIAL PRIMARY KEY,
                            type_name VARCHAR(255) NOT NULL,
                            description TEXT,
                            base_price DECIMAL(10, 2) NOT NULL,
                            max_occupancy INT
);

-- =====================================================================================================================
-- Bảng ROOMS (Room Management Schema)
-- =====================================================================================================================
CREATE TABLE rooms
(
    room_id        UUID PRIMARY KEY,
    room_type_id   INT, -- Khóa ngoại mới
    room_number    VARCHAR(10) NOT NULL UNIQUE,
    floor          INT,
    area           VARCHAR(255),
    room_status    room_status,
    image_url      VARCHAR(255),
    CONSTRAINT fk_room_type FOREIGN KEY (room_type_id) REFERENCES room_types (room_type_id)
);

-- =====================================================================================================================
-- Bảng ROOM_TYPE_FURNITURE (Room Management Schema)
-- =====================================================================================================================
CREATE TABLE room_type_furniture
(
    room_type_furniture_id SERIAL PRIMARY KEY,
    room_type_id           INT,
    furniture_id           INT,
    require_quantity       INT,
    CONSTRAINT fk_room_type_furn FOREIGN KEY (room_type_id) REFERENCES room_types (room_type_id),
    CONSTRAINT fk_furniture_furn FOREIGN KEY (furniture_id) REFERENCES furniture (furniture_id)
);

-- =====================================================================================================================
-- Bảng ROOM_MAINTENANCE (Room Management Schema)
-- =====================================================================================================================
CREATE TABLE room_maintenance
(
    id                     UUID PRIMARY KEY,
    room_id                UUID                     NOT NULL, -- Khóa ngoại
    maintenance_type_id    INT                      NOT NULL, -- Khóa ngoại
    room_number            VARCHAR(10)              NOT NULL,
    issue_type             issue_type               NOT NULL,
    priority               maintenance_priority     NOT NULL,
    status                 maintenance_status       NOT NULL,
    description            TEXT,
    notes                  TEXT,
    requested_by           VARCHAR(50),
    assigned_to            VARCHAR(50),
    requested_at           TIMESTAMP WITHOUT TIME ZONE,
    scheduled_at           TIMESTAMP WITHOUT TIME ZONE,
    started_at             TIMESTAMP WITHOUT TIME ZONE,
    completed_at           TIMESTAMP WITHOUT TIME ZONE,
    estimated_cost         DECIMAL(10, 2),
    actual_cost            DECIMAL(10, 2),
    is_urgent              BOOLEAN,
    special_instructions   TEXT,
    created_at             TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at             TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_room FOREIGN KEY (room_id) REFERENCES rooms (room_id),
    CONSTRAINT fk_maintenance_type FOREIGN KEY (maintenance_type_id) REFERENCES maintenance_types (maintenance_type_id)
);

-- =====================================================================================================================
-- Bảng GUESTS (Room Management Schema)
-- =====================================================================================================================
CREATE TABLE guests
(
    id                 UUID PRIMARY KEY,
    first_name         VARCHAR(255) NOT NULL,
    last_name          VARCHAR(255) NOT NULL,
    full_name          VARCHAR(255),
    phone              VARCHAR(20) UNIQUE,
    email              VARCHAR(255) UNIQUE,
    id_number          VARCHAR(50)  UNIQUE,
    id_type            VARCHAR(50),
    nationality        VARCHAR(50),
    address            TEXT,
    date_of_birth      DATE,
    gender             VARCHAR(10),
    special_requests   TEXT,
    status             guest_status,
    created_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

-- =====================================================================================================================
-- Bảng CHECK_INS (Room Management Schema)
-- =====================================================================================================================
CREATE TABLE check_ins
(
    id               UUID PRIMARY KEY,
    booking_id       UUID,
    guest_id         UUID                     NOT NULL,
    room_id          UUID                     NOT NULL,
    room_number      VARCHAR(10)              NOT NULL,
    check_in_date    DATE                     NOT NULL,
    check_out_date   DATE,
    check_in_time    TIMESTAMP WITHOUT TIME ZONE,
    check_out_time   TIMESTAMP WITHOUT TIME ZONE,
    number_of_guests INT,
    special_requests TEXT,
    status           checkin_status,
    checked_in_by    VARCHAR(50),
    checked_out_by   VARCHAR(50),
    notes            TEXT,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_guest FOREIGN KEY (guest_id) REFERENCES guests (id),
    CONSTRAINT fk_room_checkin FOREIGN KEY (room_id) REFERENCES rooms (room_id)
);

-- =====================================================================================================================
-- Bảng ROOM_CLEANING (Room Management Schema)
-- =====================================================================================================================
CREATE TABLE room_cleaning
(
    id                   UUID PRIMARY KEY,
    room_id              UUID                     NOT NULL,
    room_number          VARCHAR(10)              NOT NULL,
    cleaning_type        cleaning_type            NOT NULL,
    priority             cleaning_priority        NOT NULL,
    status               cleaning_status          NOT NULL,
    description          TEXT,
    notes                TEXT,
    requested_by         VARCHAR(50),
    assigned_to          VARCHAR(50),
    requested_at         TIMESTAMP WITHOUT TIME ZONE,
    scheduled_at         TIMESTAMP WITHOUT TIME ZONE,
    started_at           TIMESTAMP WITHOUT TIME ZONE,
    completed_at         TIMESTAMP WITHOUT TIME ZONE,
    is_urgent            BOOLEAN,
    special_instructions TEXT,
    created_at           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_room_cleaning FOREIGN KEY (room_id) REFERENCES rooms (room_id)
);

-- =====================================================================================================================
-- Bảng ROOM_SERVICES (Room Management Schema)
-- =====================================================================================================================
CREATE TABLE room_services
(
    service_id           UUID PRIMARY KEY,
    room_number          VARCHAR(10) NOT NULL,
    guest_id             UUID,
    guest_name           VARCHAR(255),
    service_type         VARCHAR(255) NOT NULL,
    service_name         VARCHAR(255) NOT NULL,
    description          TEXT,
    quantity             INT,
    unit_price           DECIMAL(10, 2),
    total_price          DECIMAL(10, 2),
    status               service_status,
    requested_at         TIMESTAMP WITHOUT TIME ZONE,
    completed_at         TIMESTAMP WITHOUT TIME ZONE,
    requested_by         VARCHAR(50),
    completed_by         VARCHAR(50),
    notes                TEXT,
    special_instructions TEXT,
    created_at           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_guest_service FOREIGN KEY (guest_id) REFERENCES guests (id)
);

-- =====================================================================================================================
-- INDEXES
-- =====================================================================================================================
-- Guest indexes
CREATE INDEX idx_guests_phone ON guests(phone);
CREATE INDEX idx_guests_email ON guests(email);

-- Room indexes
CREATE INDEX idx_rooms_room_number ON rooms(room_number);
CREATE INDEX idx_rooms_room_type_id ON rooms(room_type_id);
CREATE INDEX idx_rooms_room_status ON rooms(room_status);

-- Check-in indexes
CREATE INDEX idx_check_ins_guest_id ON check_ins(guest_id);
CREATE INDEX idx_check_ins_room_id ON check_ins(room_id);
CREATE INDEX idx_check_ins_room_number ON check_ins(room_number);

-- Maintenance indexes
CREATE INDEX idx_room_maintenance_room_id ON room_maintenance(room_id);
CREATE INDEX idx_room_maintenance_room_number ON room_maintenance(room_number);
CREATE INDEX idx_room_maintenance_status ON room_maintenance(status);

-- Cleaning indexes
CREATE INDEX idx_room_cleaning_room_id ON room_cleaning(room_id);
CREATE INDEX idx_room_cleaning_room_number ON room_cleaning(room_number);
CREATE INDEX idx_room_cleaning_status ON room_cleaning(status);

-- Service indexes
CREATE INDEX idx_room_services_room_number ON room_services(room_number);
CREATE INDEX idx_room_services_guest_id ON room_services(guest_id);
CREATE INDEX idx_room_services_status ON room_services(status);