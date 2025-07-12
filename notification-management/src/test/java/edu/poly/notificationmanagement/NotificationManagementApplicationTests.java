package edu.poly.notificationmanagement;

import edu.poly.notificationmanagement.model.Notification;
import edu.poly.notificationmanagement.service.EmailService;
import edu.poly.notificationmanagement.service.OtpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationManagementApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @MockBean
    private OtpService otpService;

    @Test
    void contextLoads() {
        // Đảm bảo Spring context tải thành công
    }

    @Test
    void getAllNotifications_shouldReturnListOfNotifications() throws Exception {
        String expectedJson = "[" +
                "{\"notificationId\":1,\"userId\":101,\"notificationMethod\":\"Email\",\"message\":\"Chào mừng đến với niko!\",\"priority\":\"High\",\"sentDate\":*,\"status\":\"Sent\"}," +
                "{\"notificationId\":2,\"userId\":102,\"notificationMethod\":\"SMS\",\"message\":\"Đặt phòng thành công.\",\"priority\":\"Medium\",\"sentDate\":*,\"status\":\"Sent\"}," +
                "{\"notificationId\":3,\"userId\":103,\"notificationMethod\":\"Push\",\"message\":\"khuyen mãi 50%!\",\"priority\":\"Low\",\"sentDate\":*,\"status\":\"Sent\"}," +
                "{\"notificationId\":4,\"userId\":104,\"notificationMethod\":\"Email\",\"message\":\"Đổi mật khẩu thành công.\",\"priority\":\"High\",\"sentDate\":*,\"status\":\"Sent\"}," +
                "{\"notificationId\":5,\"userId\":105,\"notificationMethod\":\"SMS\",\"message\":\"bạn đã đặt hàng thành công\",\"priority\":\"Medium\",\"sentDate\":*,\"status\":\"Sent\"}," +
                "{\"notificationId\":6,\"userId\":106,\"notificationMethod\":\"Push\",\"message\":\"Khuyến mãi hết hạn vào ngày 30-7-2025\",\"priority\":\"Low\",\"sentDate\":*,\"status\":\"Sent\"}," +
                "{\"notificationId\":9,\"userId\":108,\"notificationMethod\":\"Email\",\"message\":\"Cảm ơn bạn đã feedback!\",\"priority\":\"Low\",\"sentDate\":*,\"status\":\"Sent\"}" +
                "]";

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, false));
    }

    @Test
    void sendBookingConfirmation_shouldReturnSuccessMessage() throws Exception {
        String userEmail = "test@example.com";
        int userId = 123;

        when(notificationService.sendBookingConfirmation(userId, userEmail, null)).thenReturn("Email xác nhận đã gửi thành công tới " + userEmail);

        mockMvc.perform(post("/api/notifications/send-booking-confirmation")
                        .param("userId", String.valueOf(userId))
                        .param("userEmail", userEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("Email xác nhận đã gửi thành công tới " + userEmail)));

        verify(notificationService, times(1)).sendBookingConfirmation(userId, userEmail, null);
    }

    @Test
    void sendBookingCancellation_shouldReturnSuccessMessage() throws Exception {
        String userEmail = "cancel@example.com";
        int userId = 456;

        when(notificationService.sendBookingCancellation(userId, userEmail, null)).thenReturn("Email hủy đặt phòng đã gửi thành công tới " + userEmail);

        mockMvc.perform(post("/api/notifications/send-booking-cancellation")
                        .param("userId", String.valueOf(userId))
                        .param("userEmail", userEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("Email hủy đặt phòng đã gửi thành công tới " + userEmail)));

        verify(notificationService, times(1)).sendBookingCancellation(userId, userEmail, null);
    }

    @Test
    void sendRefundNotification_shouldReturnSuccessMessage() throws Exception {
        String userEmail = "refund@example.com";
        int userId = 789;
        String amount = "1.000.000 VNĐ";
        String bookingId = "BK98765";

        when(notificationService.sendRefundNotification(userId, userEmail, amount, bookingId)).thenReturn("Email thông báo hoàn tiền đã gửi thành công tới " + userEmail);

        mockMvc.perform(post("/api/notifications/send-refund-notification")
                        .param("userId", String.valueOf(userId))
                        .param("userEmail", userEmail)
                        .param("amount", amount)
                        .param("bookingId", bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("Email thông báo hoàn tiền đã gửi thành công tới " + userEmail)));

        verify(notificationService, times(1)).sendRefundNotification(userId, userEmail, amount, bookingId);
    }

    @Test
    void sendBookingReminder_shouldReturnSuccessMessage() throws Exception {
        String userEmail = "reminder@example.com";
        int userId = 111;
        String bookingId = "BK22222";
        String checkInDate = "2024-12-25";

        when(notificationService.sendBookingReminder(userId, userEmail, bookingId, checkInDate)).thenReturn("Email nhắc nhở đặt phòng đã gửi thành công tới " + userEmail);

        mockMvc.perform(post("/api/notifications/send-booking-reminder")
                        .param("userId", String.valueOf(userId))
                        .param("userEmail", userEmail)
                        .param("bookingId", bookingId)
                        .param("checkInDate", checkInDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("Email nhắc nhở đặt phòng đã gửi thành công tới " + userEmail)));

        verify(notificationService, times(1)).sendBookingReminder(userId, userEmail, bookingId, checkInDate);
    }

    @Test
    void sendOtpForPasswordReset_shouldReturnOtpAndSuccessMessage() throws Exception {
        String email = "reset@example.com";
        String mockOtp = "654321";

        when(otpService.generateAndSendOtp(email)).thenReturn(mockOtp);

        mockMvc.perform(post("/api/notifications/send-otp-for-password-reset")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("Mã OTP đã được gửi đến " + email + " để đặt lại mật khẩu.")))
                .andExpect(jsonPath("$.otp").value(mockOtp));

        verify(otpService, times(1)).generateAndSendOtp(email);
    }

    @Test
    void sendOtpForPasswordReset_shouldReturnErrorMessage_whenServiceThrowsException() throws Exception {
        String email = "error_reset@example.com";

        when(otpService.generateAndSendOtp(email)).thenThrow(new RuntimeException("Mail server error for reset"));

        mockMvc.perform(post("/api/notifications/send-otp-for-password-reset")
                        .param("email", email))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(containsString("Lỗi khi gửi OTP đặt lại mật khẩu: Mail server error for reset")));

        verify(otpService, times(1)).generateAndSendOtp(email);
    }

    @Test
    void sendOtpForPasswordChange_shouldReturnOtpAndSuccessMessage() throws Exception {
        String email = "change@example.com";
        String mockOtp = "987654";

        when(otpService.generateAndSendOtp(email)).thenReturn(mockOtp);

        mockMvc.perform(post("/api/notifications/send-otp-for-password-change")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("Mã OTP đã được gửi đến " + email + " để xác minh đổi mật khẩu.")))
                .andExpect(jsonPath("$.otp").value(mockOtp));

        verify(otpService, times(1)).generateAndSendOtp(email);
    }

    @Test
    void sendOtpForPasswordChange_shouldReturnErrorMessage_whenServiceThrowsException() throws Exception {
        String email = "error_change@example.com";

        when(otpService.generateAndSendOtp(email)).thenThrow(new RuntimeException("Mail server error for change"));

        mockMvc.perform(post("/api/notifications/send-otp-for-password-change")
                        .param("email", email))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(containsString("Lỗi khi gửi OTP đổi mật khẩu: Mail server error for change")));

        verify(otpService, times(1)).generateAndSendOtp(email);
    }

    @Test
    void verifyOtp_shouldReturnSuccessMessage_whenOtpIsValid() throws Exception {
        String email = "valid@example.com";
        String otp = "123456";

        when(otpService.validateOtp(email, otp)).thenReturn(true);

        mockMvc.perform(post("/api/notifications/verify-otp")
                        .param("email", email)
                        .param("otp", otp))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Xác thực OTP thành công!"));

        verify(otpService, times(1)).validateOtp(email, otp);
    }

    @Test
    void verifyOtp_shouldReturnBadRequest_whenOtpIsInvalid() throws Exception {
        String email = "invalid@example.com";
        String otp = "wrong_otp";

        when(otpService.validateOtp(email, otp)).thenReturn(false);

        mockMvc.perform(post("/api/notifications/verify-otp")
                        .param("email", email)
                        .param("otp", otp))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Mã OTP không hợp lệ hoặc đã hết hạn."));

        verify(otpService, times(1)).validateOtp(email, otp);
    }

    @Test
    void sendPasswordResetSuccess_shouldReturnSuccessMessage() throws Exception {
        String userEmail = "reset_success@example.com";
        int userId = 333;

        when(notificationService.sendPasswordResetSuccess(userId, userEmail)).thenReturn("Email thông báo đặt lại mật khẩu thành công đã gửi tới " + userEmail);

        mockMvc.perform(post("/api/notifications/send-password-reset-success")
                        .param("userId", String.valueOf(userId))
                        .param("userEmail", userEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("Email thông báo đặt lại mật khẩu thành công đã gửi tới " + userEmail)));

        verify(notificationService, times(1)).sendPasswordResetSuccess(userId, userEmail);
    }

    @Test
    void sendPasswordChangeSuccess_shouldReturnSuccessMessage() throws Exception {
        String userEmail = "change_success@example.com";
        int userId = 444;

        when(notificationService.sendPasswordChangeSuccess(userId, userEmail)).thenReturn("Email thông báo đổi mật khẩu thành công đã gửi tới " + userEmail);

        mockMvc.perform(post("/api/notifications/send-password-change-success")
                        .param("userId", String.valueOf(userId))
                        .param("userEmail", userEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("Email thông báo đổi mật khẩu thành công đã gửi tới " + userEmail)));

        verify(notificationService, times(1)).sendPasswordChangeSuccess(userId, userEmail);
    }
}
