
CREATE TABLE inventory_items (
    item_id INT PRIMARY KEY,
    item_name VARCHAR(255),
    category VARCHAR(100),
    quantity INT,
    unit_price DECIMAL(10,2),
    minimum_quantity INT
);

INSERT INTO inventory_items (item_id, item_name, category, quantity, unit_price, minimum_quantity) VALUES
    (1, 'Khăn tắm', 'Vật dụng phòng', 150, 35000.00, 50),
    (2, 'Dầu gội', 'Tiêu hao', 200, 12000.00, 50),
    (3, 'Bàn chải đánh răng', 'Tiêu hao', 250, 5000.00, 80),
    (4, 'Chăn mền', 'Vật dụng phòng', 50, 200000.00, 20),
    (5, 'Nước suối 500ml', 'Tiêu hao', 300, 7000.00, 100),
    (6, 'Tivi Sony 43\"', 'Thiết bị điện tử', 10, 5500000.00, 2),
    (7, 'Bình đun siêu tốc', 'Thiết bị điện', 30, 350000.00, 5),
    (8, 'Ly thủy tinh', 'Vật dụng nhà bếp', 100, 15000.00, 30),
    (9, 'Dép đi trong phòng', 'Tiêu hao', 180, 8000.00, 60),
    (10, 'Máy sấy tóc', 'Thiết bị điện', 25, 450000.00, 5);
