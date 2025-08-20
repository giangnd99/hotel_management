-- =====================================================================================================================
-- ROOM MANAGEMENT INITIAL DATA SCRIPT
-- Khách sạn 5 sao Việt Nam - Dữ liệu mẫu
-- =====================================================================================================================

-- Sử dụng schema room_management
SET search_path TO room_management, public;

-- =====================================================================================================================
-- MAINTENANCE TYPES (Loại bảo trì)
-- =====================================================================================================================

INSERT INTO maintenance_types (name, description) VALUES
('PLUMBING', 'Bảo trì hệ thống nước, vòi sen, bồn tắm'),
('ELECTRICAL', 'Bảo trì hệ thống điện, ổ cắm, công tắc'),
('HVAC', 'Bảo trì điều hòa không khí, thông gió'),
('STRUCTURAL', 'Bảo trì kết cấu, tường, cửa, sàn'),
('APPLIANCE', 'Bảo trì thiết bị điện tử, TV, tủ lạnh'),
('OTHER', 'Các loại bảo trì khác');

-- =====================================================================================================================
-- FURNITURE (Nội thất)
-- =====================================================================================================================

INSERT INTO furniture (name, price) VALUES
-- Nội thất cơ bản
('Giường King Size', 15000000),
('Giường Queen Size', 12000000),
('Giường Single', 8000000),
('Nệm cao su', 5000000),
('Gối cao su', 800000),
('Chăn bông', 1200000),
('Ga giường', 500000),
('Tủ đầu giường', 3000000),
('Tủ quần áo', 8000000),
('Bàn làm việc', 4000000),
('Ghế làm việc', 2500000),
('Sofa phòng khách', 12000000),
('Bàn trà', 3000000),
('Tủ lạnh mini', 8000000),
('TV 55 inch', 15000000),
('TV 43 inch', 10000000),
('Điều hòa', 12000000),
('Quạt trần', 3000000),
('Đèn ngủ', 500000),
('Đèn chính', 800000),
('Thảm sàn', 2000000),
('Rèm cửa', 1500000),
('Gương trang điểm', 2000000),
('Bàn trang điểm', 4000000),
('Ghế trang điểm', 1500000),
('Tủ phòng tắm', 3000000),
('Bồn tắm', 8000000),
('Vòi sen', 2000000),
('Bồn rửa mặt', 3000000),
('Bồn cầu', 4000000),
('Máy sấy tóc', 800000),
('Khăn tắm', 200000),
('Đồ vệ sinh', 500000);

-- =====================================================================================================================
-- ROOM TYPES (Loại phòng)
-- =====================================================================================================================

INSERT INTO room_types (type_name, description, base_price, max_occupancy) VALUES
('Standard Room', 'Phòng tiêu chuẩn với đầy đủ tiện nghi cơ bản, phù hợp cho khách du lịch và công tác', 1200000, 2),
('Deluxe Room', 'Phòng cao cấp với không gian rộng rãi, view đẹp và nội thất sang trọng', 1800000, 3),
('Suite Room', 'Phòng suite với phòng khách riêng biệt, phù hợp cho gia đình hoặc khách VIP', 2800000, 4),
('Presidential Suite', 'Phòng tổng thống với không gian sang trọng bậc nhất, dịch vụ đặc biệt', 5800000, 6);

-- =====================================================================================================================
-- ROOM TYPE FURNITURE (Yêu cầu nội thất cho loại phòng)
-- =====================================================================================================================

-- Standard Room Furniture
INSERT INTO room_type_furniture (room_type_id, furniture_id, required_quantity) VALUES
(1, 2, 1),  -- Giường Queen Size
(1, 5, 1),  -- Nệm cao su
(1, 6, 2),  -- Gối cao su
(1, 7, 1),  -- Chăn bông
(1, 8, 1),  -- Ga giường
(1, 9, 1),  -- Tủ đầu giường
(1, 10, 1), -- Tủ quần áo
(1, 11, 1), -- Bàn làm việc
(1, 12, 1), -- Ghế làm việc
(1, 16, 1), -- TV 43 inch
(1, 17, 1), -- Điều hòa
(1, 19, 1), -- Đèn ngủ
(1, 20, 1), -- Đèn chính
(1, 21, 1), -- Thảm sàn
(1, 22, 1), -- Rèm cửa
(1, 23, 1), -- Gương trang điểm
(1, 24, 1), -- Bàn trang điểm
(1, 25, 1), -- Ghế trang điểm
(1, 26, 1), -- Tủ phòng tắm
(1, 28, 1), -- Vòi sen
(1, 29, 1), -- Bồn rửa mặt
(1, 30, 1), -- Bồn cầu
(1, 31, 1), -- Máy sấy tóc
(1, 32, 2), -- Khăn tắm
(1, 33, 1); -- Đồ vệ sinh

