-- ============================================================================
-- Chuẩn bị
-- ============================================================================
SET search_path TO room_management, public;
CREATE EXTENSION IF NOT EXISTS pgcrypto;      -- để dùng gen_random_uuid()
-- (uuid-ossp & pg_trgm đã tạo trong schema trước đó)

-- ============================================================================
-- FURNITURE
-- ============================================================================
-- PK: furniture.furniture_id UUID DEFAULT uuid_generate_v4() (hoặc để trống sẽ tự sinh)
INSERT INTO furniture (furniture_id, name, price) VALUES
                                                      (gen_random_uuid(), 'Giường King Size',    5000000),
                                                      (gen_random_uuid(), 'Tivi 55 inch',       15000000),
                                                      (gen_random_uuid(), 'Bàn làm việc',        3000000),
                                                      (gen_random_uuid(), 'Minibar',             2000000),
                                                      (gen_random_uuid(), 'Ghế Sofa',            7000000),
                                                      (gen_random_uuid(), 'Máy lạnh trung tâm', 10000000),
                                                      (gen_random_uuid(), 'Bồn tắm Jacuzzi',    25000000),
                                                      (gen_random_uuid(), 'Bàn trang điểm',      2500000);

-- ============================================================================
-- MAINTENANCE TYPES
-- ============================================================================
-- PK: maintenance_types.maintenance_type_id UUID
INSERT INTO maintenance_types (maintenance_type_id, name, description) VALUES
                                                                           (gen_random_uuid(), 'Điện',     'Sửa chữa hệ thống điện, đèn, ổ cắm'),
                                                                           (gen_random_uuid(), 'Nước',     'Xử lý rò rỉ nước, vòi sen, bồn cầu'),
                                                                           (gen_random_uuid(), 'Điều hòa', 'Bảo dưỡng hệ thống HVAC'),
                                                                           (gen_random_uuid(), 'Nội thất', 'Sửa chữa giường, tủ, sofa'),
                                                                           (gen_random_uuid(), 'Khác',     'Các vấn đề khác');

-- ============================================================================
-- ROOM TYPES (5 loại)
-- ============================================================================
-- PK: room_types.room_type_id UUID
INSERT INTO room_types (room_type_id, type_name, description, base_price, max_occupancy) VALUES
                                                                                             (gen_random_uuid(), 'Standard Room',     'Phòng tiêu chuẩn 5 sao, nội thất cơ bản, thích hợp cho khách công tác.', 1500000, 2),
                                                                                             (gen_random_uuid(), 'Deluxe Room',       'Phòng Deluxe có view thành phố, trang trí hiện đại, có bàn làm việc.',   2500000, 2),
                                                                                             (gen_random_uuid(), 'Suite Room',        'Phòng Suite rộng rãi, có phòng khách riêng, view biển.',                 4000000, 3),
                                                                                             (gen_random_uuid(), 'Executive Room',    'Phòng hạng Executive, bao gồm dịch vụ lounge và ưu tiên check-in.',      6000000, 3),
                                                                                             (gen_random_uuid(), 'Presidential Suite','Phòng Tổng thống sang trọng nhất, đầy đủ tiện nghi cao cấp.',           12000000,4);

