SET search_path TO room_management, public;
-- ============================================================================
-- FURNITURE
-- ============================================================================
INSERT INTO furniture (furniture_id, name, price) VALUES
                                                      ('ff894aa1-249f-4a68-b74f-33d5df2d2f96', 'Giường King Size', 8000000),
                                                      ('996de4bd-c201-4cd5-a530-6351bf4f4349', 'Tivi 55 inch', 12000000),
                                                      ('9b8056c3-0356-4ebe-b589-807dbd2deb40', 'Bàn làm việc', 4000000),
                                                      ('9b96a489-6573-448c-b448-219fc05b30d5', 'Minibar', 3000000),
                                                      ('6cd740d8-e8bc-4a13-8d5e-9cbe367b05a8', 'Ghế Sofa', 10000000),
                                                      ('6109a759-6ba5-42ca-8fea-43527475cf19', 'Máy lạnh trung tâm', 15000000),
                                                      ('c9646f26-1532-4317-b66c-5e369a1234ff', 'Bồn tắm Jacuzzi', 30000000),
                                                      ('1ce5ac6b-1e03-4009-85eb-070928aef168', 'Bàn trang điểm', 3500000);
-- ============================================================================
-- MAINTENANCE TYPES
-- ============================================================================
INSERT INTO maintenance_types (maintenance_type_id, name, description) VALUES
                                                                           ('f68f4a3a-5f0a-4e67-81ad-f81172d1c6b7', 'Điện', 'Sửa chữa hệ thống điện, đèn, ổ cắm'),
                                                                           ('2b09a793-cb0a-42ba-9e6e-dab39f759ba3', 'Nước', 'Xử lý rò rỉ nước, vòi sen, bồn cầu'),
                                                                           ('fad6c557-377c-4aa4-a202-2539a2eb2f9f', 'Điều hòa', 'Bảo dưỡng hệ thống HVAC'),
                                                                           ('c50fe01e-2c8d-4c34-bb27-3658fce93690', 'Nội thất', 'Sửa chữa giường, tủ, sofa'),
                                                                           ('afaac7ba-01f8-47d0-84fb-52a567fc07e8', 'Khác', 'Các vấn đề khác');
-- ============================================================================
-- ROOM TYPES (dựa trên JW Marriott Hanoi)
-- ============================================================================
INSERT INTO room_types (room_type_id, type_name, description, base_price, max_occupancy) VALUES
                                                                                             ('211ea872-2dca-47c4-9b4c-61afc060954e', 'Deluxe Room', 'Phòng Deluxe rộng rãi với view hồ hoặc thành phố, nội thất hiện đại, bàn làm việc và tiện nghi cao cấp.', 5000000, 2),
                                                                                             ('e56d39e1-e0e6-4a94-ae3e-f168366d26a0', 'Executive Room', 'Phòng Executive với quyền truy cập lounge, dịch vụ ưu tiên, view đẹp và không gian làm việc thoải mái.', 7000000, 2),
                                                                                             ('ed84f420-4b1d-4f58-bdcd-830a66d43384', 'Deluxe Suite', 'Suite Deluxe với phòng khách riêng, view hồ Tây, đầy đủ tiện nghi cho kỳ nghỉ dài ngày.', 10000000, 3),
                                                                                             ('0479d94c-9b4f-469e-b1a0-35780477a120', 'Executive Suite', 'Suite Executive cao cấp, bao gồm dịch vụ lounge, không gian rộng lớn và tiện nghi sang trọng.', 15000000, 3),
                                                                                             ('fefbd3ba-023e-44a0-a547-e50297ef2199', 'Presidential Suite', 'Suite Tổng thống đẳng cấp nhất, với view panoramic, phòng khách lớn, bếp riêng và dịch vụ cá nhân hóa.', 30000000, 4);
