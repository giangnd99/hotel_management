DROP DATABASE IF EXISTS payment_management;
CREATE DATABASE IF NOT EXISTS payment_management;
USE payment_management;

CREATE TABLE invoice
(
    id     BINARY(16)  PRIMARY KEY,
    customer_id    BINARY(16),
    created_by     BINARY(16),
    sub_total      DECIMAL(15, 2),
    tax_rate     DECIMAL(15, 2),
    total_amount   DECIMAL(15, 2),
    invoice_status ENUM ('DRAFT', 'PENDING', 'PAID', 'CANCELED', 'FAILED') NOT NULL,
    create_at      DATETIME,
    update_at      DATETIME,
    note           TEXT
);

CREATE TABLE invoice_booking
(
    id         BINARY(16) PRIMARY KEY,
    invoice_id BINARY(16),
    booking_id BINARY(16),
    room_name NVARCHAR(100),
    quantity   INT,
    unit_price DECIMAL(15, 2),
    FOREIGN KEY (invoice_id) REFERENCES invoice(id) ON DELETE CASCADE
);

CREATE TABLE invoice_voucher
(
    id         BINARY(16) PRIMARY KEY,
    invoice_id BINARY(16),
    voucher_id BINARY(16),
    amount     DECIMAL(15, 2),
    FOREIGN KEY (invoice_id) REFERENCES invoice (id) ON DELETE CASCADE
);

CREATE TABLE invoice_service
(
    id         BINARY(16) PRIMARY KEY,
    service_id BINARY(16),
    invoice_booking_id BINARY(16),
    service_name NVARCHAR(255),
    quantity   INT,
    unit_price DECIMAL(15, 2),
    FOREIGN KEY (invoice_booking_id) REFERENCES invoice_booking (id) ON DELETE CASCADE
);

CREATE TABLE invoice_restaurant
(
    id            BINARY(16) PRIMARY KEY,
    invoice_booking_id    BINARY(16),
    restaurant_id BINARY(16),
    restaurant_name NVARCHAR(255),
    quantity      INT,
    unit_price    DECIMAL(15, 2),
    FOREIGN KEY (invoice_booking_id) REFERENCES invoice_booking (id) ON DELETE CASCADE
);

CREATE TABLE payment
(
    id     BINARY(16) NOT NULL PRIMARY KEY,
    booking_id     BINARY(16),
    payment_status ENUM ('PENDING', 'COMPLETED', 'CANCELLED', 'FAILED', 'EXPIRED'), -- PENDING, COMPLETED, CANCELLED, FAILED
    amount         DECIMAL(15, 2),
    payment_method ENUM('CASH', 'PAYOS'),                                                    -- CASH, PAYOS
    paid_at        DATETIME,
    created_at     DATETIME,
    reference_code VARCHAR(100),
    payment_link    VARCHAR(255),
    payment_transaction_type   ENUM('DEPOSIT', 'INVOICE', 'REFUND', 'SERVICE', 'OTHER')
);

CREATE TABLE invoice_payment
(
    id                       BINARY(16) PRIMARY KEY,
    invoice_id               BINARY(16),
    payment_id               BINARY(16),
    FOREIGN KEY (invoice_id) REFERENCES invoice (id) ON DELETE CASCADE,
    FOREIGN KEY (payment_id) REFERENCES payment (id) ON DELETE CASCADE
);


