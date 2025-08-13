


DELETE FROM services;

INSERT INTO services (service_id, service_name, description, price, availability) VALUES
    (1, 'Phòng đơn', 'Phòng đơn tiện nghi với giường cỡ lớn và phòng tắm riêng.', 100.00, 'Available'),
    (2, 'Phòng đôi', 'Phòng đôi rộng rãi với hai giường đơn hoặc một giường đôi lớn, có ban công.', 150.00, 'Available'),
    (3, 'Phòng gia đình', 'Phòng lớn phù hợp cho gia đình, bao gồm 2 phòng ngủ và khu vực sinh hoạt chung.', 250.00, 'Available'),
    (4, 'Dịch vụ giặt là', 'Dịch vụ giặt là nhanh chóng và chuyên nghiệp cho khách hàng.', 20.50, 'Available'),
    (5, 'Dịch vụ ăn sáng', 'Bữa sáng tự chọn phong phú với nhiều món ăn địa phương và quốc tế.', 15.00, 'Available'),
    (6, 'Thuê xe đạp', 'Thuê xe đạp để khám phá khu vực xung quanh khách sạn.', 10.00, 'Available'),
    (7, 'Spa & Massage', 'Các liệu pháp spa và massage thư giãn chuyên nghiệp.', 80.00, 'Available'),
    (8, 'Đưa đón sân bay', 'Dịch vụ đưa đón từ/đến sân bay theo yêu cầu.', 40.00, 'Available'),
    (9, 'Hồ bơi', 'Sử dụng hồ bơi trong nhà và ngoài trời của khách sạn.', 0.00, 'Available'),
    (10, 'Phòng họp', 'Phòng họp được trang bị đầy đủ tiện nghi cho các sự kiện kinh doanh.', 300.00, 'Unavailable');


ALTER SEQUENCE services_service_id_seq RESTART WITH 11;
