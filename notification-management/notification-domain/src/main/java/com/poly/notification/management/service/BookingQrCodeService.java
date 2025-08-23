package com.poly.notification.management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service đơn giản để tạo QR code cho booking và gửi email
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingQrCodeService {


    private final EmailService emailService;
    private final QrCodeService qrCodeService;
    /**
     * Tạo QR code với bookingId và gửi email
     *
     * @param bookingId ID của booking
     * @param userEmail Email của user
     */
    public void createQrCodeAndSendEmail(String bookingId, String userEmail) {
        try {
            log.info("Bắt đầu tạo QR code cho booking: {}", bookingId);

            // Tạo QR code và upload lên Cloudinary
            String qrCodeImageUrl = qrCodeService.createSimpleQrCode(bookingId);
            log.info("Đã tạo QR code thành công cho booking: {} và upload lên Cloudinary", bookingId);

            // Gửi email với QR code URL
            sendBookingQrCodeEmail(userEmail, bookingId, qrCodeImageUrl);

            log.info("Đã gửi email thành công cho user: {} với booking: {}", userEmail, bookingId);

        } catch (Exception e) {
            log.error("Lỗi khi tạo QR code và gửi email cho booking: {}", bookingId, e);
            throw new RuntimeException("Không thể tạo QR code và gửi email", e);
        }
    }

    /**
     * Gửi email với QR code URL từ Cloudinary
     */
    private void sendBookingQrCodeEmail(String userEmail, String bookingId, String qrCodeImageUrl) {
        String subject = "Xác nhận đặt phòng - QR Code";

        try {
            // Tạo variables cho template
            Map<String, Object> variables = new HashMap<>();
            variables.put("userEmail", userEmail);
            variables.put("bookingId", bookingId);
            variables.put("qrCodeImageUrl", qrCodeImageUrl);

            // Gửi email HTML sử dụng EmailService
            emailService.sendHtmlEmail(userEmail, subject, "booking-qr-code", variables);

        } catch (Exception e) {
            log.error("Lỗi khi gửi email HTML cho user: {}", userEmail, e);
            // Fallback: gửi email text đơn giản
            String textContent = String.format("Xác nhận đặt phòng thành công!\nMã đặt phòng: %s\nVui lòng kiểm tra email để xem QR code.", bookingId);
            emailService.sendSimpleEmail(userEmail, subject, textContent);
        }
    }
}