-- Deluxe Room Furniture
INSERT INTO room_type_furniture (room_type_id, furniture_id, required_quantity) VALUES
(2, 1, 1),  -- Giường King Size
(2, 5, 1),  -- Nệm cao su
(2, 6, 2),  -- Gối cao su
(2, 7, 1),  -- Chăn bông
(2, 8, 1),  -- Ga giường
(2, 9, 1),  -- Tủ đầu giường
(2, 10, 1), -- Tủ quần áo
(2, 11, 1), -- Bàn làm việc
(2, 12, 1), -- Ghế làm việc
(2, 13, 1), -- Sofa phòng khách
(2, 14, 1), -- Bàn trà
(2, 15, 1), -- Tủ lạnh mini
(2, 16, 1), -- TV 55 inch
(2, 17, 1), -- Điều hòa
(2, 18, 1), -- Quạt trần
(2, 19, 1), -- Đèn ngủ
(2, 20, 1), -- Đèn chính
(2, 21, 1), -- Thảm sàn
(2, 22, 1), -- Rèm cửa
(2, 23, 1), -- Gương trang điểm
(2, 24, 1), -- Bàn trang điểm
(2, 25, 1), -- Ghế trang điểm
(2, 26, 1), -- Tủ phòng tắm
(2, 27, 1), -- Bồn tắm
(2, 28, 1), -- Vòi sen
(2, 29, 1), -- Bồn rửa mặt
(2, 30, 1), -- Bồn cầu
(2, 31, 1), -- Máy sấy tóc
(2, 32, 3), -- Khăn tắm
(2, 33, 1); -- Đồ vệ sinh

-- Suite Room Furniture
INSERT INTO room_type_furniture (room_type_id, furniture_id, required_quantity) VALUES
(3, 1, 1),  -- Giường King Size
(3, 2, 1),  -- Giường Queen Size (phòng phụ)
(3, 5, 2),  -- Nệm cao su
(3, 6, 4),  -- Gối cao su
(3, 7, 2),  -- Chăn bông
(3, 8, 2),  -- Ga giường
(3, 9, 2),  -- Tủ đầu giường
(3, 10, 2), -- Tủ quần áo
(3, 11, 2), -- Bàn làm việc
(3, 12, 2), -- Ghế làm việc
(3, 13, 2), -- Sofa phòng khách
(3, 14, 1), -- Bàn trà
(3, 15, 1), -- Tủ lạnh mini
(3, 16, 2), -- TV 55 inch
(3, 17, 2), -- Điều hòa
(3, 18, 1), -- Quạt trần
(3, 19, 2), -- Đèn ngủ
(3, 20, 2), -- Đèn chính
(3, 21, 1), -- Thảm sàn
(3, 22, 1), -- Rèm cửa
(3, 23, 2), -- Gương trang điểm
(3, 24, 2), -- Bàn trang điểm
(3, 25, 2), -- Ghế trang điểm
(3, 26, 2), -- Tủ phòng tắm
(3, 27, 2), -- Bồn tắm
(3, 28, 2), -- Vòi sen
(3, 29, 2), -- Bồn rửa mặt
(3, 30, 2), -- Bồn cầu
(3, 31, 2), -- Máy sấy tóc
(3, 32, 6), -- Khăn tắm
(3, 33, 2); -- Đồ vệ sinh

