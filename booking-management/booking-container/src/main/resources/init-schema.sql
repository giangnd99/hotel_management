DROP SCHEMA IF EXISTS booking CASCADE;
CREATE SCHEMA IF NOT EXISTS booking;

-- Tạo extension để hỗ trợ UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =====================================================================================================================
-- Bảng Customers
-- =====================================================================================================================
CREATE TABLE booking.customers
(
    id uuid NOT NULL,
    username varchar NOT NULL,
    first_name varchar NOT NULL,
    last_name varchar NOT NULL,
    email varchar NOT NULL,
    CONSTRAINT customers_pkey PRIMARY KEY (id)
);

-- =====================================================================================================================
-- Bảng Bookings
-- =====================================================================================================================
DROP TYPE IF EXISTS booking_status;
CREATE TYPE booking_status AS ENUM ('PENDING', 'DEPOSITED', 'CONFIRMED', 'CHECKED_IN', 'PAID', 'CHECKED_OUT', 'CANCELLING', 'CANCELLED');

CREATE TABLE booking.bookings
(
    id uuid NOT NULL,
    customer_id uuid NOT NULL,
    check_in TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    check_out TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    actual_check_in TIMESTAMP WITHOUT TIME ZONE,
    actual_check_out TIMESTAMP WITHOUT TIME ZONE,
    tracking_id uuid ,
    total_price numeric(10,2) NOT NULL,
    status booking_status NOT NULL,
    CONSTRAINT bookings_pkey PRIMARY KEY (id),
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES booking.customers (id) ON DELETE CASCADE

);

CREATE INDEX booking_customer_id ON booking.bookings (customer_id);
CREATE UNIQUE INDEX booking_tracking_id ON booking.bookings (tracking_id);

-- =====================================================================================================================
-- Bảng Rooms
-- =====================================================================================================================
CREATE TABLE booking.rooms
(
    id uuid NOT NULL,
    room_number varchar NOT NULL,
    price numeric(10,2) NOT NULL,
    status varchar NOT NULL,
    CONSTRAINT rooms_pkey PRIMARY KEY (id)
);

-- =====================================================================================================================
-- Bảng Booking_Room
-- =====================================================================================================================
CREATE TABLE booking.booking_room
(
    id uuid NOT NULL,
    booking_id uuid NOT NULL,
    room_id uuid NOT NULL,
    price numeric(10,2) NOT NULL,
    CONSTRAINT booking_room_pkey PRIMARY KEY (id,booking_id),
    CONSTRAINT FK_BOOKING_ID FOREIGN KEY (booking_id) REFERENCES booking.bookings (id) MATCH SIMPLE ON DELETE CASCADE  ON UPDATE NO ACTION
);

CREATE INDEX booking_room_booking_id ON booking.booking_room (booking_id);
CREATE INDEX booking_room_room_id ON booking.booking_room (room_id);

-- =====================================================================================================================
-- ENUM cho Outbox & Saga
-- =====================================================================================================================
DROP TYPE IF EXISTS saga_status;
CREATE TYPE saga_status AS ENUM ('STARTED', 'FAILED', 'SUCCEEDED', 'PROCESSING', 'COMPENSATING', 'COMPENSATED');

DROP TYPE IF EXISTS outbox_status;
CREATE TYPE outbox_status AS ENUM ('STARTED', 'COMPLETED', 'FAILED');

-- =====================================================================================================================
-- Notification Outbox
-- =====================================================================================================================
CREATE TABLE booking.notification_outbox
(
    id uuid NOT NULL,
    saga_id uuid NOT NULL,
    booking_id uuid NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    processed_at TIMESTAMP WITH TIME ZONE,
    type varchar NOT NULL,
    payload jsonb NOT NULL,
    outbox_status outbox_status ,
    saga_status saga_status ,
    booking_status booking_status,
    version integer NOT NULL,
    CONSTRAINT notification_outbox_pkey PRIMARY KEY (id));

CREATE INDEX notification_outbox_saga_status ON booking.notification_outbox (type, outbox_status, saga_status);

-- =====================================================================================================================
-- Room Outbox
-- =====================================================================================================================
CREATE TABLE booking.room_outbox
(
    id uuid NOT NULL,
    saga_id uuid NOT NULL,
    booking_id uuid NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    processed_at TIMESTAMP WITH TIME ZONE,
    type varchar NOT NULL,
    payload jsonb NOT NULL,
    outbox_status outbox_status NOT NULL,
    saga_status saga_status NOT NULL,
    booking_status booking_status NOT NULL,
    version integer NOT NULL,
    CONSTRAINT room_outbox_pkey PRIMARY KEY (id));

CREATE INDEX room_outbox_saga_status ON booking.room_outbox (type, outbox_status, saga_status);


SET search_path TO booking, public;

