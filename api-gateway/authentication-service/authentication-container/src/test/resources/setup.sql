-- src/test/resources/sql/OrderPaymentSagaTestSetUp.sql
-- Script này được chạy TRƯỚC mỗi lớp test (hoặc phương thức test)
-- Nó đảm bảo dữ liệu cơ bản cần thiết cho các test tồn tại.

-- Chèn các vai trò cơ bản nếu chúng chưa tồn tại
-- Sử dụng INSERT INTO ... ON CONFLICT DO NOTHING để tránh lỗi nếu các vai vai trò đã được init bởi init-data.sql
INSERT INTO authentication_service.roles (id, name) VALUES ('USER', 'ROLE_USER') ON CONFLICT (id) DO NOTHING;
INSERT INTO authentication_service.roles (id, name) VALUES ('ADMIN', 'ROLE_ADMIN') ON CONFLICT (id) DO NOTHING;

-- Bạn có thể thêm các câu lệnh INSERT khác ở đây để thiết lập dữ liệu cụ thể
-- mà các test case của bạn cần trước khi chạy.
-- Ví dụ:
-- INSERT INTO authentication_service.users (user_id, email, password, phone, role_id) VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'predefined@example.com', 'encoded_password', '123456789', 'USER');

-- Nếu các test của bạn yêu cầu dữ liệu cho "Order Payment Saga",
-- bạn sẽ thêm các INSERT statement vào các bảng order, payment, v.v. ở đây.
-- Ví dụ:
-- INSERT INTO your_order_table (...) VALUES (...);
-- INSERT INTO your_payment_table (...) VALUES (...);
