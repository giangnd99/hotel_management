package com.poly.notification.management.controller;

import com.poly.notification.management.entity.Notification;
import com.poly.notification.management.service.NotificationService;
import com.poly.notification.management.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Notification Management", description = "APIs for managing and sending various notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private OtpService otpService;


    @Operation(summary = "Get all existing notifications",
            description = "Retrieves a list of sample notifications currently in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of notifications",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Notification.class)))
            })
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }


    @Operation(summary = "Send booking confirmation email",
            description = "Sends a booking confirmation email to the specified user.",
            parameters = {
                    @Parameter(name = "userId", description = "ID of the user making the booking", required = true, example = "1"),
                    @Parameter(name = "userEmail", description = "Email address of the user to send confirmation to", required = true, example = "tai30799@gmail.com")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email sent successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Email xác nhận đã gửi thành công tới tai30799@gmail.com\"}"))),
                    @ApiResponse(responseCode = "500", description = "Error sending email",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Lỗi khi gửi xác nhận đặt phòng: [error details]\"}")))
            })
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


    @Operation(summary = "Send booking cancellation email",
            description = "Sends a booking cancellation email to the specified user.",
            parameters = {
                    @Parameter(name = "userId", description = "ID of the user cancelling the booking", required = true, example = "123"),
                    @Parameter(name = "userEmail", description = "Email address of the user to send cancellation notification to", required = true, example = "tai30799@gmail.com")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email sent successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Email hủy đặt phòng đã gửi thành công tới tai30799@gmail.com\"}"))),
                    @ApiResponse(responseCode = "500", description = "Error sending email",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Lỗi khi gửi thông báo hủy đặt phòng: [error details]\"}")))
            })
    @PostMapping("/send-booking-cancellation")
    public ResponseEntity<Map<String, String>> sendBookingCancellation(
            @RequestParam String userId,
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


    @Operation(summary = "Send refund notification email",
            description = "Sends a refund notification email to the specified user.",
            parameters = {
                    @Parameter(name = "userId", description = "ID of the user receiving the refund", required = true, example = "123"),
                    @Parameter(name = "userEmail", description = "Email address of the user to send refund notification to", required = true, example = "tai30799@gmail.com"),
                    @Parameter(name = "amount", description = "Amount refunded (e.g., '500.000 VNĐ')", required = true, example = "500.000 VNĐ"),
                    @Parameter(name = "bookingId", description = "Booking ID associated with the refund", required = true, example = "BK12345")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email sent successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Email thông báo hoàn tiền đã gửi thành công tới tai30799@gmail.com\"}"))),
                    @ApiResponse(responseCode = "500", description = "Error sending email",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Lỗi khi gửi thông báo hoàn tiền: [error details]\"}")))
            })
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


    @Operation(summary = "Send booking reminder email",
            description = "Sends a reminder email for an upcoming booking to the specified user.",
            parameters = {
                    @Parameter(name = "userId", description = "ID of the user", required = true, example = "123"),
                    @Parameter(name = "userEmail", description = "Email address of the user", required = true, example = "tai30799@gmail.com"),
                    @Parameter(name = "bookingId", description = "Booking ID for the reminder", required = true, example = "REMINDER6789"),
                    @Parameter(name = "checkInDate", description = "Check-in date of the booking (e.g., YYYY-MM-DD)", required = true, example = "2024-12-31")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email sent successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Email nhắc nhở đặt phòng đã gửi thành công tới tai30799@gmail.com\"}"))),
                    @ApiResponse(responseCode = "500", description = "Error sending email",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Lỗi khi gửi thông báo nhắc nhở đặt phòng: [error details]\"}")))
            })
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


    @Operation(summary = "Send OTP for password reset",
            description = "Generates and sends an OTP to the user's email for password reset purposes. OTP is returned in dev/test environments.",
            parameters = {
                    @Parameter(name = "userEmail", description = "Email address of the user requesting password reset", required = true, example = "tai30799@gmail.com")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "OTP sent successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Mã OTP đã được gửi đến tai30799@gmail.com để đặt lại mật khẩu.\", \"otp\": \"123456\"}"))),
                    @ApiResponse(responseCode = "500", description = "Error sending OTP",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Lỗi khi gửi OTP đặt lại mật khẩu: [error details]\"}")))
            })
    @PostMapping("/send-otp-for-password-reset")
    public ResponseEntity<Map<String, String>> sendOtpForPasswordReset(@RequestParam String userEmail) {
        Map<String, String> response = new HashMap<>();
        try {
            String otp = otpService.generateAndSendOtp(userEmail);
            response.put("message", "Mã OTP đã được gửi đến " + userEmail + " để đặt lại mật khẩu.");
            response.put("otp", otp); // Cân nhắc bỏ trong production
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Lỗi khi gửi OTP đặt lại mật khẩu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Send OTP for password change",
            description = "Generates and sends an OTP to the user's email for password change verification. OTP is returned in dev/test environments.",
            parameters = {
                    @Parameter(name = "userEmail", description = "Email address of the logged-in user", required = true, example = "tai30799@gmail.com")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "OTP sent successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Mã OTP đã được gửi đến tai30799@gmail.com để xác minh đổi mật khẩu.\", \"otp\": \"654321\"}"))),
                    @ApiResponse(responseCode = "500", description = "Error sending OTP",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Lỗi khi gửi OTP đổi mật khẩu: [error details]\"}")))
            })
    @PostMapping("/send-otp-for-password-change")
    public ResponseEntity<Map<String, String>> sendOtpForPasswordChange(@RequestParam String userEmail) {
        Map<String, String> response = new HashMap<>();
        try {
            String otp = otpService.generateAndSendOtp(userEmail);
            response.put("message", "Mã OTP đã được gửi đến " + userEmail + " để xác minh đổi mật khẩu.");
            response.put("otp", otp); // Cân nhắc bỏ trong production
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Lỗi khi gửi OTP đổi mật khẩu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Send generic OTP",
            description = "Generates and sends a generic OTP to the specified email address. OTP is returned in dev/test environments.",
            parameters = {
                    @Parameter(name = "userEmail", description = "Email address to send OTP to", required = true, example = "tai30799@gmail.com")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "OTP sent successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Mã OTP đã được gửi đến tai30799@gmail.com\", \"otp\": \"987654\"}"))),
                    @ApiResponse(responseCode = "500", description = "Error sending OTP",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Lỗi khi gửi OTP: [error details]\"}")))
            })
    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestParam String userEmail) {
        Map<String, String> response = new HashMap<>();
        try {
            String otp = otpService.generateAndSendOtp(userEmail);
            response.put("message", "Mã OTP đã được gửi đến " + userEmail);
            response.put("otp", otp); // Chỉ nên trả về OTP trong môi trường dev/test, không phải production
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Lỗi khi gửi OTP: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @Operation(summary = "Verify OTP",
            description = "Verifies the provided OTP for a given email address. Can be used for password reset or change.",
            parameters = {
                    @Parameter(name = "userEmail", description = "Email address associated with the OTP", required = true, example = "tai30799@gmail.com"),
                    @Parameter(name = "otp", description = "The OTP to verify", required = true, example = "437923")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "OTP verified successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Xác thực OTP thành công!\"}"))),
                    @ApiResponse(responseCode = "400", description = "Invalid or expired OTP",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Mã OTP không hợp lệ hoặc đã hết hạn.\"}")))
            })
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestParam String userEmail, @RequestParam String otp) {
        Map<String, String> response = new HashMap<>();
        if (otpService.validateOtp(userEmail, otp)) {
            response.put("message", "Xác thực OTP thành công!");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Mã OTP không hợp lệ hoặc đã hết hạn.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Send password reset success notification",
            description = "Sends an email notification confirming successful password reset.",
            parameters = {
                    @Parameter(name = "userId", description = "ID of the user", required = true, example = "123"),
                    @Parameter(name = "userEmail", description = "Email address of the user", required = true, example = "tai30799@gmail.com")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email sent successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Email thông báo đặt lại mật khẩu thành công đã gửi tới tai30799@gmail.com\"}"))),
                    @ApiResponse(responseCode = "500", description = "Error sending email",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Lỗi khi gửi thông báo đặt lại mật khẩu thành công: [error details]\"}")))
            })
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

    @Operation(summary = "Send password change success notification",
            description = "Sends an email notification confirming successful password change.",
            parameters = {
                    @Parameter(name = "userId", description = "ID of the user", required = true, example = "123"),
                    @Parameter(name = "userEmail", description = "Email address of the user", required = true, example = "tai30799@gmail.com")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email sent successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Email thông báo đổi mật khẩu thành công đã gửi tới tai30799@gmail.com\"}"))),
                    @ApiResponse(responseCode = "500", description = "Error sending email",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Lỗi khi gửi thông báo đổi mật khẩu thành công: [error details]\"}")))
            })
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
    @PostMapping("/send-account-info")
    public ResponseEntity<Map<String, String>> sendAccountInfo(
            @RequestParam String userEmail,
            @RequestParam String password) {
        Map<String, String> response = new HashMap<>();
        try {
            String statusMessage = notificationService.sendAccountInfo( userEmail, password, null, null);
            response.put("message", statusMessage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Lỗi khi gửi email thông tin tài khoản: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