-- ==========================================================
-- CUSTOMERS (10 records)
-- ==========================================================
INSERT INTO customers (id, username, first_name, last_name, email) VALUES
                                                                       ('11111111-1111-1111-1111-111111111111'::uuid, 'admin@example.com', 'Nguyen', 'Van A', 'admin@example.com'),
                                                                       ('22222222-2222-2222-2222-222222222222'::uuid, 'user@example.com', 'Tran', 'Thi B', 'user@example.com'),
                                                                       (gen_random_uuid(), 'user3', 'Le', 'Van C', 'user3@example.com'),
                                                                       (gen_random_uuid(), 'user4', 'Pham', 'Thi D', 'user4@example.com'),
                                                                       (gen_random_uuid(), 'user5', 'Do', 'Van E', 'user5@example.com'),
                                                                       (gen_random_uuid(), 'user6', 'Hoang', 'Thi F', 'user6@example.com'),
                                                                       (gen_random_uuid(), 'user7', 'Bui', 'Van G', 'user7@example.com'),
                                                                       (gen_random_uuid(), 'user8', 'Ngo', 'Thi H', 'user8@example.com'),
                                                                       (gen_random_uuid(), 'user9', 'Dang', 'Van I', 'user9@example.com'),
                                                                       (gen_random_uuid(), 'user10', 'Vo', 'Thi J', 'user10@example.com');

-- ==========================================================
-- ROOMS (10 records)
-- ==========================================================
INSERT INTO rooms (id, room_number, price, status) VALUES
                                                       ('46857eb2-b9bf-437b-b3df-23b70fbe2d15'::uuid, '101', 1000000, 'VACANT'),
                                                       ('2a0a89ee-6bba-4a04-bf05-109c08154c5c'::uuid, '102', 1200000, 'BOOKED'),
                                                       ('bfec37b4-3faf-4a7d-85f7-72837330d9b8'::uuid, '103', 1500000, 'CHECKED_IN'),
                                                       ('922f4945-8dd7-41a6-9bc4-3af96490703c'::uuid, '104', 1800000, 'MAINTENANCE'),
                                                       ('8c8a420f-4cfd-43b8-a603-95ecc60f52e3'::uuid, '201', 2000000, 'CHECKED_OUT'),
                                                       ('f212e924-0f5d-43b2-a274-1f861cd7681d'::uuid, '202', 2200000, 'CHECKED_OUT'),
                                                       ('6d559770-d67e-4135-81cf-d480002fa546'::uuid, '203', 2500000, 'VACANT'),
                                                       ('7bd4d491-55a1-49d4-bd70-1c04d6f3cf65'::uuid, '204', 2800000, 'VACANT'),
                                                       ('3c671572-54df-49ec-9f43-f47e31b0ceea'::uuid, '301', 3000000, 'VACANT'),
                                                       ('661ecffa-6d93-41c2-9618-348e405d8f62'::uuid, '302', 3500000, 'VACANT');

-- ==========================================================
-- BOOKINGS (10 records)
-- note: customer_id lấy ngẫu nhiên từ bảng customers
-- ==========================================================
INSERT INTO bookings (id, customer_id, check_in, check_out, actual_check_in, actual_check_out, tracking_id, total_price, status)
VALUES
    (gen_random_uuid(), (SELECT id FROM customers LIMIT 1 OFFSET 0), '2025-09-01', '2025-09-05', NULL, NULL, gen_random_uuid(), 4000000, 'PENDING'),
    (gen_random_uuid(), (SELECT id FROM customers LIMIT 1 OFFSET 1), '2025-09-02', '2025-09-06', NULL, NULL, gen_random_uuid(), 5000000, 'DEPOSITED'),
    (gen_random_uuid(), (SELECT id FROM customers LIMIT 1 OFFSET 2), '2025-09-03', '2025-09-07', NULL, NULL, gen_random_uuid(), 6000000, 'CONFIRMED'),
    (gen_random_uuid(), (SELECT id FROM customers LIMIT 1 OFFSET 3), '2025-09-04', '2025-09-08', NULL, NULL, gen_random_uuid(), 7000000, 'CHECKED_IN'),
    (gen_random_uuid(), (SELECT id FROM customers LIMIT 1 OFFSET 4), '2025-09-05', '2025-09-09', NULL, NULL, gen_random_uuid(), 8000000, 'PAID'),
    (gen_random_uuid(), (SELECT id FROM customers LIMIT 1 OFFSET 5), '2025-09-06', '2025-09-10', NULL, NULL, gen_random_uuid(), 9000000, 'CHECKED_OUT'),
    (gen_random_uuid(), (SELECT id FROM customers LIMIT 1 OFFSET 6), '2025-09-07', '2025-09-11', NULL, NULL, gen_random_uuid(), 5500000, 'CANCELLING'),
    (gen_random_uuid(), (SELECT id FROM customers LIMIT 1 OFFSET 7), '2025-09-08', '2025-09-12', NULL, NULL, gen_random_uuid(), 4500000, 'CANCELLED'),
    (gen_random_uuid(), (SELECT id FROM customers LIMIT 1 OFFSET 8), '2025-09-09', '2025-09-13', NULL, NULL, gen_random_uuid(), 6500000, 'PENDING'),
    (gen_random_uuid(), (SELECT id FROM customers LIMIT 1 OFFSET 9), '2025-09-10', '2025-09-14', NULL, NULL, gen_random_uuid(), 7500000, 'DEPOSITED');

