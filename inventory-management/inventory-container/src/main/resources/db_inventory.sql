
CREATE TABLE inventory_items (
    item_id INT PRIMARY KEY,
    item_name VARCHAR(255),
    category VARCHAR(100),
    quantity INT,
    unit_price DECIMAL(10,2),
    minimum_quantity INT
);

CREATE TABLE inventory_transaction (
    transaction_id INT PRIMARY KEY,
    item_id INT,
    staff_id INT,
    transaction_type VARCHAR(100),
    quantity INT,
    transaction_date DATETIME,
    FOREIGN KEY (item_id) REFERENCES inventory_items(item_id)
#     ,FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
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

INSERT INTO inventory_transaction (transaction_id, item_id, staff_id, transaction_type, quantity, transaction_date)
VALUES
    (1, 1, 101, 'Import', 50, '2025-06-01 09:30:00'),
    (2, 2, 102, 'Export', 20, '2025-06-01 10:15:00'),
    (3, 3, 103, 'Import', 30, '2025-06-02 08:45:00'),
    (4, 4, 104, 'Export', 10, '2025-06-02 11:00:00'),
    (5, 5, 105, 'Import', 100, '2025-06-03 14:20:00'),
    (6, 1, 101, 'Export', 15, '2025-06-03 16:10:00'),
    (7, 2, 102, 'Import', 40, '2025-06-04 09:00:00'),
    (8, 3, 103, 'Export', 5, '2025-06-04 12:30:00'),
    (9, 4, 104, 'Import', 60, '2025-06-05 08:00:00'),
    (10, 5, 105, 'Export', 25, '2025-06-05 15:45:00');
