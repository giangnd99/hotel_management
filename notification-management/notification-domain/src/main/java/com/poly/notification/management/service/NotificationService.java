package com.poly.notification.management.service;

import com.poly.notification.management.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {
    @Autowired
    private EmailService emailService;

    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        return notifications;
    }


    public String sendBookingConfirmation(int userId, String userEmail, String userPhoneNumber) {
        String emailStatus = "Email not sent.";
        try {
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("subject", "Xác Nhận Đặt Phòng Khách Sạn Thành Công!");
            emailVariables.put("userName", "Khách Hàng " + userId);
            emailVariables.put("mainMessage", "Đơn đặt phòng của bạn đã được xác nhận thành công. Chúng tôi rất mong được chào đón bạn!");

            Map<String, String> bookingDetails = new HashMap<>();
            bookingDetails.put("Mã đặt phòng", "BK" + System.currentTimeMillis()); // Mã đặt phòng giả định
            bookingDetails.put("Ngày đặt", new Date().toString());
            bookingDetails.put("Loại phòng", "Phòng Deluxe");
            bookingDetails.put("Tổng tiền", "2.500.000 VNĐ");
            emailVariables.put("bookingDetails", bookingDetails);

            emailVariables.put("actionLink", "http://localhost:8080/my-bookings/" + userId);

            emailService.sendHtmlEmail(userEmail, (String) emailVariables.get("subject"), "email-template", emailVariables);
            emailStatus = "Email xác nhận đã gửi thành công tới " + userEmail;
        } catch (Exception e) {
            emailStatus = "Lỗi gửi email xác nhận: " + e.getMessage();
            System.err.println(emailStatus);
            e.printStackTrace();
        }
        return emailStatus;
    }


    public String sendBookingCancellation(String userId, String userEmail, String bookingId) {
        String emailStatus = "Email not sent.";
        try {
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("subject", "Thông Báo Hủy Đặt Phòng Khách Sạn");
            emailVariables.put("userName", "Khách Hàng " + userId);
            emailVariables.put("mainMessage", "Đơn đặt phòng của bạn đã được hủy thành công. Chúng tôi hy vọng sẽ được phục vụ bạn trong tương lai.");

             Map<String, String> bookingDetails = new HashMap<>();
             bookingDetails.put("Mã đặt phòng đã hủy", "BK_CANCELED_" + bookingId);
             emailVariables.put("bookingDetails", bookingDetails);

            emailService.sendHtmlEmail(userEmail, (String) emailVariables.get("subject"), "email-template", emailVariables);
            emailStatus = "Email hủy đặt phòng đã gửi thành công tới " + userEmail;
        } catch (Exception e) {
            emailStatus = "Lỗi gửi email hủy đặt phòng: " + e.getMessage();
            System.err.println(emailStatus);
            e.printStackTrace();
        }
        return emailStatus;
    }

    public String sendRefundNotification(int userId, String userEmail, String amount, String bookingId) {
        String emailStatus = "Email not sent.";
        try {
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("subject", "Thông Báo Hoàn Tiền Thành Công");
            emailVariables.put("userName", "Khách Hàng " + userId);
            emailVariables.put("mainMessage", "Yêu cầu hoàn tiền cho đặt phòng " + bookingId + " của bạn đã được xử lý thành công. Số tiền " + amount + " đã được hoàn trả.");

            Map<String, String> refundDetails = new HashMap<>();
            refundDetails.put("Số tiền hoàn", amount);
            refundDetails.put("Mã đặt phòng", bookingId);
            refundDetails.put("Ngày hoàn tiền", new Date().toString());
            emailVariables.put("bookingDetails", refundDetails);

            emailService.sendHtmlEmail(userEmail, (String) emailVariables.get("subject"), "email-template", emailVariables);
            emailStatus = "Email thông báo hoàn tiền đã gửi thành công tới " + userEmail;
        } catch (Exception e) {
            emailStatus = "Lỗi gửi email thông báo hoàn tiền: " + e.getMessage();
            System.err.println(emailStatus);
            e.printStackTrace();
        }
        return emailStatus;
    }

    /**
     * Gửi thông báo nhắc nhở đặt phòng sắp tới qua Email.
     *
     * @param userId      ID người dùng.
     * @param userEmail   Email người dùng.
     * @param bookingId   ID đặt phòng.
     * @param checkInDate Ngày nhận phòng.
     * @return Trạng thái gửi thông báo Email.
     */
    public String sendBookingReminder(int userId, String userEmail, String bookingId, String checkInDate) {
        String emailStatus = "Email not sent.";
        try {
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("subject", "Nhắc Nhở Đặt Phòng Sắp Tới Của Bạn");
            emailVariables.put("userName", "Khách Hàng " + userId);
            emailVariables.put("mainMessage", "Đây là lời nhắc nhở về đặt phòng của bạn (Mã: " + bookingId + ") vào ngày " + checkInDate + ". Chúng tôi rất mong được chào đón bạn!");

            Map<String, String> reminderDetails = new HashMap<>();
            reminderDetails.put("Mã đặt phòng", bookingId);
            reminderDetails.put("Ngày nhận phòng", checkInDate);
            emailVariables.put("bookingDetails", reminderDetails); // Tái sử dụng bookingDetails

            emailVariables.put("actionLink", "http://localhost:8080/my-bookings/" + userId);

            emailService.sendHtmlEmail(userEmail, (String) emailVariables.get("subject"), "email-template", emailVariables);
            emailStatus = "Email nhắc nhở đặt phòng đã gửi thành công tới " + userEmail;
        } catch (Exception e) {
            emailStatus = "Lỗi gửi email nhắc nhở đặt phòng: " + e.getMessage();
            System.err.println(emailStatus);
            e.printStackTrace();
        }
        return emailStatus;
    }

    /**
     * Gửi thông báo xác nhận đặt lại mật khẩu thành công qua Email.
     *
     * @param userId    ID người dùng.
     * @param userEmail Email người dùng.
     * @return Trạng thái gửi thông báo Email.
     */
    public String sendPasswordResetSuccess(int userId, String userEmail) {
        String emailStatus = "Email not sent.";
        try {
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("subject", "Thông Báo: Mật Khẩu Của Bạn Đã Được Đặt Lại Thành Công!");
            emailVariables.put("userName", "Khách Hàng " + userId);
            emailVariables.put("mainMessage", "Mật khẩu tài khoản của bạn đã được đặt lại thành công. Nếu bạn không thực hiện hành động này, vui lòng liên hệ hỗ trợ ngay lập tức.");

            emailVariables.put("actionLink", "http://localhost:8080/login"); // Link đến trang đăng nhập

            emailService.sendHtmlEmail(userEmail, (String) emailVariables.get("subject"), "email-template", emailVariables);
            emailStatus = "Email thông báo đặt lại mật khẩu thành công đã gửi tới " + userEmail;
        } catch (Exception e) {
            emailStatus = "Lỗi gửi email thông báo đặt lại mật khẩu thành công: " + e.getMessage();
            System.err.println(emailStatus);
            e.printStackTrace();
        }
        return emailStatus;
    }

    /**
     * Gửi thông báo xác nhận đổi mật khẩu thành công qua Email.
     *
     * @param userId    ID người dùng.
     * @param userEmail Email người dùng.
     * @return Trạng thái gửi thông báo Email.
     */
    public String sendPasswordChangeSuccess(int userId, String userEmail) {
        String emailStatus = "Email not sent.";
        try {
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("subject", "Thông Báo: Mật Khẩu Của Bạn Đã Được Đổi Thành Công!");
            emailVariables.put("userName", "Khách Hàng " + userId);
            emailVariables.put("mainMessage", "Mật khẩu tài khoản của bạn đã được đổi thành công. Nếu bạn không thực hiện hành động này, vui lòng liên hệ hỗ trợ ngay lập tức.");

            emailVariables.put("actionLink", "http://localhost:8080/profile"); // Link đến trang hồ sơ hoặc cài đặt

            emailService.sendHtmlEmail(userEmail, (String) emailVariables.get("subject"), "email-template", emailVariables);
            emailStatus = "Email thông báo đổi mật khẩu thành công đã gửi tới " + userEmail;
        } catch (Exception e) {
            emailStatus = "Lỗi gửi email thông báo đổi mật khẩu thành công: " + e.getMessage();
            System.err.println(emailStatus);
            e.printStackTrace();
        }
        return emailStatus;
    }
    /**
     * Gửi thông tin tài khoản (email và mật khẩu) đến người dùng qua Email.
     * Sử dụng template Confirm-account-email.html.
     *
     * @param userEmail Email của tài khoản mới.
     * @param password Mật khẩu của tài khoản mới.
     * @return Trạng thái gửi thông báo Email.
     */
    public String sendAccountInfo( String userEmail, String password, String userName, String loginLink) {
        String emailStatus = "Email not sent.";
        try {
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("subject", "Thông Tin Tài Khoản Của Bạn Đã Được Tạo!");
            emailVariables.put("userName", userName != null && !userName.isEmpty() ? userName : "Quý Khách"); // Sử dụng tên nếu có, nếu không thì dùng "Quý Khách"
            emailVariables.put("email", userEmail);
            emailVariables.put("password", password);
            if (loginLink != null && !loginLink.isEmpty()) {
                emailVariables.put("loginLink", loginLink);
            }
            // Tên template cần khớp với tên file HTML (không có phần mở rộng .html)
            emailService.sendHtmlEmail(userEmail, (String) emailVariables.get("subject"), "Confirm-account-email", emailVariables);
            emailStatus = "Email thông tin tài khoản đã được gửi đến " + userEmail;
        } catch (Exception e) {
            emailStatus = "Lỗi khi gửi email thông tin tài khoản: " + e.getMessage();
            System.err.println(emailStatus);
            e.printStackTrace();
        }
        return emailStatus;
    }
}
