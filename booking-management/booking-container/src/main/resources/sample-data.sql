-- =====================================================================================================================
-- SAMPLE DATA FOR BOOKING MANAGEMENT SYSTEM
-- =====================================================================================================================

-- Xóa dữ liệu cũ nếu có
DELETE FROM booking.booking_room;
DELETE FROM booking.bookings;
DELETE FROM booking.rooms;
DELETE FROM booking.customers;

-- =====================================================================================================================
-- SAMPLE CUSTOMERS DATA (10 records)
-- =====================================================================================================================
INSERT INTO booking.customers (id, username, first_name, last_name, email) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'nguyenvanA', 'Nguyen', 'Van A', 'nguyenvana@email.com'),
('550e8400-e29b-41d4-a716-446655440002', 'tranthiB', 'Tran', 'Thi B', 'tranthib@email.com'),
('550e8400-e29b-41d4-a716-446655440003', 'levanC', 'Le', 'Van C', 'levanc@email.com'),
('550e8400-e29b-41d4-a716-446655440004', 'phamthiD', 'Pham', 'Thi D', 'phamthid@email.com'),
('550e8400-e29b-41d4-a716-446655440005', 'hoangvanE', 'Hoang', 'Van E', 'hoangvane@email.com'),
('550e8400-e29b-41d4-a716-446655440006', 'dangthiF', 'Dang', 'Thi F', 'dangthif@email.com'),
('550e8400-e29b-41d4-a716-446655440007', 'buitheG', 'Bui', 'The G', 'buitheg@email.com'),
('550e8400-e29b-41d4-a716-446655440008', 'vuthiH', 'Vu', 'Thi H', 'vuthih@email.com'),
('550e8400-e29b-41d4-a716-446655440009', 'nguyenthiI', 'Nguyen', 'Thi I', 'nguyenthi@email.com'),
('550e8400-e29b-41d4-a716-446655440010', 'tranvanJ', 'Tran', 'Van J', 'tranvanj@email.com');

-- =====================================================================================================================
-- SAMPLE ROOMS DATA (10 records)
-- =====================================================================================================================
INSERT INTO booking.rooms (id, room_number, description, price, status) VALUES
('550e8400-e29b-41d4-a716-446655440011', '101', 'Phòng Standard 1 giường đôi, view thành phố', 1500000.00, 'AVAILABLE'),
('550e8400-e29b-41d4-a716-446655440012', '102', 'Phòng Standard 2 giường đơn, view thành phố', 1800000.00, 'AVAILABLE'),
('550e8400-e29b-41d4-a716-446655440013', '201', 'Phòng Deluxe 1 giường king, view biển', 2500000.00, 'AVAILABLE'),
('550e8400-e29b-41d4-a716-446655440014', '202', 'Phòng Deluxe 2 giường đôi, view biển', 2800000.00, 'AVAILABLE'),
('550e8400-e29b-41d4-a716-446655440015', '301', 'Phòng Suite 1 giường king, view biển, balcony', 3500000.00, 'AVAILABLE'),
('550e8400-e29b-41d4-a716-446655440016', '302', 'Phòng Suite 2 giường king, view biển, balcony', 4200000.00, 'AVAILABLE'),
('550e8400-e29b-41d4-a716-446655440017', '401', 'Phòng Presidential, view biển 360 độ', 8000000.00, 'AVAILABLE'),
('550e8400-e29b-41d4-a716-446655440018', '501', 'Phòng Family, 3 giường, view biển', 3200000.00, 'AVAILABLE'),
('550e8400-e29b-41d4-a716-446655440019', '601', 'Phòng Honeymoon, 1 giường king, view biển', 4500000.00, 'AVAILABLE'),
('550e8400-e29b-41d4-a716-446655440020', '701', 'Phòng Business, 1 giường king, view thành phố', 2200000.00, 'AVAILABLE');