-- Presidential Suite Furniture
INSERT INTO room_type_furniture (room_type_id, furniture_id, required_quantity) VALUES
(4, 1, 2),  -- Giường King Size
(4, 2, 2),  -- Giường Queen Size
(4, 5, 4),  -- Nệm cao su
(4, 6, 8),  -- Gối cao su
(4, 7, 4),  -- Chăn bông
(4, 8, 4),  -- Ga giường
(4, 9, 4),  -- Tủ đầu giường
(4, 10, 4), -- Tủ quần áo
(4, 11, 4), -- Bàn làm việc
(4, 12, 4), -- Ghế làm việc
(4, 13, 4), -- Sofa phòng khách
(4, 14, 2), -- Bàn trà
(4, 15, 2), -- Tủ lạnh mini
(4, 16, 4), -- TV 55 inch
(4, 17, 4), -- Điều hòa
(4, 18, 2), -- Quạt trần
(4, 19, 4), -- Đèn ngủ
(4, 20, 4), -- Đèn chính
(4, 21, 2), -- Thảm sàn
(4, 22, 2), -- Rèm cửa
(4, 23, 4), -- Gương trang điểm
(4, 24, 4), -- Bàn trang điểm
(4, 25, 4), -- Ghế trang điểm
(4, 26, 4), -- Tủ phòng tắm
(4, 27, 4), -- Bồn tắm
(4, 28, 4), -- Vòi sen
(4, 29, 4), -- Bồn rửa mặt
(4, 30, 4), -- Bồn cầu
(4, 31, 4), -- Máy sấy tóc
(4, 32, 12),-- Khăn tắm
(4, 33, 4); -- Đồ vệ sinh

-- =====================================================================================================================
-- ROOMS (Phòng)
-- =====================================================================================================================

-- Tầng 1 - Standard Rooms
INSERT INTO rooms (room_number, floor, room_type_id, room_status, area) VALUES
('101', 1, 1, 'VACANT', '25m²'),
('102', 1, 1, 'VACANT', '25m²'),
('103', 1, 1, 'VACANT', '25m²'),
('104', 1, 1, 'VACANT', '25m²'),
('105', 1, 1, 'VACANT', '25m²');

-- Tầng 2 - Standard & Deluxe Rooms
INSERT INTO rooms (room_number, floor, room_type_id, room_status, area) VALUES
('201', 2, 1, 'VACANT', '25m²'),
('202', 2, 1, 'VACANT', '25m²'),
('203', 2, 2, 'VACANT', '35m²'),
('204', 2, 2, 'VACANT', '35m²'),
('205', 2, 1, 'VACANT', '25m²');

-- Tầng 3 - Deluxe & Suite Rooms
INSERT INTO rooms (room_number, floor, room_type_id, room_status, area) VALUES
('301', 3, 2, 'VACANT', '35m²'),
('302', 3, 2, 'VACANT', '35m²'),
('303', 3, 3, 'VACANT', '50m²'),
('304', 3, 3, 'VACANT', '50m²'),
('305', 3, 2, 'VACANT', '35m²');

-- Tầng 4 - Suite & Presidential Rooms
INSERT INTO rooms (room_number, floor, room_type_id, room_status, area) VALUES
('401', 4, 3, 'VACANT', '50m²'),
('402', 4, 3, 'VACANT', '50m²'),
('403', 4, 4, 'VACANT', '120m²'),
('404', 4, 3, 'VACANT', '50m²'),
('405', 4, 3, 'VACANT', '50m²');

-- =====================================================================================================================
-- GUESTS (Khách hàng mẫu)
-- =====================================================================================================================

