DROP
    DATABASE IF EXISTS payment_management;
CREATE
    DATABASE IF NOT EXISTS payment_management;
USE payment_management;

CREATE TABLE invoice
(
    invoice_id     BINARY(16)  PRIMARY KEY,
    customer_id    BINARY(16),
    created_by     BINARY(16),
    sub_total      DECIMAL(15, 2),
    tax_rate     DECIMAL(15, 2),
    total_amount   DECIMAL(15, 2),
    invoice_status ENUM ('DRAFT', 'PAID', 'PENDING', 'CANCELED') NOT NULL,
    create_at      DATETIME,
    update_at      DATETIME,
    note           TEXT
);

CREATE TABLE invoice_booking
(
    id         BINARY(16) PRIMARY KEY,
    invoice_id BINARY(16),
    booking_id BINARY(16),
    quantity   INT,
    unit_price DECIMAL(15, 2),
    FOREIGN KEY (invoice_id) REFERENCES invoice (invoice_id) ON DELETE CASCADE
);

CREATE TABLE invoice_voucher
(
    id         BINARY(16) PRIMARY KEY,
    invoice_id BINARY(16),
    voucher_id BINARY(16),
    amount     DECIMAL(15, 2),
    FOREIGN KEY (invoice_id) REFERENCES invoice (invoice_id) ON DELETE CASCADE
);

CREATE TABLE invoice_service
(
    id         BINARY(16) PRIMARY KEY,
    invoice_id BINARY(16),
    service_id BINARY(16),
    quantity   INT,
    unit_price DECIMAL(15, 2),
    FOREIGN KEY (invoice_id) REFERENCES invoice (invoice_id) ON DELETE CASCADE
);

CREATE TABLE invoice_restaurant
(
    id            BINARY(16) PRIMARY KEY,
    invoice_id    BINARY(16),
    restaurant_id BINARY(16),
    quantity      INT,
    unit_price    DECIMAL(15, 2),
    FOREIGN KEY (invoice_id) REFERENCES invoice (invoice_id) ON DELETE CASCADE
);

CREATE TABLE payment
(
    payment_id     BINARY(16) NOT NULL PRIMARY KEY,
    invoice_id     BINARY(16),
    payment_status ENUM ('PENDING', 'COMPLETED', 'CANCELLED', 'FAILED', 'EXPIRED'), -- PENDING, COMPLETED, CANCELLED, FAILED
    amount         DECIMAL(15, 2),
    payment_method NVARCHAR(50),                                                    -- CASH, PAYOS
    paid_at        DATETIME,
    created_at     DATETIME,
    reference_code VARCHAR(100),
    service_type   VARCHAR(100)
);

CREATE TABLE invoice_payment
(
    id                       BINARY(16) PRIMARY KEY,
    invoice_id               BINARY(16),
    payment_id               BINARY(16),
    payment_transaction_type ENUM ('DEPOSIT', 'INVOICE_PAYMENT', 'REFUND', 'OTHER'),
    FOREIGN KEY (invoice_id) REFERENCES payment (invoice_id) ON DELETE CASCADE,
    FOREIGN KEY (payment_id) REFERENCES payment (payment_id) ON DELETE CASCADE
);

select *
from payment p
where p.payment_status = 'PENDING'
  AND p.payment_transaction_type = 'DEPOSIT'
  AND p.created_at <= NOW() - INTERVAL 20 MINUTE;
select *
from payment p
where p.created_at < NOW();
SELECT payment_id, created_at, NOW(), created_at <= NOW() AS is_before_now
FROM payment;
SELECT payment_id, created_at
FROM payment
WHERE created_at IS NOT NULL;
SELECT NOW(), @@global.time_zone, @@session.time_zone;
SET time_zone = '+07:00';

SELECT *
FROM payment
WHERE booking_id = '4aee8407-3034-4172-a31a-10bbe199f848'
  AND payment_transaction_type = 'DEPOSIT';

SELECT booking_id
FROM payment;
SELECT DISTINCT payment_transaction_type
FROM payment;

SELECT *
FROM payment
WHERE booking_id = '4aee8407-3034-4172-a31a-10bbe199f848';
SELECT *
FROM payment
WHERE booking_id = UUID_TO_BIN('4aee8407-3034-4172-a31a-10bbe199f848')
  AND payment_transaction_type = 'DEPOSIT';