-- =====================================================================================================================
-- SAMPLE BOOKINGS DATA (10 records)
-- =====================================================================================================================
INSERT INTO booking.bookings (id, customer_id, check_in, check_out, actual_check_in, actual_check_out, tracking_id, total_price, status) VALUES
('550e8400-e29b-41d4-a716-446655440021', '550e8400-e29b-41d4-a716-446655440001', '2024-12-01 14:00:00', '2024-12-03 12:00:00', '2024-12-01 14:30:00', '2024-12-03 11:45:00', '550e8400-e29b-41d4-a716-446655440031', 3000000.00, 'CHECKED_OUT'),
('550e8400-e29b-41d4-a716-446655440022', '550e8400-e29b-41d4-a716-446655440002', '2024-12-05 14:00:00', '2024-12-07 12:00:00', '2024-12-05 15:00:00', NULL, '550e8400-e29b-41d4-a716-446655440032', 3600000.00, 'CHECKED_IN'),
('550e8400-e29b-41d4-a716-446655440023', '550e8400-e29b-41d4-a716-446655440003', '2024-12-10 14:00:00', '2024-12-12 12:00:00', NULL, NULL, '550e8400-e29b-41d4-a716-446655440033', 5000000.00, 'PAID'),
('550e8400-e29b-41d4-a716-446655440024', '550e8400-e29b-41d4-a716-446655440004', '2024-12-15 14:00:00', '2024-12-17 12:00:00', NULL, NULL, '550e8400-e29b-41d4-a716-446655440034', 6400000.00, 'CONFIRMED'),
('550e8400-e29b-41d4-a716-446655440025', '550e8400-e29b-41d4-a716-446655440005', '2024-12-20 14:00:00', '2024-12-22 12:00:00', NULL, NULL, '550e8400-e29b-41d4-a716-446655440035', 9000000.00, 'PENDING'),
('550e8400-e29b-41d4-a716-446655440026', '550e8400-e29b-41d4-a716-446655440006', '2024-12-25 14:00:00', '2024-12-27 12:00:00', NULL, NULL, '550e8400-e29b-41d4-a716-446655440036', 6400000.00, 'PENDING'),
('550e8400-e29b-41d4-a716-446655440027', '550e8400-e29b-41d4-a716-446655440007', '2024-12-30 14:00:00', '2025-01-01 12:00:00', NULL, NULL, '550e8400-e29b-41d4-a716-446655440037', 9000000.00, 'PENDING'),
('550e8400-e29b-41d4-a716-446655440028', '550e8400-e29b-41d4-a716-446655440008', '2025-01-05 14:00:00', '2025-01-07 12:00:00', NULL, NULL, '550e8400-e29b-41d4-a716-446655440038', 4400000.00, 'PENDING'),
('550e8400-e29b-41d4-a716-446655440029', '550e8400-e29b-41d4-a716-446655440009', '2025-01-10 14:00:00', '2025-01-12 12:00:00', NULL, NULL, '550e8400-e29b-41d4-a716-446655440039', 7000000.00, 'PENDING'),
('550e8400-e29b-41d4-a716-446655440030', '550e8400-e29b-41d4-a716-446655440010', '2025-01-15 14:00:00', '2025-01-17 12:00:00', NULL, NULL, '550e8400-e29b-41d4-a716-446655440040', 4400000.00, 'PENDING');

-- =====================================================================================================================
-- SAMPLE BOOKING_ROOM DATA (10 records - linking bookings with rooms)
-- =====================================================================================================================
INSERT INTO booking.booking_room (id, booking_id, room_id, price) VALUES
('550e8400-e29b-41d4-a716-446655440041', '550e8400-e29b-41d4-a716-446655440021', '550e8400-e29b-41d4-a716-446655440011', 1500000.00),
('550e8400-e29b-41d4-a716-446655440042', '550e8400-e29b-41d4-a716-446655440021', '550e8400-e29b-41d4-a716-446655440012', 1500000.00),
('550e8400-e29b-41d4-a716-446655440043', '550e8400-e29b-41d4-a716-446655440022', '550e8400-e29b-41d4-a716-446655440013', 2500000.00),
('550e8400-e29b-41d4-a716-446655440044', '550e8400-e29b-41d4-a716-446655440022', '550e8400-e29b-41d4-a716-446655440014', 1100000.00),
('550e8400-e29b-41d4-a716-446655440045', '550e8400-e29b-41d4-a716-446655440023', '550e8400-e29b-41d4-a716-446655440015', 3500000.00),
('550e8400-e29b-41d4-a716-446655440046', '550e8400-e29b-41d4-a716-446655440023', '550e8400-e29b-41d4-a716-446655440016', 1500000.00),
('550e8400-e29b-41d4-a716-446655440047', '550e8400-e29b-41d4-a716-446655440024', '550e8400-e29b-41d4-a716-446655440017', 8000000.00),
('550e8400-e29b-41d4-a716-446655440048', '550e8400-e29b-41d4-a716-446655440025', '550e8400-e29b-41d4-a716-446655440018', 3200000.00),
('550e8400-e29b-41d4-a716-446655440049', '550e8400-e29b-41d4-a716-446655440025', '550e8400-e29b-41d4-a716-446655440019', 4500000.00),
('550e8400-e29b-41d4-a716-446655440050', '550e8400-e29b-41d4-a716-446655440025', '550e8400-e29b-41d4-a716-446655440020', 1300000.00);

