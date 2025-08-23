SET search_path TO booking, public;

-- =====================================================================================================================
-- INSERT DỮ LIỆU MẪU CHO CUSTOMERS
-- =====================================================================================================================
INSERT INTO booking.customers (id, username, first_name, last_name, email) VALUES
    ('11111111-1111-1111-1111-111111111111', 'nguyenvana', 'Nguyễn', 'Văn A', 'vana@example.com'),
    ('22222222-2222-2222-2222-222222222222', 'tranthib', 'Trần', 'Thị B', 'thib@example.com'),
    ('33333333-3333-3333-3333-333333333333', 'levanc', 'Lê', 'Văn C', 'vanc@example.com'),
    ('44444444-4444-4444-4444-444444444444', 'phamthid', 'Phạm', 'Thị D', 'thid@example.com'),
    ('55555555-5555-5555-5555-555555555555', 'hoangvane', 'Hoàng', 'Văn E', 'vane@example.com'),
    ('66666666-6666-6666-6666-666666666666', 'dothif', 'Đỗ', 'Thị F', 'thif@example.com'),
    ('77777777-7777-7777-7777-777777777777', 'buivang', 'Bùi', 'Văn G', 'vang@example.com'),
    ('88888888-8888-8888-8888-888888888888', 'ngothih', 'Ngô', 'Thị H', 'thih@example.com'),
    ('99999999-9999-9999-9999-999999999999', 'vovani', 'Võ', 'Văn I', 'vani@example.com'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01', 'dangthij', 'Đặng', 'Thị J', 'thij@example.com');

-- =====================================================================================================================
-- INSERT DỮ LIỆU MẪU CHO ROOMS (tham chiếu từ room_management)
-- =====================================================================================================================
INSERT INTO booking.rooms (id, room_number, price, status) VALUES
    ('fe0dc28a-654a-449c-84bd-f843d0544047', '101', 5000000, 'VACANT'),
    ('737cedb6-830e-4e72-a659-fa74352f7258', '102', 5000000, 'BOOKED'),
    ('7afb950f-c05b-4f5b-9fa2-30fe701e7f9f', '103', 5000000, 'CHECKED_IN'),
    ('5894fe26-1199-4124-a8f4-d87467cbc73a', '104', 5000000, 'MAINTENANCE'),
    ('4ce4e84b-06a1-4712-ab51-050dbbecfba5', '201', 7000000, 'CLEANING'),
    ('c63b64e0-c59e-4958-aef2-da05702427a1', '202', 7000000, 'CHECKED_OUT'),
    ('9d779fcc-09f9-4321-8c2b-f1b523554461', '203', 7000000, 'VACANT'),
    ('eea149f3-aba3-400d-a185-c177fea153aa', '204', 7000000, 'VACANT'),
    ('b7838df0-ac7d-40c4-8560-aa898597c45b', '301', 10000000, 'VACANT'),
    ('42fec22a-423a-4e99-a052-6d706002c045', '302', 10000000, 'VACANT'),
    ('d8fb7433-c94a-4799-a8ec-a2cd219eed55', '303', 10000000, 'VACANT'),
    ('b3b97adf-d7cd-4605-b271-ac1412066196', '304', 10000000, 'VACANT'),
    ('ab591c3e-c1bc-462d-8512-60a9ef261009', '401', 15000000, 'VACANT'),
    ('9a0e26be-30a0-4fd8-a37e-6a61ce744567', '402', 15000000, 'VACANT'),
    ('4434b747-bd73-44b8-9ade-070f97a60302', '403', 15000000, 'VACANT'),
    ('2519114c-0991-4c48-af11-ec43ecd521a7', '404', 15000000, 'VACANT'),
    ('2ac1d74b-f06f-445f-bf91-acdde9893f97', '501', 30000000, 'VACANT'),
    ('998ff04a-e8f8-4c68-acce-f903f6f44235', '502', 30000000, 'VACANT'),
    ('c263cd1a-3269-4329-bfbf-67beb61ea2c6', '503', 30000000, 'VACANT'),
    ('9796916f-8d91-4c1c-bd40-90b8f5cf7fbc', '504', 30000000, 'VACANT');

-- =====================================================================================================================
-- INSERT DỮ LIỆU MẪU CHO BOOKINGS
-- =====================================================================================================================
INSERT INTO booking.bookings (id, customer_id, check_in, check_out, actual_check_in, actual_check_out, tracking_id, total_price, status) VALUES
    -- Booking đã xác nhận - check-in hôm nay
    ('b1111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 
     '2025-08-20 14:00:00', '2025-08-22 11:00:00', '2025-08-20 14:30:00', NULL, 
     't1111111-1111-1111-1111-111111111111', 10000000, 'CHECKED_IN'),
    
    -- Booking đã xác nhận - check-in ngày mai
    ('b2222222-2222-2222-2222-222222222222', '22222222-2222-2222-2222-222222222222', 
     '2025-08-21 14:00:00', '2025-08-23 11:00:00', NULL, NULL, 
     't2222222-2222-2222-2222-222222222222', 14000000, 'CONFIRMED'),
    
    -- Booking đã đặt cọc
    ('b3333333-3333-3333-3333-333333333333', '33333333-3333-3333-3333-333333333333', 
     '2025-08-25 14:00:00', '2025-08-27 11:00:00', NULL, NULL, 
     't3333333-3333-3333-3333-333333333333', 20000000, 'DEPOSITED'),
    
    -- Booking chờ xác nhận
    ('b4444444-4444-4444-4444-444444444444', '44444444-4444-4444-4444-444444444444', 
     '2025-08-28 14:00:00', '2025-08-30 11:00:00', NULL, NULL, 
     't4444444-4444-4444-4444-444444444444', 30000000, 'PENDING'),
    
    -- Booking đã thanh toán
    ('b5555555-5555-5555-5555-555555555555', '55555555-5555-5555-5555-555555555555', 
     '2025-08-15 14:00:00', '2025-08-17 11:00:00', '2025-08-15 14:15:00', '2025-08-17 11:00:00', 
     't5555555-5555-5555-5555-555555555555', 14000000, 'PAID'),
    
    -- Booking đã check-out
    ('b6666666-6666-6666-6666-666666666666', '66666666-6666-6666-6666-666666666666', 
     '2025-08-10 14:00:00', '2025-08-12 11:00:00', '2025-08-10 14:20:00', '2025-08-12 11:00:00', 
     't6666666-6666-6666-6666-666666666666', 10000000, 'CHECKED_OUT'),
    
    -- Booking đang hủy
    ('b7777777-7777-7777-7777-777777777777', '77777777-7777-7777-7777-777777777777', 
     '2025-08-30 14:00:00', '2025-09-01 11:00:00', NULL, NULL, 
     't7777777-7777-7777-7777-777777777777', 20000000, 'CANCELLING'),
    
    -- Booking đã hủy
    ('b8888888-8888-8888-8888-888888888888', '88888888-8888-8888-8888-888888888888', 
     '2025-09-05 14:00:00', '2025-09-07 11:00:00', NULL, NULL, 
     't8888888-8888-8888-8888-888888888888', 30000000, 'CANCELLED'),
    
    -- Booking VIP - Suite
    ('b9999999-9999-9999-9999-999999999999', '99999999-9999-9999-9999-999999999999', 
     '2025-08-22 14:00:00', '2025-08-25 11:00:00', NULL, NULL, 
     't9999999-9999-9999-9999-999999999999', 45000000, 'CONFIRMED'),
    
    -- Booking dài ngày
    ('baaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01', 
     '2025-08-25 14:00:00', '2025-09-01 11:00:00', NULL, NULL, 
     'taaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01', 210000000, 'DEPOSITED');

-- =====================================================================================================================
-- INSERT DỮ LIỆU MẪU CHO BOOKING_ROOM
-- =====================================================================================================================
INSERT INTO booking.booking_room (id, booking_id, room_id, price) VALUES
    -- Booking 1: Phòng 103 (Deluxe) - 2 đêm
    ('br111111-1111-1111-1111-111111111111', 'b1111111-1111-1111-1111-111111111111', 
     '7afb950f-c05b-4f5b-9fa2-30fe701e7f9f', 5000000),
    
    -- Booking 2: Phòng 201 (Executive) - 2 đêm
    ('br222222-2222-2222-2222-222222222222', 'b2222222-2222-2222-2222-222222222222', 
     '4ce4e84b-06a1-4712-ab51-050dbbecfba5', 7000000),
    
    -- Booking 3: Phòng 301 (Deluxe Suite) - 2 đêm
    ('br333333-3333-3333-3333-333333333333', 'b3333333-3333-3333-3333-333333333333', 
     'b7838df0-ac7d-40c4-8560-aa898597c45b', 10000000),
    
    -- Booking 4: Phòng 401 (Executive Suite) - 2 đêm
    ('br444444-4444-4444-4444-444444444444', 'b4444444-4444-4444-4444-444444444444', 
     'ab591c3e-c1bc-462d-8512-60a9ef261009', 15000000),
    
    -- Booking 5: Phòng 202 (Executive) - 2 đêm
    ('br555555-5555-5555-5555-555555555555', 'b5555555-5555-5555-5555-555555555555', 
     'c63b64e0-c59e-4958-aef2-da05702427a1', 7000000),
    
    -- Booking 6: Phòng 102 (Deluxe) - 2 đêm
    ('br666666-6666-6666-6666-666666666666', 'b6666666-6666-6666-6666-666666666666', 
     '737cedb6-830e-4e72-a659-fa74352f7258', 5000000),
    
    -- Booking 7: Phòng 302 (Deluxe Suite) - 2 đêm
    ('br777777-7777-7777-7777-777777777777', 'b7777777-7777-7777-7777-777777777777', 
     '42fec22a-423a-4e99-a052-6d706002c045', 10000000),
    
    -- Booking 8: Phòng 402 (Executive Suite) - 2 đêm
    ('br888888-8888-8888-8888-888888888888', 'b8888888-8888-8888-8888-888888888888', 
     '9a0e26be-30a0-4fd8-a37e-6a61ce744567', 15000000),
    
    -- Booking 9: Phòng 303 (Deluxe Suite) - 3 đêm
    ('br999999-9999-9999-9999-999999999999', 'b9999999-9999-9999-9999-999999999999', 
     'd8fb7433-c94a-4799-a8ec-a2cd219eed55', 10000000),
    
    -- Booking 10: Phòng 501 (Presidential Suite) - 7 đêm
    ('braaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01', 'baaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01', 
     '2ac1d74b-f06f-445f-bf91-acdde9893f97', 30000000);

-- =====================================================================================================================
-- INSERT DỮ LIỆU MẪU CHO NOTIFICATION OUTBOX
-- =====================================================================================================================
INSERT INTO booking.notification_outbox (id, saga_id, booking_id, created_at, processed_at, type, payload, outbox_status, saga_status, booking_status, version) VALUES
    ('no111111-1111-1111-1111-111111111111', 's1111111-1111-1111-1111-111111111111', 'b1111111-1111-1111-1111-111111111111',
     '2025-08-20 14:30:00', '2025-08-20 14:35:00', 'BOOKING_CONFIRMED', 
     '{"customerName": "Nguyễn Văn A", "roomNumber": "103", "checkIn": "2025-08-20 14:00:00"}', 
     'COMPLETED', 'SUCCEEDED', 'CHECKED_IN', 1),
    
    ('no222222-2222-2222-2222-222222222222', 's2222222-2222-2222-2222-222222222222', 'b2222222-2222-2222-2222-222222222222',
     '2025-08-20 15:00:00', NULL, 'BOOKING_CONFIRMED', 
     '{"customerName": "Trần Thị B", "roomNumber": "201", "checkIn": "2025-08-21 14:00:00"}', 
     'STARTED', 'PROCESSING', 'CONFIRMED', 1),
    
    ('no333333-3333-3333-3333-333333333333', 's3333333-3333-3333-3333-333333333333', 'b3333333-3333-3333-3333-333333333333',
     '2025-08-20 16:00:00', '2025-08-20 16:05:00', 'DEPOSIT_RECEIVED', 
     '{"customerName": "Lê Văn C", "amount": 20000000, "roomNumber": "301"}', 
     'COMPLETED', 'SUCCEEDED', 'DEPOSITED', 1);

-- =====================================================================================================================
-- INSERT DỮ LIỆU MẪU CHO ROOM OUTBOX
-- =====================================================================================================================
INSERT INTO booking.room_outbox (id, saga_id, booking_id, created_at, processed_at, type, payload, outbox_status, saga_status, booking_status, version) VALUES
    ('ro111111-1111-1111-1111-111111111111', 's1111111-1111-1111-1111-111111111111', 'b1111111-1111-1111-1111-111111111111',
     '2025-08-20 14:30:00', '2025-08-20 14:32:00', 'ROOM_OCCUPIED', 
     '{"roomId": "7afb950f-c05b-4f5b-9fa2-30fe701e7f9f", "roomNumber": "103", "status": "CHECKED_IN"}', 
     'COMPLETED', 'SUCCEEDED', 'CHECKED_IN', 1),
    
    ('ro222222-2222-2222-2222-222222222222', 's2222222-2222-2222-2222-222222222222', 'b2222222-2222-2222-2222-222222222222',
     '2025-08-20 15:00:00', '2025-08-20 15:02:00', 'ROOM_RESERVED', 
     '{"roomId": "4ce4e84b-06a1-4712-ab51-050dbbecfba5", "roomNumber": "201", "status": "BOOKED"}', 
     'COMPLETED', 'SUCCEEDED', 'CONFIRMED', 1),
    
    ('ro333333-3333-3333-3333-333333333333', 's3333333-3333-3333-3333-333333333333', 'b3333333-3333-3333-3333-333333333333',
     '2025-08-20 16:00:00', '2025-08-20 16:01:00', 'ROOM_RESERVED', 
     '{"roomId": "b7838df0-ac7d-40c4-8560-aa898597c45b", "roomNumber": "301", "status": "BOOKED"}', 
     'COMPLETED', 'SUCCEEDED', 'DEPOSITED', 1);

-- =====================================================================================================================
-- CẬP NHẬT TRẠNG THÁI PHÒNG THEO BOOKING
-- =====================================================================================================================
-- Cập nhật trạng thái phòng 103 thành CHECKED_IN
UPDATE booking.rooms SET status = 'CHECKED_IN' WHERE room_number = '103';

-- Cập nhật trạng thái phòng 201 thành BOOKED
UPDATE booking.rooms SET status = 'BOOKED' WHERE room_number = '201';

-- Cập nhật trạng thái phòng 301 thành BOOKED
UPDATE booking.rooms SET status = 'BOOKED' WHERE room_number = '301';

-- Cập nhật trạng thái phòng 401 thành BOOKED
UPDATE booking.rooms SET status = 'BOOKED' WHERE room_number = '401';

-- Cập nhật trạng thái phòng 202 thành VACANT (đã check-out)
UPDATE booking.rooms SET status = 'VACANT' WHERE room_number = '202';

-- Cập nhật trạng thái phòng 102 thành BOOKED
UPDATE booking.rooms SET status = 'BOOKED' WHERE room_number = '102';

-- Cập nhật trạng thái phòng 302 thành BOOKED
UPDATE booking.rooms SET status = 'BOOKED' WHERE room_number = '302';

-- Cập nhật trạng thái phòng 402 thành BOOKED
UPDATE booking.rooms SET status = 'BOOKED' WHERE room_number = '402';

-- Cập nhật trạng thái phòng 303 thành BOOKED
UPDATE booking.rooms SET status = 'BOOKED' WHERE room_number = '303';

-- Cập nhật trạng thái phòng 501 thành BOOKED
UPDATE booking.rooms SET status = 'BOOKED' WHERE room_number = '501';
                                        