-- Create database for restaurant service
DROP DATABASE IF EXISTS restaurant;
CREATE DATABASE IF NOT EXISTS restaurant;

-- Use the restaurant database
USE restaurant;

-- Create tables for restaurant service
-- Note: These are basic tables, you may need to adjust based on your actual domain entities

-- Categories table
CREATE TABLE IF NOT EXISTS categories (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- Menu items table
CREATE TABLE IF NOT EXISTS menu_items (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category_id VARCHAR(36),
    is_available BOOLEAN DEFAULT true,
    image_url VARCHAR(500),
    preparation_time INTEGER, -- in minutes
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
    );

-- Orders table
CREATE TABLE IF NOT EXISTS orders (
    id VARCHAR(36) PRIMARY KEY,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    customer_id VARCHAR(100) NOT NULL,
    room_id VARCHAR(100),
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) DEFAULT 'NEW',
    payment_status VARCHAR(50) DEFAULT 'PENDING',
    order_type VARCHAR(50) DEFAULT 'DINE_IN', -- DINE_IN, ROOM_SERVICE, TAKEAWAY
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- Order items table
CREATE TABLE IF NOT EXISTS order_items (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36),
    menu_item_id VARCHAR(36),
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
    );

-- Create indexes for better performance
-- CREATE INDEX idx_orders_customer_id ON orders(customer_id);
-- CREATE INDEX idx_orders_room_id ON orders(room_id);
-- CREATE INDEX idx_orders_status ON orders(status);
-- CREATE INDEX idx_orders_created_at ON orders(created_at);
-- CREATE INDEX idx_order_items_order_id ON order_items(order_id);
-- CREATE INDEX idx_menu_items_category_id ON menu_items(category_id);
-- CREATE INDEX idx_menu_items_available ON menu_items(is_available);

INSERT IGNORE INTO categories (id, name, description) VALUES
  ('C001', 'Appetizers', 'Starters and small plates'),
  ('C002', 'Main Courses', 'Primary dishes'),
  ('C003', 'Desserts', 'Sweet endings'),
  ('C004', 'Beverages', 'Drinks and refreshments');

INSERT IGNORE INTO menu_items (id, name, description, price, category_id, preparation_time) VALUES
  ('M001', 'Caesar Salad', 'Fresh romaine lettuce with Caesar dressing', 85000, 'C001', 10),
  ('M002', 'Grilled Salmon', 'Fresh salmon with seasonal vegetables', 275000, 'C002', 25),
  ('M003', 'Chocolate Cake', 'Rich chocolate cake with vanilla ice cream', 95000, 'C003', 5),
  ('M004', 'Fresh Orange Juice', 'Freshly squeezed orange juice', 35000, 'C004', 3);

-- Tables
CREATE TABLE IF NOT EXISTS tables (
    id VARCHAR(36) PRIMARY KEY,
    number INT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE'
    );

INSERT IGNORE INTO tables (id, number, status) VALUES
  ('T001', 1, 'AVAILABLE'),
  ('T002', 2, 'AVAILABLE'),
  ('T003', 3, 'RESERVED'),
  ('T004', 4, 'OCCUPIED'),
  ('T005', 5, 'AVAILABLE');

-- Orders
INSERT IGNORE INTO orders (id, order_number, customer_id, room_id, total_amount, status, payment_status, order_type, special_instructions)
VALUES
  ('O001', 'R-1001', '11111111-1111-1111-1111-111111111111', 'T001', 205000, 'COMPLETED', 'PAID', 'DINE_IN', 'No spice'),
  ('O002', 'R-1002', '11111111-1111-1111-1111-111111111111', 'T002', 275000, 'IN_PROGRESS', 'PENDING', 'DINE_IN', 'Extra napkins'),
  ('O003', 'R-1003', '22222222-2222-2222-2222-222222222222', 'T003', 95000, 'NEW', 'PENDING', 'TAKEAWAY', NULL),
  ('O004', 'R-1004', '11111111-1111-1111-1111-111111111111', 'T004', 130000, 'CANCELLED', 'REFUNDED', 'DINE_IN', 'Customer cancelled');

-- Order items
INSERT IGNORE INTO order_items (id, order_id, menu_item_id, quantity, unit_price, total_price, special_instructions)
VALUES
  ('OI001', 'O001', 'M001', 1, 85000, 85000, NULL),
  ('OI002', 'O001', 'M004', 2, 60000, 120000, NULL),
  ('OI003', 'O002', 'M002', 1, 275000, 275000, 'Well done'),
  ('OI004', 'O003', 'M003', 1, 95000, 95000, NULL),
  ('OI005', 'O004', 'M001', 1, 85000, 85000, NULL),
  ('OI006', 'O004', 'M003', 1, 95000, 95000, NULL);
