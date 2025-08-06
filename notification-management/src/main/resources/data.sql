

CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    notification_method VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    priority VARCHAR(20),
    sent_date DATETIME,
    status VARCHAR(20)
    );


INSERT INTO notifications (user_id, notification_method, message, priority, sent_date, status) VALUES
    (101, 'Email', 'Chào mừng đến với niko!', 'High', NOW(), 'Sent'),
    (102, 'SMS', 'Đặt phòng thành công.', 'Medium', NOW(), 'Sent'),
    (103, 'Push', 'khuyen mãi 50%!', 'Low', NOW(), 'Sent'),
    (104, 'Email', 'Đổi mật khẩu thành công.', 'High', NOW(), 'Sent'),
    (105, 'SMS', 'bạn đã đặt hàng thành công', 'Medium', NOW(), 'Sent'),
    (106, 'Push', 'Khuyến mãi hết hạn vào ngày 30-7-2025', 'Low', NOW(), 'Sent'),
    (108, 'Email', 'Cảm ơn bạn đã feedback!', 'Low', NOW(), 'Sent');