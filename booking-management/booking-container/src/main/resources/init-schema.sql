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
-- Payment Outbox
-- =====================================================================================================================
CREATE TABLE booking.payment_outbox
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
    CONSTRAINT payment_outbox_pkey PRIMARY KEY (id),
    CONSTRAINT FK_PAYMENT_OUTBOX_BOOKING FOREIGN KEY (booking_id) REFERENCES booking.bookings (id)
);

CREATE INDEX payment_outbox_saga_status ON booking.payment_outbox (type, outbox_status, saga_status);
CREATE INDEX payment_outbox_booking_id ON booking.payment_outbox (booking_id);

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
    outbox_status outbox_status NOT NULL,
    saga_status saga_status NOT NULL,
    booking_status booking_status NOT NULL,
    version integer NOT NULL,
    CONSTRAINT notification_outbox_pkey PRIMARY KEY (id),
    CONSTRAINT FK_NOTIFICATION_OUTBOX_BOOKING FOREIGN KEY (booking_id) REFERENCES booking.bookings (id)
);

CREATE INDEX notification_outbox_saga_status ON booking.notification_outbox (type, outbox_status, saga_status);
CREATE INDEX notification_outbox_booking_id ON booking.notification_outbox (booking_id);

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
    CONSTRAINT room_outbox_pkey PRIMARY KEY (id),
    CONSTRAINT FK_ROOM_OUTBOX_BOOKING FOREIGN KEY (booking_id) REFERENCES booking.bookings (id)
);

CREATE INDEX room_outbox_saga_status ON booking.room_outbox (type, outbox_status, saga_status);
CREATE INDEX room_outbox_booking_id ON booking.room_outbox (booking_id);
