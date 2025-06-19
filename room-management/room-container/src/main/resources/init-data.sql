-- Insert MaintenanceTypeEntity records
INSERT INTO maintenance_type_entity (maintenance_type_id, name) VALUES
(1, 'Regular Cleaning'),
(2, 'Equipment Repair'),
(3, 'Furniture Replacement'),
(4, 'Plumbing Service'),
(5, 'AC Maintenance');

-- Insert RoomStatusEntity records
INSERT INTO room_status_entity (status_id, status_name) VALUES
(1, 'Available'),
(2, 'Occupied'),
(3, 'Under Maintenance'),
(4, 'Out of Service'),
(5, 'Reserved');

-- Insert RoomTypeEntity records
INSERT INTO room_type_entity (room_type_id, type_name, description, base_price, max_occupancy) VALUES
(1, 'Standard Single', 'Comfortable room with single bed', 100.00, 1),
(2, 'Standard Double', 'Spacious room with double bed', 150.00, 2),
(3, 'Deluxe', 'Luxury room with premium amenities', 250.00, 2),
(4, 'Suite', 'Large suite with separate living area', 400.00, 4),
(5, 'Family Room', 'Spacious room for family stay', 350.00, 6);

-- Insert FurnitureEntity records
INSERT INTO furniture_entity (furniture_id, inventory_item_id) VALUES
(1, 'BED-001'),
(2, 'CHAIR-001'),
(3, 'TABLE-001'),
(4, 'DESK-001'),
(5, 'CABINET-001');

-- Insert RoomTypeFurnitureEntity records
INSERT INTO room_type_furniture_entity (room_type_id, furniture_id, require_quantity) VALUES
(1, 1, 1),
(1, 2, 2),
(2, 1, 1),
(3, 3, 1),
(4, 4, 1);

-- Insert RoomEntity records
INSERT INTO room_entity (room_id, room_type_id, status_id, room_number, floor) VALUES
(1, 1, 1, '101', 1),
(2, 2, 2, '201', 2),
(3, 3, 1, '301', 3),
(4, 4, 3, '401', 4),
(5, 5, 1, '501', 5);

-- Insert RoomMaintenanceEntity records
INSERT INTO room_maintenance_entity (maintenance_id, room_id, staff_id, maintenance_date, maintenance_type_id, description, status) VALUES
(1, 1, 'STAFF001', '2025-06-14 09:00:00', 1, 'Regular room cleaning', 'COMPLETED'),
(2, 2, 'STAFF002', '2025-06-14 10:30:00', 2, 'Fix broken AC', 'IN_PROGRESS'),
(3, 3, 'STAFF003', '2025-06-14 11:45:00', 3, 'Replace old furniture', 'SCHEDULED'),
(4, 4, 'STAFF004', '2025-06-14 14:00:00', 4, 'Fix leaking faucet', 'COMPLETED'),
(5, 5, 'STAFF005', '2025-06-14 15:30:00', 5, 'AC filter cleaning', 'PENDING');