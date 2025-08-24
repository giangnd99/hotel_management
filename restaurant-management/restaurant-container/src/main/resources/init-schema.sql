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
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_room_id ON orders(room_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_menu_items_category_id ON menu_items(category_id);
CREATE INDEX idx_menu_items_available ON menu_items(is_available);

-- Insert sample data for development
INSERT IGNORE INTO categories (id, name, description) VALUES
    (UUID(), 'Appetizers', 'Starters and small plates'),
    (UUID(), 'Main Courses', 'Primary dishes'),
    (UUID(), 'Desserts', 'Sweet endings'),
    (UUID(), 'Beverages', 'Drinks and refreshments');

-- Insert sample menu items
INSERT IGNORE INTO menu_items (id, name, description, price, category_id, preparation_time) VALUES
    (UUID(), 'Caesar Salad', 'Fresh romaine lettuce with Caesar dressing', 12.99,
     (SELECT id FROM categories WHERE name = 'Appetizers'), 10),
    (UUID(), 'Grilled Salmon', 'Fresh salmon with seasonal vegetables', 28.99,
     (SELECT id FROM categories WHERE name = 'Main Courses'), 25),
    (UUID(), 'Chocolate Cake', 'Rich chocolate cake with vanilla ice cream', 8.99,
     (SELECT id FROM categories WHERE name = 'Desserts'), 5),
    (UUID(), 'Fresh Orange Juice', 'Freshly squeezed orange juice', 4.99,
     (SELECT id FROM categories WHERE name = 'Beverages'), 3);