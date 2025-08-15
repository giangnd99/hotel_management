-- Create schema for service management
CREATE SCHEMA IF NOT EXISTS service_management;

-- Set search path to service_management schema
SET search_path TO service_management, public;

-- Create service_management schema for test environment
CREATE SCHEMA IF NOT EXISTS service_management_test;

-- Create tables for service management

-- Services table
CREATE TABLE IF NOT EXISTS service_management.services (
    service_id SERIAL PRIMARY KEY,
    service_name VARCHAR(200) NOT NULL UNIQUE,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    availability VARCHAR(50) NOT NULL DEFAULT 'Available',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Service orders table
CREATE TABLE IF NOT EXISTS service_management.service_orders (
    order_id SERIAL PRIMARY KEY,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    service_id INTEGER NOT NULL REFERENCES service_management.services(service_id),
    customer_id VARCHAR(100) NOT NULL,
    room_id VARCHAR(100),
    quantity INTEGER NOT NULL DEFAULT 1,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'NEW',
    payment_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    special_instructions TEXT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_services_availability ON service_management.services(availability);
CREATE INDEX IF NOT EXISTS idx_service_orders_customer_id ON service_management.service_orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_service_orders_room_id ON service_management.service_orders(room_id);
CREATE INDEX IF NOT EXISTS idx_service_orders_status ON service_management.service_orders(status);
CREATE INDEX IF NOT EXISTS idx_service_orders_payment_status ON service_management.service_orders(payment_status);
CREATE INDEX IF NOT EXISTS idx_service_orders_created_at ON service_management.service_orders(created_at);

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION service_management.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_services_updated_at 
    BEFORE UPDATE ON service_management.services 
    FOR EACH ROW EXECUTE FUNCTION service_management.update_updated_at_column();

CREATE TRIGGER update_service_orders_updated_at 
    BEFORE UPDATE ON service_management.service_orders 
    FOR EACH ROW EXECUTE FUNCTION service_management.update_updated_at_column();

-- Insert sample data for development
INSERT INTO service_management.services (service_id, service_name, description, price, availability) VALUES
    (1, 'Phòng đơn', 'Phòng đơn tiện nghi với giường cỡ lớn và phòng tắm riêng.', 100.00, 'Available'),
    (2, 'Phòng đôi', 'Phòng đôi rộng rãi với hai giường đơn hoặc một giường đôi lớn, có ban công.', 150.00, 'Available'),
    (3, 'Phòng gia đình', 'Phòng lớn phù hợp cho gia đình, bao gồm 2 phòng ngủ và khu vực sinh hoạt chung.', 250.00, 'Available'),
    (4, 'Dịch vụ giặt là', 'Dịch vụ giặt là nhanh chóng và chuyên nghiệp cho khách hàng.', 20.50, 'Available'),
    (5, 'Dịch vụ ăn sáng', 'Bữa sáng tự chọn phong phú với nhiều món ăn địa phương và quốc tế.', 15.00, 'Available'),
    (6, 'Thuê xe đạp', 'Thuê xe đạp để khám phá khu vực xung quanh khách sạn.', 10.00, 'Available'),
    (7, 'Spa & Massage', 'Các liệu pháp spa và massage thư giãn chuyên nghiệp.', 80.00, 'Available'),
    (8, 'Đưa đón sân bay', 'Dịch vụ đưa đón từ/đến sân bay theo yêu cầu.', 40.00, 'Available'),
    (9, 'Hồ bơi', 'Sử dụng hồ bơi trong nhà và ngoài trời của khách sạn.', 0.00, 'Available'),
    (10, 'Phòng họp', 'Phòng họp được trang bị đầy đủ tiện nghi cho các sự kiện kinh doanh.', 300.00, 'Unavailable');

-- Reset sequence
ALTER SEQUENCE service_management.services_service_id_seq RESTART WITH 11;
