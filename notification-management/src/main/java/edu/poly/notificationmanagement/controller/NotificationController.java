package edu.poly.notificationmanagement.controller;

import edu.poly.notificationmanagement.model.Notification;
import edu.poly.notificationmanagement.service.NotificationService;
import edu.poly.notificationmanagement.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private OtpService otpService;

    /**
     * Lấy tất cả các thông báo hiện có trong hệ thống (dữ liệu mẫu).
     * http://localhost:8500/api/notifications
     * @return ResponseEntity chứa danh sách các đối tượng Notification.
     */
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Endpoint để gửi xác nhận đặt phòng qua email.
     * http://localhost:8500/api/notifications/send-booking-confirmation?userId=123&userEmail=tai30799@gmail.com
     * @param userId    ID của người dùng đặt phòng.
     * @param userEmail Email của người dùng để gửi xác nhận.
     * @return ResponseEntity chứa thông báo trạng thái gửi email.
     */
    @PostMapping("/send-booking-confirmation")
    public ResponseEntity<Map<String, String>> sendBookingConfirmation(
            @RequestParam int userId,
            @RequestParam String userEmail) {
        Map<String, String> response = new HashMap<>();
        try {
            String statusMessage = notificationService.sendBookingConfirmation(userId, userEmail, null);
            response.put("message", statusMessage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Lỗi khi gửi xác nhận đặt phòng: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint để gửi thông báo hủy đặt phòng qua email.
     * http://localhost:8500/api/notifications/send-booking-cancellation?userId=123&userEmail=tai30799@gmail.com
     * @param userId    ID của người dùng hủy đặt phòng.
     * @param userEmail Email của người dùng để gửi thông báo hủy.
     * @return ResponseEntity chứa thông báo trạng thái gửi email.
     */
    @PostMapping("/send-booking-cancellation")
    public ResponseEntity<Map<String, String>> sendBookingCancellation(
            @RequestParam int userId,
            @RequestParam String userEmail) {
        Map<String, String> response = new HashMap<>();
        try {
            String statusMessage = notificationService.sendBookingCancellation(userId, userEmail, null);
            response.put("message", statusMessage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Lỗi khi gửi thông báo hủy đặt phòng: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint để gửi thông báo hoàn tiền qua email.
     * http://localhost:8500/api/notifications/send-refund-notification?userId=123&userEmail=tai30799@gmail.com&amount=500.000 VNĐ&bookingId=BK12345
     * @param userId    ID của người dùng được hoàn tiền.
     * @param userEmail Email của người dùng để gửi thông báo hoàn tiền.
     * @param amount    Số tiền đã hoàn.
     * @param bookingId ID đặt phòng liên quan đến việc hoàn tiền.
     * @return ResponseEntity chứa thông báo trạng thái gửi email.
     */
    @PostMapping("/send-refund-notification")
    public ResponseEntity<Map<String, String>> sendRefundNotification(
            @RequestParam int userId,
            @RequestParam String userEmail,
            @RequestParam String amount,
            @RequestParam String bookingId) {
        Map<String, String> response = new HashMap<>();
        try {
            String statusMessage = notificationService.sendRefundNotification(userId, userEmail, amount, bookingId);
            response.put("message", statusMessage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Lỗi khi gửi thông báo hoàn tiền: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint để gửi thông báo nhắc nhở đặt phòng sắp tới qua email.
     * http://localhost:8500/api/notifications/send-booking-reminder?userId=123&userEmail=tai30799@gmail.com&bookingId=REMINDER6789&checkInDate=2024-12-31
     * @param userId    ID của người dùng.
     * @param userEmail Email của người dùng.
     * @param bookingId ID đặt phòng.
     * @param checkInDate Ngày nhận phòng.
     * @return ResponseEntity chứa thông báo trạng thái gửi email.
     */
    @PostMapping("/send-booking-reminder")
    public ResponseEntity<Map<String, String>> sendBookingReminder(
            @RequestParam int userId,
            @RequestParam String userEmail,
            @RequestParam String bookingId,
            @RequestParam String checkInDate) {
        Map<String, String> response = new HashMap<>();
        try {
            String statusMessage = notificationService.sendBookingReminder(userId, userEmail, bookingId, checkInDate);
            response.put("message", statusMessage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Lỗi khi gửi thông báo nhắc nhở đặt phòng: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint để gửi OTP đến email cho mục đích quên mật khẩu.
     * Frontend sẽ gọi API này khi người dùng yêu cầu đặt lại mật khẩu.
     *
     * @param email Email người dùng cần đặt lại mật khẩu.
     * @return ResponseEntity với thông báo và OTP (chỉ trong môi trường dev/test).
     */
    @PostMapping("/send-otp-for-password-reset")
    public ResponseEntity<Map<String, String>> sendOtpForPasswordReset(@RequestParam String userEmail) {
        Map<String, String> response = new HashMap<>();
        try {
            // Trong thực tế, bạn cần kiểm tra xem email này có tồn tại trong hệ thống không
            // if (!userService.isEmailRegistered(email)) {
            //     response.put("message", "Email không tồn tại trong hệ thống.");
            //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            // }

            String otp = otpService.generateAndSendOtp(userEmail);
            response.put("message", "Mã OTP đã được gửi đến " + userEmail + " để đặt lại mật khẩu.");
            response.put("otp", otp); // Cân nhắc bỏ trong production
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Lỗi khi gửi OTP đặt lại mật khẩu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint để gửi OTP đến email cho mục đích đổi mật khẩu (khi đã đăng nhập).
     * Frontend sẽ gọi API này khi người dùng muốn đổi mật khẩu và cần xác minh.
     *
     * @param email Email người dùng đang đăng nhập.
     * @return ResponseEntity với thông báo và OTP (chỉ trong môi trường dev/test).
     */
    @PostMapping("/send-otp-for-password-change")
    public ResponseEntity<Map<String, String>> sendOtpForPasswordChange(@RequestParam String email) {
        Map<String, String> response = new HashMap<>();
        try {
            // Trong thực tế, bạn cần xác minh người dùng đang đăng nhập có phải là chủ sở hữu email này không
            // Ví dụ: Lấy email từ Principal/SecurityContext
            // if (!currentUserEmail.equals(email)) {
            //     response.put("message", "Bạn không có quyền gửi OTP cho email này.");
            //     return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            // }

            String otp = otpService.generateAndSendOtp(email);
            response.put("message", "Mã OTP đã được gửi đến " + email + " để xác minh đổi mật khẩu.");
            response.put("otp", otp); // Cân nhắc bỏ trong production
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Lỗi khi gửi OTP đổi mật khẩu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    /**
     * Endpoint để gửi OTP đến email.
     *
     * @param email Email người dùng.
     * @return ResponseEntity với thông báo và OTP (trong môi trường phát triển/test) hoặc chỉ thông báo.
     */
    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestParam String email) {
        Map<String, String> response = new HashMap<>();
        try {
            String otp = otpService.generateAndSendOtp(email);
            response.put("message", "Mã OTP đã được gửi đến " + email);
            response.put("otp", otp); // Chỉ nên trả về OTP trong môi trường dev/test, không phải production
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Lỗi khi gửi OTP: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    /**
     * Endpoint để xác thực OTP. API này có thể dùng chung cho cả quên mật khẩu và đổi mật khẩu.
     *
     * @param email Email người dùng.
     * @param otp   Mã OTP cần xác thực.
     * @return ResponseEntity với kết quả xác thực.
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        Map<String, String> response = new HashMap<>();
        if (otpService.validateOtp(email, otp)) {
            response.put("message", "Xác thực OTP thành công!");
            // Sau khi xác thực thành công, frontend có thể chuyển sang bước đặt mật khẩu mới.
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Mã OTP không hợp lệ hoặc đã hết hạn.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Endpoint để gửi thông báo xác nhận đặt lại mật khẩu thành công.
     * API này sẽ được gọi sau khi người dùng đã đặt mật khẩu mới thành công.
     *
     * @param userId    ID của người dùng.
     * @param userEmail Email của người dùng.
     * @return ResponseEntity chứa thông báo trạng thái gửi email.
     */
    @PostMapping("/send-password-reset-success")
    public ResponseEntity<Map<String, String>> sendPasswordResetSuccess(
            @RequestParam int userId,
            @RequestParam String userEmail) {
        Map<String, String> response = new HashMap<>();
        try {
            String statusMessage = notificationService.sendPasswordResetSuccess(userId, userEmail);
            response.put("message", statusMessage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Lỗi khi gửi thông báo đặt lại mật khẩu thành công: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint để gửi thông báo xác nhận đổi mật khẩu thành công.
     * API này sẽ được gọi sau khi người dùng đã đổi mật khẩu thành công (khi đã đăng nhập).
     *
     * @param userId    ID của người dùng.
     * @param userEmail Email của người dùng.
     * @return ResponseEntity chứa thông báo trạng thái gửi email.
     */
    @PostMapping("/send-password-change-success")
    public ResponseEntity<Map<String, String>> sendPasswordChangeSuccess(
            @RequestParam int userId,
            @RequestParam String userEmail) {
        Map<String, String> response = new HashMap<>();
        try {
            String statusMessage = notificationService.sendPasswordChangeSuccess(userId, userEmail);
            response.put("message", statusMessage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Lỗi khi gửi thông báo đổi mật khẩu thành công: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
