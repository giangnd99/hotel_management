CREATE DATABASE IF NOT EXISTS db_restaurant;
USE db_restaurant;

CREATE TABLE menu_items (
    menu_item_id INT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'
);

CREATE TABLE orders (
    order_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    table_id VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'NEW',
    customer_note TEXT
);

CREATE TABLE order_items (
    order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    menu_item_id VARCHAR(50) NOT NULL,
    order_id VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

CREATE TABLE tables (
    table_id VARCHAR(50) PRIMARY KEY,
    number INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'
);

-- Menu Items
INSERT INTO `menu_items` (`menu_item_id`, `item_name`, `description`, `price`, `category`, `quantity`, `status`) VALUES
    (101, 'Phở bò', 'Phở bò truyền thống', 150000, 'Món chính', 100, 'AVAILABLE'),
    (102, 'Trà đá', 'Trà đá mát lạnh', 25000, 'Đồ uống', 200, 'AVAILABLE'),
    (103, 'Cà phê sữa', 'Cà phê pha với sữa đặc', 90000, 'Đồ uống', 150, 'AVAILABLE'),
    (104, 'Bún chả', 'Bún chả Hà Nội', 120000, 'Món chính', 80, 'AVAILABLE'),
    (105, 'Gỏi cuốn', 'Gỏi cuốn tôm thịt', 80000, 'Khai vị', 60, 'AVAILABLE');

-- Tables
INSERT INTO `tables` (`table_id`, `number`, `status`) VALUES
    ('table_001', 1, 'AVAILABLE'),
    ('table_002', 2, 'AVAILABLE'),
    ('table_003', 3, 'AVAILABLE'),
    ('table_004', 4, 'OCCUPIED'),
    ('table_005', 5, 'AVAILABLE');

-- Restaurant Orders
INSERT INTO `orders` (`order_id`, `customer_id`, `table_id`, `created_at`, `status`, `customer_note`) VALUES
    ('order_001', 'customer_001', 'table_001', '2025-01-15 10:00:00', 'COMPLETED', 'Không cay'),
    ('order_002', 'customer_001', 'table_002', '2025-01-15 11:00:00', 'IN_PROGRESS', 'Thêm rau'),
    ('order_003', 'customer_001', 'table_003', '2025-01-15 12:00:00', 'NEW', NULL);

-- Order Items
INSERT INTO `order_items` (`menu_item_id`, `order_id`, `quantity`, `price`) VALUES
    ('101', 'order_001', 2, 150000),
    ('102', 'order_001', 2, 25000),
    ('103', 'order_002', 1, 90000),
    ('104', 'order_002', 1, 120000),
    ('105', 'order_003', 1, 80000);