-- ============================================================================
-- ROOMS (20 phòng) – map room_type_id theo type_name
-- ============================================================================
INSERT INTO rooms (room_id, room_type_id, room_number, floor, area, room_status, image_url) VALUES
                                                                                                ('46857eb2-b9bf-437b-b3df-23b70fbe2d15'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Standard Room'),     '101', 1, '28m2', 'VACANT',      'https://picsum.photos/200/300'),
                                                                                                ('8363e9a6-9855-40df-9c29-7a4e46cf6d46'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Standard Room'),     '102', 1, '28m2', 'BOOKED',      'https://picsum.photos/200/300'),
                                                                                                ('1287b0b1-da82-45ba-8348-72418f84c3a9'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Standard Room'),     '103', 1, '30m2', 'CHECKED_IN',  'https://picsum.photos/200/300'),
                                                                                                ('cdb48914-277a-4496-9926-dac8d592f21c'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Standard Room'),     '104', 1, '30m2', 'MAINTENANCE', 'https://picsum.photos/200/300'),

                                                                                                ('9f58c535-3f0d-41cd-a411-d09c290ea54e'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Deluxe Room'),       '201', 2, '35m2', 'CLEANING',    'https://picsum.photos/200/300'),
                                                                                                ('90dfdd43-82cb-4a0b-8b31-c2b4fa4da5c4'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Deluxe Room'),       '202', 2, '35m2', 'CHECKED_OUT', 'https://picsum.photos/200/300'),
                                                                                                ('c408f56a-aa03-4729-9f55-745c629bc530'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Deluxe Room'),       '203', 2, '38m2', 'VACANT',      'https://picsum.photos/200/300'),
                                                                                                ('ac9206cb-a12e-4ed8-858f-aa21b08cb943'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Deluxe Room'),       '204', 2, '38m2', 'VACANT',      'https://picsum.photos/200/300'),

                                                                                                ('6b973b8b-ca2f-4f20-8461-bb933cbc0558'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Suite Room'),        '301', 3, '50m2', 'VACANT',      'https://picsum.photos/200/300'),
                                                                                                ('de0c95ac-fae5-4312-bfb3-94bb69478ed3'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Suite Room'),        '302', 3, '52m2', 'VACANT',      'https://picsum.photos/200/300'),
                                                                                                ('9773b39b-6465-4bb0-a011-06124fd570f2'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Suite Room'),        '303', 3, '55m2', 'VACANT',      'https://picsum.photos/200/300'),
                                                                                                ('4ae2e8d8-d927-46b2-a8d0-2702cd3bd561'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Suite Room'),        '304', 3, '55m2', 'VACANT',      'https://picsum.photos/200/300'),

                                                                                                ('da8e366c-74c4-4e3f-8c79-b920a1360718'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Executive Room'),    '401', 4, '65m2', 'VACANT',      'https://picsum.photos/200/300'),
                                                                                                ('db8d2765-2eab-4637-af4e-1dd8f5f3e443'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Executive Room'),    '402', 4, '68m2', 'VACANT',      'https://picsum.photos/200/300'),
                                                                                                ('b34a4355-6b7d-4626-93d9-41553c381202'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Executive Room'),    '403', 4, '70m2', 'VACANT',      'https://picsum.photos/200/300'),
                                                                                                ('e791f150-6088-4c86-b01a-eb7e36a6e7a0'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Executive Room'),    '404', 4, '70m2', 'VACANT',      'https://picsum.photos/200/300'),

                                                                                                ('f6dafb56-2ed2-4eb7-b32e-93660576aed8'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Presidential Suite'),'501', 5, '120m2', 'VACANT',     'https://picsum.photos/200/300'),
                                                                                                ('c9d19e4f-7b01-4d55-ad5a-a93a4877ea9a'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Presidential Suite'),'502', 5, '125m2', 'VACANT',     'https://picsum.photos/200/300'),
                                                                                                ('89f73a0a-1a4d-41fb-b9bb-07d1071baddf'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Presidential Suite'),'503', 5, '130m2', 'VACANT',     'https://picsum.photos/200/300'),
                                                                                                ('26817235-498e-4de4-abf7-cd2c027fb4f3'::uuid, (SELECT room_type_id FROM room_types WHERE type_name='Presidential Suite'),'504', 5, '135m2', 'VACANT',     'https://picsum.photos/200/300');

-- ============================================================================
-- ROOM TYPE FURNITURE (map bằng tên)
-- ============================================================================
INSERT INTO room_type_furniture (room_type_furniture_id, room_type_id, furniture_id, require_quantity) VALUES
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Standard Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Giường King Size'), 1),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Standard Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Tivi 55 inch'), 1),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Standard Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Bàn làm việc'), 1),

                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Deluxe Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Giường King Size'), 1),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Deluxe Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Tivi 55 inch'), 1),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Deluxe Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Bàn làm việc'), 1),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Deluxe Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Minibar'), 1),

                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Suite Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Giường King Size'), 1),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Suite Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Tivi 55 inch'), 2),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Suite Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Ghế Sofa'), 1),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Suite Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Minibar'), 1),

                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Executive Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Giường King Size'), 2),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Executive Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Tivi 55 inch'), 2),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Executive Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Ghế Sofa'), 1),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Executive Room'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Máy lạnh trung tâm'), 1),

                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Presidential Suite'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Giường King Size'), 2),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Presidential Suite'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Tivi 55 inch'), 3),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Presidential Suite'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Ghế Sofa'), 2),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Presidential Suite'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Máy lạnh trung tâm'), 1),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Presidential Suite'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Bồn tắm Jacuzzi'), 1),
                                                                                                           (gen_random_uuid(), (SELECT room_type_id FROM room_types WHERE type_name='Presidential Suite'),
                                                                                                            (SELECT furniture_id FROM furniture  WHERE name='Bàn trang điểm'), 1);

-- ============================================================================
-- GUESTS
-- ============================================================================
INSERT INTO guests (id, first_name, last_name, full_name, phone, email, id_number, id_type, nationality, address, date_of_birth, gender, status, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'Nguyen', 'Van A', 'Nguyen Van A', '0905123456', 'vana@example.com', '123456789', 'CMND', 'Việt Nam', 'Hà Nội', '1990-05-15', 'Nam', 'ACTIVE', NOW(), NOW()),
    (gen_random_uuid(), 'Tran',   'Thi B', 'Tran Thi B',   '0905234567', 'thib@example.com','987654321', 'CCCD', 'Việt Nam', 'TP. Hồ Chí Minh', '1992-07-20', 'Nữ', 'ACTIVE', NOW(), NOW());

-- ============================================================================
-- ROOM SERVICES (tham chiếu guest_id bằng email)
-- ============================================================================
INSERT INTO room_services (service_id, room_number, guest_id, guest_name, service_type, service_name, description, quantity, unit_price, total_price, status, created_at, updated_at)
VALUES
    (gen_random_uuid(), '301',
     (SELECT id FROM guests WHERE email='vana@example.com'),
     'Nguyen Van A', 'Spa',    'Massage toàn thân', 'Dịch vụ spa cao cấp 60 phút', 1, 1500000, 1500000, 'PENDING', NOW(), NOW()),
    (gen_random_uuid(), '201',
     (SELECT id FROM guests WHERE email='thib@example.com'),
     'Tran Thi B',   'Dining', 'Buffet tối',        'Buffet quốc tế tại nhà hàng khách sạn', 2, 800000, 1600000, 'PENDING', NOW(), NOW());
