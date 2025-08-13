-- Create schema for restaurant service
CREATE SCHEMA IF NOT EXISTS restaurant;

-- Set search path to restaurant schema
SET search_path TO restaurant, public;

-- Create restaurant schema for test environment
CREATE SCHEMA IF NOT EXISTS restaurant_test;

-- Create tables for restaurant service
-- Note: These are basic tables, you may need to adjust based on your actual domain entities

-- Categories table
CREATE TABLE IF NOT EXISTS restaurant.categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Menu items table
CREATE TABLE IF NOT EXISTS restaurant.menu_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category_id UUID REFERENCES restaurant.categories(id),
    is_available BOOLEAN DEFAULT true,
    image_url VARCHAR(500),
    preparation_time INTEGER, -- in minutes
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders table
CREATE TABLE IF NOT EXISTS restaurant.orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_number VARCHAR(50) UNIQUE NOT NULL,
    customer_id VARCHAR(100) NOT NULL,
    room_id VARCHAR(100),
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) DEFAULT 'NEW',
    payment_status VARCHAR(50) DEFAULT 'PENDING',
    order_type VARCHAR(50) DEFAULT 'DINE_IN', -- DINE_IN, ROOM_SERVICE, TAKEAWAY
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Order items table
CREATE TABLE IF NOT EXISTS restaurant.order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID REFERENCES restaurant.orders(id) ON DELETE CASCADE,
    menu_item_id UUID REFERENCES restaurant.menu_items(id),
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_orders_customer_id ON restaurant.orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_orders_room_id ON restaurant.orders(room_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON restaurant.orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON restaurant.orders(created_at);
CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON restaurant.order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_menu_items_category_id ON restaurant.menu_items(category_id);
CREATE INDEX IF NOT EXISTS idx_menu_items_available ON restaurant.menu_items(is_available);

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION restaurant.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_categories_updated_at 
    BEFORE UPDATE ON restaurant.categories 
    FOR EACH ROW EXECUTE FUNCTION restaurant.update_updated_at_column();

CREATE TRIGGER update_menu_items_updated_at 
    BEFORE UPDATE ON restaurant.menu_items 
    FOR EACH ROW EXECUTE FUNCTION restaurant.update_updated_at_column();

CREATE TRIGGER update_orders_updated_at 
    BEFORE UPDATE ON restaurant.orders 
    FOR EACH ROW EXECUTE FUNCTION restaurant.update_updated_at_column();

-- Insert sample data for development
INSERT INTO restaurant.categories (name, description) VALUES
    ('Appetizers', 'Starters and small plates'),
    ('Main Courses', 'Primary dishes'),
    ('Desserts', 'Sweet endings'),
    ('Beverages', 'Drinks and refreshments')
ON CONFLICT DO NOTHING;

-- Insert sample menu items
INSERT INTO restaurant.menu_items (name, description, price, category_id, preparation_time) VALUES
    ('Caesar Salad', 'Fresh romaine lettuce with Caesar dressing', 12.99, 
     (SELECT id FROM restaurant.categories WHERE name = 'Appetizers'), 10),
    ('Grilled Salmon', 'Fresh salmon with seasonal vegetables', 28.99, 
     (SELECT id FROM restaurant.categories WHERE name = 'Main Courses'), 25),
    ('Chocolate Cake', 'Rich chocolate cake with vanilla ice cream', 8.99, 
     (SELECT id FROM restaurant.categories WHERE name = 'Desserts'), 5),
    ('Fresh Orange Juice', 'Freshly squeezed orange juice', 4.99, 
     (SELECT id FROM restaurant.categories WHERE name = 'Beverages'), 3)
ON CONFLICT DO NOTHING;
