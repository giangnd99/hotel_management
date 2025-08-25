DROP SCHEMA IF EXISTS payment_db CASCADE;
-- Đảm bảo schema tồn tại
CREATE SCHEMA IF NOT EXISTS payment_db;

-- Đặt đường dẫn tìm kiếm để làm cho các câu lệnh ngắn gọn hơn
SET search_path TO payment_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tạo bảng `invoice`
CREATE TABLE invoice (
                         id UUID PRIMARY KEY,
                         customer_id UUID,
                         staff_id UUID,
                         tax_rate DECIMAL(15, 2) NOT NULL,
                         sub_total DECIMAL(15, 2) NOT NULL,
                         total_amount DECIMAL(15, 2) NOT NULL,
                         status VARCHAR(50) NOT NULL,
                         created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                         updated_at TIMESTAMP WITHOUT TIME ZONE,
                         note TEXT
);

-- Thêm chỉ mục cho các cột thường được dùng để tìm kiếm
CREATE INDEX idx_invoice_customer_id ON invoice(customer_id);
CREATE INDEX idx_invoice_status ON invoice(status);
CREATE INDEX idx_invoice_created_at ON invoice(created_at);

-- Tạo bảng `payment`
CREATE TABLE payment (
                         id UUID PRIMARY KEY,
                         reference_id UUID,
                         status VARCHAR(50) NOT NULL,
                         amount DECIMAL(15, 2) NOT NULL,
                         method VARCHAR(50),
                         paid_at TIMESTAMP WITHOUT TIME ZONE,
                         created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                         updated_at TIMESTAMP WITHOUT TIME ZONE,
                         order_code BIGINT UNIQUE,
                         link VARCHAR(255),
                         description TEXT
);

-- Thêm các chỉ mục cần thiết
CREATE INDEX idx_payment_reference_id ON payment(reference_id);
CREATE INDEX idx_payment_status ON payment(status);

-- Tạo bảng `invoice_payment`
CREATE TABLE invoice_payment (
                                 id UUID PRIMARY KEY,
                                 invoice_id UUID ,
                                 payment_id UUID
);

-- Thêm ràng buộc UNIQUE để ngăn chặn việc liên kết thanh toán trùng lặp
CREATE UNIQUE INDEX idx_unique_invoice_payment ON invoice_payment(invoice_id, payment_id);