-- ============================================================================
-- ROOMS (20 phòng, dựa trên cấu trúc khách sạn thực tế)
-- ============================================================================
INSERT INTO rooms (room_id, room_type_id, room_number, floor, area, room_status, image_url) VALUES
                                                                                                ('fe0dc28a-654a-449c-84bd-f843d0544047', '211ea872-2dca-47c4-9b4c-61afc060954e', '101', 1, '48m2', 'VACANT', 'https://picsum.photos/200/300'),
                                                                                                ('737cedb6-830e-4e72-a659-fa74352f7258', '211ea872-2dca-47c4-9b4c-61afc060954e', '102', 1, '48m2', 'BOOKED', 'https://picsum.photos/200/300'),
                                                                                                ('7afb950f-c05b-4f5b-9fa2-30fe701e7f9f', '211ea872-2dca-47c4-9b4c-61afc060954e', '103', 1, '50m2', 'CHECKED_IN', 'https://picsum.photos/200/300'),
                                                                                                ('5894fe26-1199-4124-a8f4-d87467cbc73a', '211ea872-2dca-47c4-9b4c-61afc060954e', '104', 1, '50m2', 'MAINTENANCE', 'https://picsum.photos/200/300'),
                                                                                                ('4ce4e84b-06a1-4712-ab51-050dbbecfba5', 'e56d39e1-e0e6-4a94-ae3e-f168366d26a0', '201', 2, '55m2', 'CLEANING', 'https://picsum.photos/200/300'),
                                                                                                ('c63b64e0-c59e-4958-aef2-da05702427a1', 'e56d39e1-e0e6-4a94-ae3e-f168366d26a0', '202', 2, '55m2', 'CHECKED_OUT', 'https://picsum.photos/200/300'),
                                                                                                ('9d779fcc-09f9-4321-8c2b-f1b523554461', 'e56d39e1-e0e6-4a94-ae3e-f168366d26a0', '203', 2, '58m2', 'VACANT', 'https://picsum.photos/200/300'),
                                                                                                ('eea149f3-aba3-400d-a185-c177fea153aa', 'e56d39e1-e0e6-4a94-ae3e-f168366d26a0', '204', 2, '58m2', 'VACANT', 'https://picsum.photos/200/300'),
                                                                                                ('b7838df0-ac7d-40c4-8560-aa898597c45b', 'ed84f420-4b1d-4f58-bdcd-830a66d43384', '301', 3, '70m2', 'VACANT', 'https://picsum.photos/200/300'),
                                                                                                ('42fec22a-423a-4e99-a052-6d706002c045', 'ed84f420-4b1d-4f58-bdcd-830a66d43384', '302', 3, '72m2', 'VACANT', 'https://picsum.photos/200/300'),
                                                                                                ('d8fb7433-c94a-4799-a8ec-a2cd219eed55', 'ed84f420-4b1d-4f58-bdcd-830a66d43384', '303', 3, '75m2', 'VACANT', 'https://picsum.photos/200/300'),
                                                                                                ('b3b97adf-d7cd-4605-b271-ac1412066196', 'ed84f420-4b1d-4f58-bdcd-830a66d43384', '304', 3, '75m2', 'VACANT', 'https://picsum.photos/200/300'),
                                                                                                ('ab591c3e-c1bc-462d-8512-60a9ef261009', '0479d94c-9b4f-469e-b1a0-35780477a120', '401', 4, '85m2', 'VACANT', 'https://picsum.photos/200/300'),
                                                                                                ('9a0e26be-30a0-4fd8-a37e-6a61ce744567', '0479d94c-9b4f-469e-b1a0-35780477a120', '402', 4, '88m2', 'VACANT', 'https://picsum.photos/200/300'),
                                                                                                ('4434b747-bd73-44b8-9ade-070f97a60302', '0479d94c-9b4f-469e-b1a0-35780477a120', '403', 4, '90m2', 'VACANT', 'https://picsum.photos/200/300'),
                                                                                                ('2519114c-0991-4c48-af11-ec43ecd521a7', '0479d94c-9b4f-469e-b1a0-35780477a120', '404', 4, '90m2', 'VACANT', 'https://picsum.photos/200/300'),
                                                                                                ('2ac1d74b-f06f-445f-bf91-acdde9893f97', 'fefbd3ba-023e-44a0-a547-e50297ef2199', '501', 5, '150m2', 'VACANT', 'https://picsum.photos/200/300'),
                                                                                                ('998ff04a-e8f8-4c68-acce-f903f6f44235', 'fefbd3ba-023e-44a0-a547-e50297ef2199', '502', 5, '155m2', 'VACANT', 'https://picsum.photos/200/300'),
                                                                                                ('c263cd1a-3269-4329-bfbf-67beb61ea2c6', 'fefbd3ba-023e-44a0-a547-e50297ef2199', '503', 5, '160m2', 'VACANT', 'https://picsum.photos/200/300'),
                                                                                                ('9796916f-8d91-4c1c-bd40-90b8f5cf7fbc', 'fefbd3ba-023e-44a0-a547-e50297ef2199', '504', 5, '165m2', 'VACANT', 'https://picsum.photos/200/300');