-- ==========================================================
-- BOOKING_ROOM (10 records)
-- mapping booking_id và room_id
-- ==========================================================
INSERT INTO booking_room (id, booking_id, room_id, price) VALUES
                                                              (gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 0), (SELECT id FROM rooms LIMIT 1 OFFSET 0), 1000000),
    (gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 1), (SELECT id FROM rooms LIMIT 1 OFFSET 1), 1200000),
    (gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 2), (SELECT id FROM rooms LIMIT 1 OFFSET 2), 1500000),
    (gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 3), (SELECT id FROM rooms LIMIT 1 OFFSET 3), 1800000),
    (gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 4), (SELECT id FROM rooms LIMIT 1 OFFSET 4), 2000000),
    (gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 5), (SELECT id FROM rooms LIMIT 1 OFFSET 5), 2200000),
    (gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 6), (SELECT id FROM rooms LIMIT 1 OFFSET 6), 2500000),
    (gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 7), (SELECT id FROM rooms LIMIT 1 OFFSET 7), 2800000),
    (gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 8), (SELECT id FROM rooms LIMIT 1 OFFSET 8), 3000000),
    (gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 9), (SELECT id FROM rooms LIMIT 1 OFFSET 9), 3500000);

-- ==========================================================
-- NOTIFICATION_OUTBOX (10 records)
-- ==========================================================
INSERT INTO notification_outbox (id, saga_id, booking_id, created_at, processed_at, type, payload, outbox_status, saga_status, booking_status, version) VALUES
                                                                                                                                                            (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 0), NOW(), NULL, 'EMAIL', '{"msg":"Booking created"}', 'STARTED', 'STARTED', 'PENDING', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 1), NOW(), NULL, 'EMAIL', '{"msg":"Deposit confirmed"}', 'STARTED', 'PROCESSING', 'DEPOSITED', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 2), NOW(), NULL, 'SMS',   '{"msg":"Booking confirmed"}', 'COMPLETED', 'SUCCEEDED', 'CONFIRMED', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 3), NOW(), NULL, 'EMAIL', '{"msg":"Customer checked in"}', 'COMPLETED', 'SUCCEEDED', 'CHECKED_IN', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 4), NOW(), NULL, 'SMS',   '{"msg":"Payment successful"}', 'COMPLETED', 'SUCCEEDED', 'PAID', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 5), NOW(), NULL, 'EMAIL', '{"msg":"Checked out"}', 'COMPLETED', 'SUCCEEDED', 'CHECKED_OUT', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 6), NOW(), NULL, 'EMAIL', '{"msg":"Booking cancelling"}', 'FAILED', 'FAILED', 'CANCELLING', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 7), NOW(), NULL, 'EMAIL', '{"msg":"Booking cancelled"}', 'FAILED', 'COMPENSATED', 'CANCELLED', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 8), NOW(), NULL, 'EMAIL', '{"msg":"Booking retry"}', 'STARTED', 'COMPENSATING', 'PENDING', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 9), NOW(), NULL, 'EMAIL', '{"msg":"Deposit again"}', 'STARTED', 'PROCESSING', 'DEPOSITED', 1);

-- ==========================================================
-- ROOM_OUTBOX (10 records)
-- ==========================================================
INSERT INTO room_outbox (id, saga_id, booking_id, created_at, processed_at, type, payload, outbox_status, saga_status, booking_status, version) VALUES
                                                                                                                                                    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 0), NOW(), NULL, 'ROOM', '{"msg":"Assign room"}', 'STARTED', 'STARTED', 'PENDING', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 1), NOW(), NULL, 'ROOM', '{"msg":"Reserve room"}', 'STARTED', 'PROCESSING', 'DEPOSITED', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 2), NOW(), NULL, 'ROOM', '{"msg":"Room confirmed"}', 'COMPLETED', 'SUCCEEDED', 'CONFIRMED', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 3), NOW(), NULL, 'ROOM', '{"msg":"Checked in room"}', 'COMPLETED', 'SUCCEEDED', 'CHECKED_IN', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 4), NOW(), NULL, 'ROOM', '{"msg":"Payment done"}', 'COMPLETED', 'SUCCEEDED', 'PAID', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 5), NOW(), NULL, 'ROOM', '{"msg":"Checkout done"}', 'COMPLETED', 'SUCCEEDED', 'CHECKED_OUT', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 6), NOW(), NULL, 'ROOM', '{"msg":"Cancelling room"}', 'FAILED', 'FAILED', 'CANCELLING', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 7), NOW(), NULL, 'ROOM', '{"msg":"Cancelled room"}', 'FAILED', 'COMPENSATED', 'CANCELLED', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 8), NOW(), NULL, 'ROOM', '{"msg":"Retry assign"}', 'STARTED', 'COMPENSATING', 'PENDING', 1),
    (gen_random_uuid(), gen_random_uuid(), (SELECT id FROM bookings LIMIT 1 OFFSET 9), NOW(), NULL, 'ROOM', '{"msg":"Deposit room"}', 'STARTED', 'PROCESSING', 'DEPOSITED', 1);