-- =====================================================================================================================
-- SAMPLE PAYMENT OUTBOX DATA (10 records)
-- =====================================================================================================================
INSERT INTO booking.payment_outbox (id, saga_id, booking_id, created_at, processed_at, type, payload, outbox_status, saga_status, booking_status, version) VALUES
('550e8400-e29b-41d4-a716-446655440051', '550e8400-e29b-41d4-a716-446655440061', '550e8400-e29b-41d4-a716-446655440021', '2024-12-01 14:00:00+07', '2024-12-01 14:05:00+07', 'PAYMENT_PROCESSED', '{"amount": 3000000, "method": "CREDIT_CARD"}', 'COMPLETED', 'SUCCEEDED', 'PAID', 1),
('550e8400-e29b-41d4-a716-446655440052', '550e8400-e29b-41d4-a716-446655440062', '550e8400-e29b-41d4-a716-446655440022', '2024-12-05 14:00:00+07', '2024-12-05 14:03:00+07', 'PAYMENT_PROCESSED', '{"amount": 3600000, "method": "BANK_TRANSFER"}', 'COMPLETED', 'SUCCEEDED', 'PAID', 1),
('550e8400-e29b-41d4-a716-446655440053', '550e8400-e29b-41d4-a716-446655440063', '550e8400-e29b-41d4-a716-446655440023', '2024-12-10 14:00:00+07', '2024-12-10 14:02:00+07', 'PAYMENT_PROCESSED', '{"amount": 5000000, "method": "CREDIT_CARD"}', 'COMPLETED', 'SUCCEEDED', 'PAID', 1),
('550e8400-e29b-41d4-a716-446655440054', '550e8400-e29b-41d4-a716-446655440064', '550e8400-e29b-41d4-a716-446655440024', '2024-12-15 14:00:00+07', '2024-12-15 14:04:00+07', 'PAYMENT_PROCESSED', '{"amount": 6400000, "method": "CASH"}', 'COMPLETED', 'SUCCEEDED', 'PAID', 1),
('550e8400-e29b-41d4-a716-446655440055', '550e8400-e29b-41d4-a716-446655440065', '550e8400-e29b-41d4-a716-446655440025', '2024-12-20 14:00:00+07', NULL, 'PAYMENT_PENDING', '{"amount": 9000000, "method": "CREDIT_CARD"}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440056', '550e8400-e29b-41d4-a716-446655440066', '550e8400-e29b-41d4-a716-446655440026', '2024-12-25 14:00:00+07', NULL, 'PAYMENT_PENDING', '{"amount": 6400000, "method": "BANK_TRANSFER"}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440057', '550e8400-e29b-41d4-a716-446655440067', '550e8400-e29b-41d4-a716-446655440027', '2024-12-30 14:00:00+07', NULL, 'PAYMENT_PENDING', '{"amount": 9000000, "method": "CREDIT_CARD"}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440058', '550e8400-e29b-41d4-a716-446655440068', '550e8400-e29b-41d4-a716-446655440028', '2025-01-05 14:00:00+07', NULL, 'PAYMENT_PENDING', '{"amount": 4400000, "method": "CASH"}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440059', '550e8400-e29b-41d4-a716-446655440069', '550e8400-e29b-41d4-a716-446655440029', '2025-01-10 14:00:00+07', NULL, 'PAYMENT_PENDING', '{"amount": 7000000, "method": "CREDIT_CARD"}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440060', '550e8400-e29b-41d4-a716-446655440070', '550e8400-e29b-41d4-a716-446655440030', '2025-01-15 14:00:00+07', NULL, 'PAYMENT_PENDING', '{"amount": 4400000, "method": "BANK_TRANSFER"}', 'STARTED', 'PROCESSING', 'PENDING', 1);