-- ============================================================================
-- ROOM TYPE FURNITURE
-- ============================================================================
-- Deluxe Room
INSERT INTO room_type_furniture (room_type_furniture_id, room_type_id, furniture_id, require_quantity) VALUES
                                                                                                           ('15f6380a-690d-4ed1-83c2-ca934d1e5fb7', '211ea872-2dca-47c4-9b4c-61afc060954e', 'ff894aa1-249f-4a68-b74f-33d5df2d2f96', 1), -- Giường King Size
                                                                                                           ('50b2df57-4141-4610-9b50-28c0a37ec686', '211ea872-2dca-47c4-9b4c-61afc060954e', '996de4bd-c201-4cd5-a530-6351bf4f4349', 1), -- Tivi 55 inch
                                                                                                           ('dfbe3d98-b5ad-4885-aaa4-319739174fc3', '211ea872-2dca-47c4-9b4c-61afc060954e', '9b8056c3-0356-4ebe-b589-807dbd2deb40', 1), -- Bàn làm việc
                                                                                                           ('5f89e5d0-946c-4a6f-83bc-fea82db7bf6b', '211ea872-2dca-47c4-9b4c-61afc060954e', '9b96a489-6573-448c-b448-219fc05b30d5', 1); -- Minibar
-- Executive Room
INSERT INTO room_type_furniture (room_type_furniture_id, room_type_id, furniture_id, require_quantity) VALUES
                                                                                                           ('4be49fbe-6d0d-4cc6-8107-9d842ffea36a', 'e56d39e1-e0e6-4a94-ae3e-f168366d26a0', 'ff894aa1-249f-4a68-b74f-33d5df2d2f96', 1),
                                                                                                           ('4af81081-498b-4bfc-bc72-78e410bc1b3d', 'e56d39e1-e0e6-4a94-ae3e-f168366d26a0', '996de4bd-c201-4cd5-a530-6351bf4f4349', 1),
                                                                                                           ('cae7f09e-dc7c-4cda-9b7f-563c9b7c7c57', 'e56d39e1-e0e6-4a94-ae3e-f168366d26a0', '9b8056c3-0356-4ebe-b589-807dbd2deb40', 1),
                                                                                                           ('cad63f85-a0ba-4cb1-abff-281ecc32652b', 'e56d39e1-e0e6-4a94-ae3e-f168366d26a0', '9b96a489-6573-448c-b448-219fc05b30d5', 1),
                                                                                                           ('b39ea863-0f01-4aeb-bbd5-d96c274ad067', 'e56d39e1-e0e6-4a94-ae3e-f168366d26a0', '6cd740d8-e8bc-4a13-8d5e-9cbe367b05a8', 1); -- Ghế Sofa
