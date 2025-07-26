CREATE DATABASE IF NOT EXISTS db_restaurant;
USE db_restaurant;

CREATE TABLE menu_items (
    menu_item_id INT PRIMARY KEY,
    item_name VARCHAR(100),
    description TEXT,
    price DECIMAL(10,2),
    category VARCHAR(50),
    quantity INT
);

CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    order_date DATETIME,
    total_price DECIMAL(10,2),
    status VARCHAR(50)
);

CREATE TABLE order_items (
    order_item_id INT,
    menu_item_id INT,
    order_id INT,
    quantity INT,
    unit_price DECIMAL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(menu_item_id)
    );

-- Menu Items
INSERT INTO `menu_items` (`menu_item_id`, `item_name`, `description`, `price`, `category`, `quantity`) VALUES
    (101, 'Phở bò', 'Phở bò truyền thống', 150000, 'Món chính', 100),
    (102, 'Trà đá', 'Trà đá mát lạnh', 25000, 'Đồ uống', 200),
    (103, 'Cà phê sữa', 'Cà phê pha với sữa đặc', 90000, 'Đồ uống', 150);

-- Restaurant Orders
INSERT INTO `orders` (`order_id`, `customer_id`, `order_date`, `total_price`, `status`) VALUES
    (1, 1001, '2025-07-15 10:00:00', 110000, 'COMPLETED'),
    (2, 1002, '2025-07-15 11:00:00', 20000, 'IN_PROGRESS');

-- Order Items
INSERT INTO `order_items` (`order_item_id`, `menu_item_id`, `order_id`, `quantity`, `unit_price`) VALUES
    (1, 101, 1, 2, 50000),
    (2, 102, 1, 2, 5000),
    (3, 103, 2, 1, 20000);