-- =====================================================================================================================
-- SAMPLE NOTIFICATION OUTBOX DATA (10 records)
-- =====================================================================================================================
INSERT INTO booking.notification_outbox (id, saga_id, booking_id, created_at, processed_at, type, payload, outbox_status, saga_status, booking_status, version) VALUES
('550e8400-e29b-41d4-a716-446655440071', '550e8400-e29b-41d4-a716-446655440081', '550e8400-e29b-41d4-a716-446655440021', '2024-12-01 14:00:00+07', '2024-12-01 14:06:00+07', 'BOOKING_CONFIRMATION', '{"email": "nguyenvana@email.com", "template": "CONFIRMATION"}', 'COMPLETED', 'SUCCEEDED', 'PAID', 1),
('550e8400-e29b-41d4-a716-446655440072', '550e8400-e29b-41d4-a716-446655440082', '550e8400-e29b-41d4-a716-446655440022', '2024-12-05 14:00:00+07', '2024-12-05 14:04:00+07', 'BOOKING_CONFIRMATION', '{"email": "tranthib@email.com", "template": "CONFIRMATION"}', 'COMPLETED', 'SUCCEEDED', 'PAID', 1),
('550e8400-e29b-41d4-a716-446655440073', '550e8400-e29b-41d4-a716-446655440083', '550e8400-e29b-41d4-a716-446655440023', '2024-12-10 14:00:00+07', '2024-12-10 14:03:00+07', 'BOOKING_CONFIRMATION', '{"email": "levanc@email.com", "template": "CONFIRMATION"}', 'COMPLETED', 'SUCCEEDED', 'PAID', 1),
('550e8400-e29b-41d4-a716-446655440074', '550e8400-e29b-41d4-a716-446655440084', '550e8400-e29b-41d4-a716-446655440024', '2024-12-15 14:00:00+07', '2024-12-15 14:05:00+07', 'BOOKING_CONFIRMATION', '{"email": "phamthid@email.com", "template": "CONFIRMATION"}', 'COMPLETED', 'SUCCEEDED', 'PAID', 1),
('550e8400-e29b-41d4-a716-446655440075', '550e8400-e29b-41d4-a716-446655440085', '550e8400-e29b-41d4-a716-446655440025', '2024-12-20 14:00:00+07', NULL, 'BOOKING_PENDING', '{"email": "hoangvane@email.com", "template": "PENDING"}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440076', '550e8400-e29b-41d4-a716-446655440086', '550e8400-e29b-41d4-a716-446655440026', '2024-12-25 14:00:00+07', NULL, 'BOOKING_PENDING', '{"email": "dangthif@email.com", "template": "PENDING"}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440077', '550e8400-e29b-41d4-a716-446655440087', '550e8400-e29b-41d4-a716-446655440027', '2024-12-30 14:00:00+07', NULL, 'BOOKING_PENDING', '{"email": "buitheg@email.com", "template": "PENDING"}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440078', '550e8400-e29b-41d4-a716-446655440088', '550e8400-e29b-41d4-a716-446655440028', '2025-01-05 14:00:00+07', NULL, 'BOOKING_PENDING', '{"email": "vuthih@email.com", "template": "PENDING"}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440079', '550e8400-e29b-41d4-a716-446655440089', '550e8400-e29b-41d4-a716-446655440029', '2025-01-10 14:00:00+07', NULL, 'BOOKING_PENDING', '{"email": "nguyenthi@email.com", "template": "PENDING"}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440080', '550e8400-e29b-41d4-a716-446655440090', '550e8400-e29b-41d4-a716-446655440030', '2025-01-15 14:00:00+07', NULL, 'BOOKING_PENDING', '{"email": "tranvanj@email.com", "template": "PENDING"}', 'STARTED', 'PROCESSING', 'PENDING', 1);