-- Deluxe Suite
INSERT INTO room_type_furniture (room_type_furniture_id, room_type_id, furniture_id, require_quantity) VALUES
                                                                                                           ('b5675e2a-5ec0-4150-bf85-1adb874f4d00', 'ed84f420-4b1d-4f58-bdcd-830a66d43384', 'ff894aa1-249f-4a68-b74f-33d5df2d2f96', 1),
                                                                                                           ('f1c23540-12d8-495a-94b1-23769524996c', 'ed84f420-4b1d-4f58-bdcd-830a66d43384', '996de4bd-c201-4cd5-a530-6351bf4f4349', 2),
                                                                                                           ('1446249b-910a-4b29-8f01-8f6b294860fb', 'ed84f420-4b1d-4f58-bdcd-830a66d43384', '6cd740d8-e8bc-4a13-8d5e-9cbe367b05a8', 1),
                                                                                                           ('9403dc51-53da-445c-8e9b-98b62339f469', 'ed84f420-4b1d-4f58-bdcd-830a66d43384', '9b96a489-6573-448c-b448-219fc05b30d5', 1),
                                                                                                           ('cb36dc68-2426-4106-9a3e-35e2522787ca', 'ed84f420-4b1d-4f58-bdcd-830a66d43384', '6109a759-6ba5-42ca-8fea-43527475cf19', 1); -- Máy lạnh trung tâm
-- Executive Suite
INSERT INTO room_type_furniture (room_type_furniture_id, room_type_id, furniture_id, require_quantity) VALUES
                                                                                                           ('ebf820d9-2b09-4fd1-86f0-4ee4892325b2', '0479d94c-9b4f-469e-b1a0-35780477a120', 'ff894aa1-249f-4a68-b74f-33d5df2d2f96', 2),
                                                                                                           ('68463114-78f1-488c-afa6-d1241d93d8f9', '0479d94c-9b4f-469e-b1a0-35780477a120', '996de4bd-c201-4cd5-a530-6351bf4f4349', 2),
                                                                                                           ('7fff75d4-e630-4a82-8436-2376b7c3d15b', '0479d94c-9b4f-469e-b1a0-35780477a120', '6cd740d8-e8bc-4a13-8d5e-9cbe367b05a8', 1),
                                                                                                           ('57672da2-8251-46f4-b631-125fd91e22bc', '0479d94c-9b4f-469e-b1a0-35780477a120', '6109a759-6ba5-42ca-8fea-43527475cf19', 1),
                                                                                                           ('2f0ab2ff-959e-4072-9279-c7511dc0ed2c', '0479d94c-9b4f-469e-b1a0-35780477a120', 'c9646f26-1532-4317-b66c-5e369a1234ff', 1); -- Bồn tắm Jacuzzi
-- Presidential Suite
INSERT INTO room_type_furniture (room_type_furniture_id, room_type_id, furniture_id, require_quantity) VALUES
                                                                                                           ('4f970cc0-ddc9-4778-a34b-76ded6e5dc52', 'fefbd3ba-023e-44a0-a547-e50297ef2199', 'ff894aa1-249f-4a68-b74f-33d5df2d2f96', 2),
                                                                                                           ('1649fffc-1259-4dec-9e4a-cab1efcf7240', 'fefbd3ba-023e-44a0-a547-e50297ef2199', '996de4bd-c201-4cd5-a530-6351bf4f4349', 3),
                                                                                                           ('dba191af-1a12-448c-b88b-e172ca0659b2', 'fefbd3ba-023e-44a0-a547-e50297ef2199', '6cd740d8-e8bc-4a13-8d5e-9cbe367b05a8', 2),
                                                                                                           ('bd2c6626-31a3-4121-ab57-6f8ba0295664', 'fefbd3ba-023e-44a0-a547-e50297ef2199', '6109a759-6ba5-42ca-8fea-43527475cf19', 1),
                                                                                                           ('87bd878f-3b62-491d-b974-67c3972bdfe3', 'fefbd3ba-023e-44a0-a547-e50297ef2199', 'c9646f26-1532-4317-b66c-5e369a1234ff', 1),
                                                                                                           ('54f248f3-988c-40a2-bbe9-3f8c90ad9e6b', 'fefbd3ba-023e-44a0-a547-e50297ef2199', '1ce5ac6b-1e03-4009-85eb-070928aef168', 1); -- Bàn trang điểm