INSERT INTO guests (first_name, last_name, full_name, phone, email, id_number, id_type, nationality, address, date_of_birth, gender, special_requests, status) VALUES
('Nguyễn', 'Văn An', 'Nguyễn Văn An', '0901234567', 'nguyenvanan@email.com', '123456789012', 'CCCD', 'Việt Nam', '123 Đường ABC, Quận 1, TP.HCM', '1985-03-15', 'Nam', 'Không có yêu cầu đặc biệt', 'ACTIVE'),
('Trần', 'Thị Bình', 'Trần Thị Bình', '0901234568', 'tranthibinh@email.com', '123456789013', 'CCCD', 'Việt Nam', '456 Đường XYZ, Quận 3, TP.HCM', '1990-07-22', 'Nữ', 'Phòng không có mùi thuốc lá', 'ACTIVE'),
('Lê', 'Văn Cường', 'Lê Văn Cường', '0901234569', 'levancuong@email.com', '123456789014', 'CCCD', 'Việt Nam', '789 Đường DEF, Quận 5, TP.HCM', '1988-11-08', 'Nam', 'Phòng view biển', 'ACTIVE'),
('Phạm', 'Thị Dung', 'Phạm Thị Dung', '0901234570', 'phamthidung@email.com', '123456789015', 'CCCD', 'Việt Nam', '321 Đường GHI, Quận 7, TP.HCM', '1992-05-12', 'Nữ', 'Phòng yên tĩnh', 'ACTIVE'),
('Hoàng', 'Văn Em', 'Hoàng Văn Em', '0901234571', 'hoangvanem@email.com', '123456789016', 'CCCD', 'Việt Nam', '654 Đường JKL, Quận 10, TP.HCM', '1987-09-30', 'Nam', 'Phòng có ban công', 'ACTIVE'),
('John', 'Smith', 'John Smith', '0901234572', 'johnsmith@email.com', 'AB123456', 'Passport', 'Hoa Kỳ', '123 Main Street, New York, USA', '1980-12-25', 'Nam', 'Phòng không có thảm', 'ACTIVE'),
('Maria', 'Garcia', 'Maria Garcia', '0901234573', 'mariagarcia@email.com', 'CD789012', 'Passport', 'Tây Ban Nha', '456 Calle Principal, Madrid, Spain', '1985-08-14', 'Nữ', 'Phòng có view thành phố', 'ACTIVE'),
('Pierre', 'Dubois', 'Pierre Dubois', '0901234574', 'pierredubois@email.com', 'EF345678', 'Passport', 'Pháp', '789 Rue Principale, Paris, France', '1983-04-18', 'Nam', 'Phòng có minibar', 'ACTIVE'),
('Yuki', 'Tanaka', 'Yuki Tanaka', '0901234575', 'yukitanaka@email.com', 'GH901234', 'Passport', 'Nhật Bản', '123 Main Street, Tokyo, Japan', '1990-06-20', 'Nữ', 'Phòng có bồn tắm', 'ACTIVE'),
('Li', 'Wei', 'Li Wei', '0901234576', 'liwei@email.com', 'IJ567890', 'Passport', 'Trung Quốc', '456 Main Street, Beijing, China', '1988-02-28', 'Nam', 'Phòng có wifi mạnh', 'ACTIVE');

-- =====================================================================================================================
-- ROOM MAINTENANCE (Bảo trì phòng mẫu)
-- =====================================================================================================================

INSERT INTO room_maintenance (room_id, room_number, issue_type, priority, status, description, notes, requested_by, assigned_to, scheduled_at, estimated_cost) VALUES
((SELECT room_id FROM rooms WHERE room_number = '101'), '101', 'PLUMBING', 'MEDIUM', 'REQUESTED', 'Vòi sen bị rò rỉ nước', 'Cần thay vòi sen mới', 'Nhân viên vệ sinh', NULL, CURRENT_TIMESTAMP + INTERVAL '2 days', 500000),
((SELECT room_id FROM rooms WHERE room_number = '203'), '203', 'ELECTRICAL', 'HIGH', 'ASSIGNED', 'Ổ cắm điện bị cháy', 'Cần thay ổ cắm mới', 'Khách hàng', 'Thợ điện', CURRENT_TIMESTAMP + INTERVAL '1 day', 300000),
((SELECT room_id FROM rooms WHERE room_number = '303'), '303', 'HVAC', 'URGENT', 'IN_PROGRESS', 'Điều hòa không hoạt động', 'Khách hàng phàn nàn nóng', 'Nhân viên lễ tân', 'Thợ điều hòa', CURRENT_TIMESTAMP, 800000);

-- =====================================================================================================================
-- ROOM CLEANING (Dọn dẹp phòng mẫu)
-- =====================================================================================================================

INSERT INTO room_cleaning (room_id, room_number, cleaning_type, priority, status, description, notes, requested_by, assigned_to, scheduled_at) VALUES
((SELECT room_id FROM rooms WHERE room_number = '102'), '102', 'POST_CHECKOUT', 'HIGH', 'ASSIGNED', 'Dọn dẹp sau khi khách check-out', 'Khách check-out lúc 11:00', 'Nhân viên lễ tân', 'Nhân viên vệ sinh', CURRENT_TIMESTAMP + INTERVAL '1 hour'),
((SELECT room_id FROM rooms WHERE room_number = '204'), '204', 'DAILY', 'MEDIUM', 'REQUESTED', 'Dọn dẹp hàng ngày', 'Khách yêu cầu dọn dẹp lúc 14:00', 'Khách hàng', NULL, CURRENT_TIMESTAMP + INTERVAL '3 hours'),
((SELECT room_id FROM rooms WHERE room_number = '401'), '401', 'DEEP_CLEANING', 'LOW', 'REQUESTED', 'Dọn dẹp sâu định kỳ', 'Dọn dẹp sâu hàng tuần', 'Quản lý', NULL, CURRENT_TIMESTAMP + INTERVAL '1 day');

-- =====================================================================================================================
-- ROOM SERVICES (Dịch vụ phòng mẫu)
-- =====================================================================================================================