-- =====================================================================================================================
-- SAMPLE ROOM OUTBOX DATA (10 records)
-- =====================================================================================================================
INSERT INTO booking.room_outbox (id, saga_id, booking_id, created_at, processed_at, type, payload, outbox_status, saga_status, booking_status, version) VALUES
('550e8400-e29b-41d4-a716-446655440091', '550e8400-e29b-41d4-a716-446655440101', '550e8400-e29b-41d4-a716-446655440021', '2024-12-01 14:00:00+07', '2024-12-01 14:07:00+07', 'ROOM_RESERVED', '{"roomIds": ["550e8400-e29b-41d4-a716-446655440011", "550e8400-e29b-41d4-a716-446655440012"]}', 'COMPLETED', 'SUCCEEDED', 'PAID', 1),
('550e8400-e29b-41d4-a716-446655440092', '550e8400-e29b-41d4-a716-446655440102', '550e8400-e29b-41d4-a716-446655440022', '2024-12-05 14:00:00+07', '2024-12-05 14:05:00+07', 'ROOM_RESERVED', '{"roomIds": ["550e8400-e29b-41d4-a716-446655440013", "550e8400-e29b-41d4-a716-446655440014"]}', 'COMPLETED', 'SUCCEEDED', 'PAID', 1),
('550e8400-e29b-41d4-a716-446655440093', '550e8400-e29b-41d4-a716-446655440103', '550e8400-e29b-41d4-a716-446655440023', '2024-12-10 14:00:00+07', '2024-12-10 14:04:00+07', 'ROOM_RESERVED', '{"roomIds": ["550e8400-e29b-41d4-a716-446655440015", "550e8400-e29b-41d4-a716-446655440016"]}', 'COMPLETED', 'SUCCEEDED', 'PAID', 1),
('550e8400-e29b-41d4-a716-446655440094', '550e8400-e29b-41d4-a716-446655440104', '550e8400-e29b-41d4-a716-446655440024', '2024-12-15 14:00:00+07', '2024-12-15 14:06:00+07', 'ROOM_RESERVED', '{"roomIds": ["550e8400-e29b-41d4-a716-446655440017"]}', 'COMPLETED', 'SUCCEEDED', 'PAID', 1),
('550e8400-e29b-41d4-a716-446655440095', '550e8400-e29b-41d4-a716-446655440105', '550e8400-e29b-41d4-a716-446655440025', '2024-12-20 14:00:00+07', NULL, 'ROOM_PENDING', '{"roomIds": ["550e8400-e29b-41d4-a716-446655440018", "550e8400-e29b-41d4-a716-446655440019", "550e8400-e29b-41d4-a716-446655440020"]}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440096', '550e8400-e29b-41d4-a716-446655440106', '550e8400-e29b-41d4-a716-446655440026', '2024-12-25 14:00:00+07', NULL, 'ROOM_PENDING', '{"roomIds": ["550e8400-e29b-41d4-a716-446655440017"]}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440097', '550e8400-e29b-41d4-a716-446655440107', '550e8400-e29b-41d4-a716-446655440027', '2024-12-30 14:00:00+07', NULL, 'ROOM_PENDING', '{"roomIds": ["550e8400-e29b-41d4-a716-446655440018", "550e8400-e29b-41d4-a716-446655440019"]}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440098', '550e8400-e29b-41d4-a716-446655440108', '550e8400-e29b-41d4-a716-446655440028', '2025-01-05 14:00:00+07', NULL, 'ROOM_PENDING', '{"roomIds": ["550e8400-e29b-41d4-a716-446655440020", "550e8400-e29b-41d4-a716-446655440011"]}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440099', '550e8400-e29b-41d4-a716-446655440109', '550e8400-e29b-41d4-a716-446655440029', '2025-01-10 14:00:00+07', NULL, 'ROOM_PENDING', '{"roomIds": ["550e8400-e29b-41d4-a716-446655440015", "550e8400-e29b-41d4-a716-446655440016"]}', 'STARTED', 'PROCESSING', 'PENDING', 1),
('550e8400-e29b-41d4-a716-446655440100', '550e8400-e29b-41d4-a716-446655440110', '550e8400-e29b-41d4-a716-446655440030', '2025-01-15 14:00:00+07', NULL, 'ROOM_PENDING', '{"roomIds": ["550e8400-e29b-41d4-a716-446655440012", "550e8400-e29b-41d4-a716-446655440013"]}', 'STARTED', 'PROCESSING', 'PENDING', 1);

-- =====================================================================================================================
-- COMMIT TRANSACTION
-- =====================================================================================================================
COMMIT;

-- =====================================================================================================================
-- VERIFICATION QUERIES
-- =====================================================================================================================

-- Kiểm tra số lượng record đã insert
SELECT 'customers' as table_name, COUNT(*) as record_count FROM booking.customers
UNION ALL
SELECT 'rooms' as table_name, COUNT(*) as record_count FROM booking.rooms
UNION ALL
SELECT 'bookings' as table_name, COUNT(*) as record_count FROM booking.bookings
UNION ALL
SELECT 'booking_room' as table_name, COUNT(*) as record_count FROM booking.booking_room
UNION ALL
SELECT 'payment_outbox' as table_name, COUNT(*) as record_count FROM booking.payment_outbox
UNION ALL
SELECT 'notification_outbox' as table_name, COUNT(*) as record_count FROM booking.notification_outbox
UNION ALL
SELECT 'room_outbox' as table_name, COUNT(*) as record_count FROM booking.room_outbox;

-- Kiểm tra dữ liệu mẫu
SELECT 'Sample Customers' as info;
SELECT id, username, first_name, last_name, email FROM booking.customers LIMIT 5;

SELECT 'Sample Rooms' as info;
SELECT id, room_number, description, price, status FROM booking.rooms LIMIT 5;

SELECT 'Sample Bookings' as info;
SELECT id, customer_id, check_in, check_out, total_price, status FROM booking.bookings LIMIT 5;

SELECT 'Sample Booking-Room Relationships' as info;
SELECT br.id, br.booking_id, br.room_id, br.price, r.room_number, b.status 
FROM booking.booking_room br 
JOIN booking.rooms r ON br.room_id = r.id 
JOIN booking.bookings b ON br.booking_id = b.id 
LIMIT 5;