-- ============================================================================
-- GUESTS
-- ============================================================================
INSERT INTO guests (id, first_name, last_name, full_name, phone, email, id_number, id_type, nationality, address, date_of_birth, gender, status, created_at, updated_at)
VALUES
    ('9e22cea5-0d86-4da8-8a67-32e89401e211', 'Nguyễn', 'Văn A', 'Nguyễn Văn A', '0905123456', 'vana@example.com', '123456789', 'CMND', 'Việt Nam', 'Hà Nội', '1990-05-15', 'Nam', 'ACTIVE', NOW(), NOW()),
    ('9251a049-1f01-430d-8802-ff9b930415b1', 'Trần', 'Thị B', 'Trần Thị B', '0905234567', 'thib@example.com', '987654321', 'CCCD', 'Việt Nam', 'TP. Hồ Chí Minh', '1992-07-20', 'Nữ', 'ACTIVE', NOW(), NOW());
-- ============================================================================
-- ROOM SERVICES
-- ============================================================================
INSERT INTO room_services (service_id, room_number, guest_id, guest_name, service_type, service_name, description, quantity, unit_price, total_price, status, created_at, updated_at)
VALUES
    ('24daa207-72d1-4dd9-a62c-0a22cc8de70d', '301', '9e22cea5-0d86-4da8-8a67-32e89401e211', 'Nguyễn Văn A', 'Spa', 'Massage toàn thân', 'Dịch vụ spa cao cấp 60 phút với tinh dầu thiên nhiên', 1, 2000000, 2000000, 'PENDING', NOW(), NOW()),
    ('1467984f-9600-45fa-859d-fb3c46321700', '201', '9251a049-1f01-430d-8802-ff9b930415b1', 'Trần Thị B', 'Dining', 'Buffet tối', 'Buffet quốc tế tại nhà hàng khách sạn với hải sản tươi', 2, 1000000, 2000000, 'PENDING', NOW(), NOW());
-- ============================================================================
-- ROOM MAINTENANCE (thêm dữ liệu mẫu sát thực tế)
-- ============================================================================
INSERT INTO room_maintenance (id, room_id, maintenance_type_id, room_number, issue_type, priority, status, description, notes, requested_by, assigned_to, requested_at, scheduled_at, started_at, completed_at, estimated_cost, actual_cost, is_urgent, special_instructions, created_at, updated_at)
VALUES
    ('a1b2c3d4-e5f6-4a78-9b10-c1d2e3f4a5b6',
     'fe0dc28a-654a-449c-84bd-f843d0544047', -- room_id của phòng 101 (Deluxe Room)
     'f68f4a3a-5f0a-4e67-81ad-f81172d1c6b7', -- maintenance_type_id 'Điện'
     '101', 'ELECTRICAL', 'MEDIUM', 'REQUESTED', 'Đèn phòng khách bị chập chờn', 'Kiểm tra hệ thống dây điện', 'Khách hàng A', 'Nhân viên kỹ thuật 1',
     '2025-08-20 10:00:00', '2025-08-21 09:00:00', NULL, NULL, 500000, NULL, FALSE, 'Mang theo dụng cụ an toàn', NOW(), NOW()),
    ('b1c2d3e4-f5a6-4b78-9c10-d1e2f3a4b5c6',
     '5894fe26-1199-4124-a8f4-d87467cbc73a', -- room_id của phòng 104 (Deluxe Room, đang MAINTENANCE)
     '2b09a793-cb0a-42ba-9e6e-dab39f759ba3', -- maintenance_type_id 'Nước'
     '104', 'PLUMBING', 'HIGH', 'IN_PROGRESS', 'Vòi sen bị rò rỉ nước', 'Thay thế linh kiện', 'Quản lý tầng', 'Nhân viên kỹ thuật 2',
     '2025-08-21 14:00:00', '2025-08-22 10:00:00', '2025-08-22 11:00:00', NULL, 1000000, 800000, TRUE, 'Đảm bảo không làm ướt sàn', NOW(), NOW());
