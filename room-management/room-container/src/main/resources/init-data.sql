-- ==========================================================
-- INSERT dữ liệu Furniture (Nội thất)
-- ==========================================================
INSERT INTO furniture (name, price) VALUES
                                        ('Giường King Size', 5000000),
                                        ('Tivi 55 inch', 15000000),
                                        ('Bàn làm việc', 3000000),
                                        ('Minibar', 2000000),
                                        ('Ghế Sofa', 7000000),
                                        ('Máy lạnh trung tâm', 10000000),
                                        ('Bồn tắm Jacuzzi', 25000000),
                                        ('Bàn trang điểm', 2500000);

-- ==========================================================
-- INSERT dữ liệu Maintenance Types (Các loại bảo trì)
-- ==========================================================
INSERT INTO maintenance_types (name, description) VALUES
                                                      ('Điện', 'Sửa chữa hệ thống điện, đèn, ổ cắm'),
                                                      ('Nước', 'Xử lý rò rỉ nước, vòi sen, bồn cầu'),
                                                      ('Điều hòa', 'Bảo dưỡng hệ thống HVAC'),
                                                      ('Nội thất', 'Sửa chữa giường, tủ, sofa'),
                                                      ('Khác', 'Các vấn đề khác');

-- ==========================================================
-- INSERT dữ liệu Room Types (5 loại phòng)
-- ==========================================================
INSERT INTO room_types (type_name, description, base_price, max_occupancy) VALUES
                                                                               ('Standard Room', 'Phòng tiêu chuẩn 5 sao, nội thất cơ bản, thích hợp cho khách công tác.', 1500000, 2),
                                                                               ('Deluxe Room', 'Phòng Deluxe có view thành phố, trang trí hiện đại, có bàn làm việc.', 2500000, 2),
                                                                               ('Suite Room', 'Phòng Suite rộng rãi, có phòng khách riêng, view biển.', 4000000, 3),
                                                                               ('Executive Room', 'Phòng hạng Executive, bao gồm dịch vụ lounge và ưu tiên check-in.', 6000000, 3),
                                                                               ('Presidential Suite', 'Phòng Tổng thống sang trọng nhất, đầy đủ tiện nghi cao cấp.', 12000000, 4);

-- ==========================================================
-- INSERT dữ liệu Rooms (20 phòng)
-- ==========================================================
INSERT INTO rooms (room_id, room_type_id, room_number, floor, area, room_status) VALUES
                                                                                     (gen_random_uuid(), 1, '101', 1, '28m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 1, '102', 1, '28m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 1, '103', 1, '30m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 1, '104', 1, '30m2', 'VACANT'),

                                                                                     (gen_random_uuid(), 2, '201', 2, '35m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 2, '202', 2, '35m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 2, '203', 2, '38m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 2, '204', 2, '38m2', 'VACANT'),

                                                                                     (gen_random_uuid(), 3, '301', 3, '50m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 3, '302', 3, '52m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 3, '303', 3, '55m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 3, '304', 3, '55m2', 'VACANT'),

                                                                                     (gen_random_uuid(), 4, '401', 4, '65m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 4, '402', 4, '68m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 4, '403', 4, '70m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 4, '404', 4, '70m2', 'VACANT'),

                                                                                     (gen_random_uuid(), 5, '501', 5, '120m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 5, '502', 5, '125m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 5, '503', 5, '130m2', 'VACANT'),
                                                                                     (gen_random_uuid(), 5, '504', 5, '135m2', 'VACANT');

-- ==========================================================
-- INSERT dữ liệu Room Type Furniture (gắn nội thất cho từng loại phòng)
-- ==========================================================
INSERT INTO room_type_furniture (room_type_id, furniture_id, require_quantity) VALUES
                                                                                   (1, 1, 1), -- Standard: Giường King
                                                                                   (1, 2, 1), -- Tivi
                                                                                   (1, 3, 1), -- Bàn làm việc

                                                                                   (2, 1, 1),
                                                                                   (2, 2, 1),
                                                                                   (2, 3, 1),
                                                                                   (2, 4, 1), -- Minibar

                                                                                   (3, 1, 1),
                                                                                   (3, 2, 2),
                                                                                   (3, 5, 1),
                                                                                   (3, 4, 1),

                                                                                   (4, 1, 2),
                                                                                   (4, 2, 2),
                                                                                   (4, 5, 1),
                                                                                   (4, 6, 1), -- Máy lạnh trung tâm

                                                                                   (5, 1, 2),
                                                                                   (5, 2, 3),
                                                                                   (5, 5, 2),
                                                                                   (5, 6, 1),
                                                                                   (5, 7, 1), -- Bồn tắm jacuzzi
                                                                                   (5, 8, 1); -- Bàn trang điểm

-- ==========================================================
-- INSERT dữ liệu Guests
-- ==========================================================
INSERT INTO guests (id, first_name, last_name, full_name, phone, email, id_number, id_type, nationality, address, date_of_birth, gender, status, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'Nguyen', 'Van A', 'Nguyen Van A', '0905123456', 'vana@example.com', '123456789', 'CMND', 'Việt Nam', 'Hà Nội', '1990-05-15', 'Nam', 'ACTIVE', NOW(), NOW()),
    (gen_random_uuid(), 'Tran', 'Thi B', 'Tran Thi B', '0905234567', 'thib@example.com', '987654321', 'CCCD', 'Việt Nam', 'TP. Hồ Chí Minh', '1992-07-20', 'Nữ', 'ACTIVE', NOW(), NOW());

-- ==========================================================
-- ==========================================================
-- INSERT dữ liệu Room Services
-- ==========================================================
INSERT INTO room_services (service_id, room_number, guest_id, guest_name, service_type, service_name, description, quantity, unit_price, total_price, status, created_at, updated_at)
VALUES
    (gen_random_uuid(), '301', NULL, 'Nguyen Van A', 'Spa', 'Massage toàn thân', 'Dịch vụ spa cao cấp 60 phút', 1, 1500000, 1500000, 'PENDING', NOW(), NOW()),
    (gen_random_uuid(), '201', NULL, 'Tran Thi B', 'Dining', 'Buffet tối', 'Buffet quốc tế tại nhà hàng khách sạn', 2, 800000, 1600000, 'PENDING', NOW(), NOW());