INSERT INTO room_services (room_number, guest_id, guest_name, service_type, service_name, description, quantity, unit_price, total_price, status, requested_at, requested_by) VALUES
('203', (SELECT id FROM guests WHERE full_name = 'Trần Thị Bình'), 'Trần Thị Bình', 'FOOD', 'Bữa sáng tại phòng', 'Bữa sáng kiểu Việt Nam với phở bò', 2, 150000, 300000, 'REQUESTED', CURRENT_TIMESTAMP, 'Khách hàng'),
('303', (SELECT id FROM guests WHERE full_name = 'Lê Văn Cường'), 'Lê Văn Cường', 'LAUNDRY', 'Giặt ủi quần áo', 'Giặt ủi 5 bộ quần áo', 5, 50000, 250000, 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '2 hours', 'Khách hàng'),
('403', (SELECT id FROM guests WHERE full_name = 'John Smith'), 'John Smith', 'TRANSPORT', 'Đặt xe sân bay', 'Đặt xe từ khách sạn đến sân bay Tân Sơn Nhất', 1, 300000, 300000, 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Khách hàng');

-- =====================================================================================================================
-- CHECK-INS (Check-in mẫu)
-- =====================================================================================================================

INSERT INTO check_ins (booking_id, guest_id, room_id, room_number, check_in_date, check_out_date, check_in_time, number_of_guests, special_requests, status, checked_in_by) VALUES
(uuid_generate_v4(), (SELECT id FROM guests WHERE full_name = 'Nguyễn Văn An'), (SELECT room_id FROM rooms WHERE room_number = '101'), '101', CURRENT_DATE, CURRENT_DATE + INTERVAL '2 days', CURRENT_TIMESTAMP, 2, 'Không có yêu cầu đặc biệt', 'CHECKED_IN', 'Nhân viên lễ tân'),
(uuid_generate_v4(), (SELECT id FROM guests WHERE full_name = 'Trần Thị Bình'), (SELECT room_id FROM rooms WHERE room_number = '203'), '203', CURRENT_DATE, CURRENT_DATE + INTERVAL '3 days', CURRENT_TIMESTAMP, 1, 'Phòng không có mùi thuốc lá', 'CHECKED_IN', 'Nhân viên lễ tân'),
(uuid_generate_v4(), (SELECT id FROM guests WHERE full_name = 'Lê Văn Cường'), (SELECT room_id FROM rooms WHERE room_number = '303'), '303', CURRENT_DATE, CURRENT_DATE + INTERVAL '1 day', CURRENT_TIMESTAMP, 2, 'Phòng view biển', 'CHECKED_IN', 'Nhân viên lễ tân');

-- Cập nhật trạng thái phòng đã check-in
UPDATE rooms SET room_status = 'CHECKED_IN' WHERE room_number IN ('101', '203', '303');

-- =====================================================================================================================
-- VERIFICATION
-- =====================================================================================================================

-- Kiểm tra dữ liệu đã được tạo
SELECT 'Data initialization completed successfully' as status;

-- Đếm số lượng bản ghi trong mỗi bảng
SELECT 'room_types' as table_name, COUNT(*) as record_count FROM room_types
UNION ALL
SELECT 'furniture' as table_name, COUNT(*) as record_count FROM furniture
UNION ALL
SELECT 'room_type_furniture' as table_name, COUNT(*) as record_count FROM room_type_furniture
UNION ALL
SELECT 'rooms' as table_name, COUNT(*) as record_count FROM rooms
UNION ALL
SELECT 'maintenance_types' as table_name, COUNT(*) as record_count FROM maintenance_types
UNION ALL
SELECT 'guests' as table_name, COUNT(*) as record_count FROM guests
UNION ALL
SELECT 'room_maintenance' as table_name, COUNT(*) as record_count FROM room_maintenance
UNION ALL
SELECT 'room_cleaning' as table_name, COUNT(*) as record_count FROM room_cleaning
UNION ALL
SELECT 'room_services' as table_name, COUNT(*) as record_count FROM room_services
UNION ALL
SELECT 'check_ins' as table_name, COUNT(*) as record_count FROM check_ins;

-- Hiển thị thông tin phòng
SELECT 
    r.room_number,
    r.floor,
    rt.type_name,
    r.room_status,
    r.area,
    rt.base_price
FROM rooms r
JOIN room_types rt ON r.room_type_id = rt.room_type_id
ORDER BY r.room_number;