-- ============================================================================
-- CHECK_INS (thêm dữ liệu mẫu, tham chiếu guest và room)
-- ============================================================================
INSERT INTO check_ins (id, booking_id, guest_id, room_id, room_number, check_in_date, check_out_date, check_in_time, check_out_time, number_of_guests, special_requests, status, checked_in_by, checked_out_by, notes, created_at, updated_at)
VALUES
    ('c1d2e3f4-a5b6-4c78-9d10-e1f2a3b4c5d6',
     NULL, -- booking_id có thể NULL nếu không có
     '9e22cea5-0d86-4da8-8a67-32e89401e211', -- guest_id của Nguyễn Văn A
     '7afb950f-c05b-4f5b-9fa2-30fe701e7f9f', -- room_id của phòng 103 (CHECKED_IN)
     '103', '2025-08-20', '2025-08-25', '2025-08-20 14:00:00', NULL, 2, 'Yêu cầu giường phụ', 'ACTIVE', 'Lễ tân 1', NULL, 'Khách VIP', NOW(), NOW()),
    ('d1e2f3a4-b5c6-4d78-9e10-f1a2b3c4d5e6',
     NULL,
     '9251a049-1f01-430d-8802-ff9b930415b1', -- guest_id của Trần Thị B
     'c63b64e0-c59e-4958-aef2-da05702427a1', -- room_id của phòng 202 (CHECKED_OUT)
     '202', '2025-08-15', '2025-08-20', '2025-08-15 15:00:00', '2025-08-20 11:00:00', 1, 'Không hút thuốc', 'COMPLETED', 'Lễ tân 2', 'Lễ tân 3', 'Thanh toán đầy đủ', NOW(), NOW());
-- ============================================================================
-- ROOM CLEANING (thêm dữ liệu mẫu)
-- ============================================================================
INSERT INTO room_cleaning (id, room_id, room_number, cleaning_type, priority, status, description, notes, requested_by, assigned_to, requested_at, scheduled_at, started_at, completed_at, is_urgent, special_instructions, created_at, updated_at)
VALUES
    ('e1f2a3b4-c5d6-4e78-9f10-a1b2c3d4e5f6',
     '4ce4e84b-06a1-4712-ab51-050dbbecfba5', -- room_id của phòng 201 (CLEANING)
     '201', 'DAILY', 'MEDIUM', 'IN_PROGRESS', 'Dọn dẹp hàng ngày, thay khăn', 'Bụi nhiều ở bàn làm việc', 'Khách hàng B', 'Nhân viên dọn phòng 1',
     '2025-08-22 08:00:00', '2025-08-22 09:00:00', '2025-08-22 09:30:00', NULL, FALSE, 'Sử dụng chất tẩy tự nhiên', NOW(), NOW()),
    ('f1a2b3c4-d5e6-4f78-9a10-b1c2d3e4f5a6',
     'c63b64e0-c59e-4958-aef2-da05702427a1', -- room_id của phòng 202 (CHECKED_OUT)
     '202', 'POST_CHECK_OUT', 'HIGH', 'PENDING', 'Dọn sâu sau check-out', 'Kiểm tra đồ đạc bỏ quên', 'Quản lý tầng', 'Nhân viên dọn phòng 2',
     '2025-08-20 12:00:00', '2025-08-20 13:00:00', NULL, NULL, TRUE, 'Khử trùng toàn bộ phòng', NOW(), NOW());