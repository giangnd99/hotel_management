DROP
DATABASE IF EXISTS payment_management;
CREATE
DATABASE IF NOT EXISTS payment_management;
USE payment_management;

CREATE TABLE invoice
(
    invoice_id      BINARY(16) NOT NULL PRIMARY KEY,
    booking_id      BINARY(16),
    customer_id     BINARY(16),
    created_by      BINARY(16),
    updated_by      BINARY(16),
    voucher_id      BINARY(16),
    sub_total       DECIMAL(15, 2),
    tax_amount      DECIMAL(15, 2),
    discount_amount DECIMAL(15, 2),
    total_amount    DECIMAL(15, 2),
    paid_amount     DECIMAL(15, 2),
    invoice_status  ENUM ('DRAFT', 'PAID', 'PENDING', 'CANCELED', 'FINALIZED') NOT NULL, -- DRAFT, FINALIZED, PAID, CANCELLED
    create_at       DATETIME,
    update_at       DATETIME,
    change_amount   DECIMAL(15, 2),
    note            TEXT
);

CREATE TABLE invoice_item
(
    invoice_item_id BINARY(16) NOT NULL PRIMARY KEY,
    invoice_id      BINARY(16),
    service_id      BINARY(16),
    description     TEXT,
    service_type    ENUM ('ROOM', 'FOOD', 'SPA', 'OTHER'), -- ENUM-like (you handle validation in app)
    quantity        INT,
    unit_price      DECIMAL(15, 2),
    create_at       DATETIME,
    note            TEXT,
    FOREIGN KEY (invoice_id) REFERENCES invoice (invoice_id) ON DELETE CASCADE
);

CREATE TABLE payment
(
    payment_id               BINARY(16) NOT NULL PRIMARY KEY,
    booking_id               BINARY(16),
    invoice_id               BINARY(16),
    payment_status           ENUM ('PENDING', 'COMPLETED', 'CANCELLED', 'FAILED', 'EXPIRED'), -- PENDING, COMPLETED, CANCELLED, FAILED
    amount                   DECIMAL(15, 2),
    payment_method           ENUM ('CASH', 'PAYOS'), -- CASH, PAYOS
    paid_at                  DATETIME,
    created_at               DATETIME,
    payment_transaction_type ENUM ('DEPOSIT', 'INVOICE_PAYMENT', 'REFUND', 'OTHER'), -- , etc.
    reference_code           VARCHAR(100)
);

select * from payment p where p.payment_status = 'PENDING' AND p.payment_transaction_type = 'DEPOSIT' AND p.created_at <= NOW() - INTERVAL 20 MINUTE ;
select * from payment p where p.created_at < NOW() ;
SELECT payment_id, created_at, NOW(), created_at <= NOW() AS is_before_now
FROM payment;
SELECT payment_id, created_at FROM payment WHERE created_at IS NOT NULL;
SELECT NOW(), @@global.time_zone, @@session.time_zone;
SET time_zone = '+07:00';

