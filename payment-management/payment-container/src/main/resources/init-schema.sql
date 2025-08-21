-- Đảm bảo schema tồn tại
CREATE SCHEMA IF NOT EXISTS payment_db;

-- Đặt đường dẫn tìm kiếm để làm cho các câu lệnh ngắn gọn hơn
SET search_path TO payment_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Xóa các bảng nếu chúng đã tồn tại để tránh lỗi
DROP TABLE IF EXISTS invoice_payment CASCADE;
DROP TABLE IF EXISTS invoice_restaurant CASCADE;
DROP TABLE IF EXISTS invoice_service CASCADE;
DROP TABLE IF EXISTS invoice_voucher CASCADE;
DROP TABLE IF EXISTS invoice_booking CASCADE;
DROP TABLE IF EXISTS payment CASCADE;
DROP TABLE IF EXISTS invoice CASCADE;

-- Tạo bảng `invoice`
CREATE TABLE invoice (
                         id UUID PRIMARY KEY,
                         customer_id UUID,
                         staff_id UUID,
                         tax_rate DECIMAL(15, 2) NOT NULL,
                         sub_total DECIMAL(15, 2) NOT NULL,
                         total_amount DECIMAL(15, 2) NOT NULL,
                         status VARCHAR(50) NOT NULL,
                         created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                         updated_date TIMESTAMP WITHOUT TIME ZONE,
                         note TEXT
);

-- Thêm chỉ mục cho các cột thường được dùng để tìm kiếm
CREATE INDEX idx_invoice_customer_id ON invoice(customer_id);
CREATE INDEX idx_invoice_status ON invoice(status);
CREATE INDEX idx_invoice_created_at ON invoice(created_date);

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

-- Tạo bảng `invoice_booking`
CREATE TABLE invoice_booking (
                                 id UUID PRIMARY KEY,
                                 invoice_id UUID NOT NULL,
                                 booking_id UUID NOT NULL,
                                 room_name VARCHAR(100),
                                 quantity INT NOT NULL,
                                 unit_price DECIMAL(15, 2) NOT NULL,
                                 FOREIGN KEY (invoice_id) REFERENCES invoice (id) ON DELETE CASCADE
);

-- Thêm các chỉ mục và ràng buộc UNIQUE
CREATE UNIQUE INDEX idx_unique_invoice_booking ON invoice_booking(invoice_id, booking_id);

-- Tạo bảng `invoice_voucher`
CREATE TABLE invoice_voucher (
                                 id UUID PRIMARY KEY,
                                 invoice_id UUID NOT NULL,
                                 voucher_id UUID NOT NULL,
                                 amount DECIMAL(15, 2) NOT NULL,
                                 FOREIGN KEY (invoice_id) REFERENCES invoice (id) ON DELETE CASCADE
);

-- Thêm các chỉ mục và ràng buộc UNIQUE
CREATE UNIQUE INDEX idx_unique_invoice_voucher ON invoice_voucher(invoice_id, voucher_id);

-- Tạo bảng `invoice_service`
CREATE TABLE invoice_service (
                                 id UUID PRIMARY KEY,
                                 service_id UUID NOT NULL,
                                 invoice_booking_id UUID NOT NULL,
                                 service_name VARCHAR(255),
                                 quantity INT NOT NULL,
                                 unit_price DECIMAL(15, 2) NOT NULL,
                                 FOREIGN KEY (invoice_booking_id) REFERENCES invoice_booking (id) ON DELETE CASCADE
);

-- Thêm chỉ mục
CREATE INDEX idx_invoice_service_booking_id ON invoice_service(invoice_booking_id);

-- Tạo bảng `invoice_restaurant`
CREATE TABLE invoice_restaurant (
                                    id UUID PRIMARY KEY,
                                    invoice_booking_id UUID NOT NULL,
                                    restaurant_id UUID NOT NULL,
                                    restaurant_name VARCHAR(255),
                                    quantity INT NOT NULL,
                                    unit_price DECIMAL(15, 2) NOT NULL,
                                    FOREIGN KEY (invoice_booking_id) REFERENCES invoice_booking (id) ON DELETE CASCADE
);

-- Thêm chỉ mục
CREATE INDEX idx_invoice_restaurant_booking_id ON invoice_restaurant(invoice_booking_id);

-- Tạo bảng `invoice_payment`
CREATE TABLE invoice_payment (
                                 id UUID PRIMARY KEY,
                                 invoice_id UUID ,
                                 payment_id UUID
);

-- Thêm ràng buộc UNIQUE để ngăn chặn việc liên kết thanh toán trùng lặp
CREATE UNIQUE INDEX idx_unique_invoice_payment ON invoice_payment(invoice_id, payment_id);