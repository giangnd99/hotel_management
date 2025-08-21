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

    private final CloudinaryQrCodeService cloudinaryQrCodeService;
    private final EmailService emailService;

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
            String qrCodeImageUrl = cloudinaryQrCodeService.createSimpleQrCodeAndUpload(bookingId);
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

    /**
     * Tạo template email HTML đẹp với QR code từ Cloudinary
     */
    private String createEmailTemplate(String userName, String bookingId, String qrCodeImageUrl) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Xác nhận đặt phòng</title>
                    <style>
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            line-height: 1.6;
                            color: #333;
                            max-width: 600px;
                            margin: 0 auto;
                            background-color: #f4f4f4;
                        }
                        .container {
                            background-color: #ffffff;
                            padding: 30px;
                            border-radius: 10px;
                            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                            margin: 20px;
                        }
                        .header {
                            text-align: center;
                            border-bottom: 3px solid #007bff;
                            padding-bottom: 20px;
                            margin-bottom: 30px;
                        }
                        .hotel-name {
                            color: #007bff;
                            font-size: 28px;
                            font-weight: bold;
                            margin: 0;
                        }
                        .subtitle {
                            color: #666;
                            font-size: 16px;
                            margin: 10px 0 0 0;
                        }
                        .greeting {
                            font-size: 18px;
                            color: #333;
                            margin-bottom: 20px;
                        }
                        .booking-info {
                            background-color: #f8f9fa;
                            padding: 20px;
                            border-radius: 8px;
                            border-left: 4px solid #28a745;
                            margin: 20px 0;
                        }
                        .booking-id {
                            font-size: 20px;
                            font-weight: bold;
                            color: #28a745;
                            margin: 0 0 10px 0;
                        }
                        .qr-section {
                            text-align: center;
                            margin: 30px 0;
                            padding: 20px;
                            background-color: #f8f9fa;
                            border-radius: 8px;
                        }
                        .qr-title {
                            font-size: 18px;
                            font-weight: bold;
                            color: #333;
                            margin-bottom: 15px;
                        }
                        .qr-instruction {
                            color: #666;
                            font-size: 14px;
                            margin-bottom: 20px;
                        }
                        .qr-code {
                            border: 2px solid #ddd;
                            border-radius: 8px;
                            padding: 10px;
                            background-color: white;
                            display: inline-block;
                        }
                        .footer {
                            text-align: center;
                            margin-top: 30px;
                            padding-top: 20px;
                            border-top: 1px solid #ddd;
                            color: #666;
                            font-size: 14px;
                        }
                        .contact-info {
                            background-color: #e9ecef;
                            padding: 15px;
                            border-radius: 5px;
                            margin: 20px 0;
                        }
                        .highlight {
                            color: #007bff;
                            font-weight: bold;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1 class="hotel-name">🏨 Luxury Hotel</h1>
                            <p class="subtitle">Trải nghiệm đẳng cấp - Dịch vụ hoàn hảo</p>
                        </div>
                
                        <div class="greeting">
                            Xin chào <span class="highlight">%s</span>,
                        </div>
                
                        <p>Cảm ơn bạn đã chọn Luxury Hotel cho chuyến du lịch sắp tới. Chúng tôi rất vui mừng xác nhận đặt phòng của bạn đã được hoàn tất thành công!</p>
                
                        <div class="booking-info">
                            <h3 class="booking-id">📋 Mã đặt phòng: %s</h3>
                            <p>Vui lòng giữ mã này để sử dụng khi check-in tại khách sạn.</p>
                        </div>
                
                        <div class="qr-section">
                            <h3 class="qr-title">📱 QR Code Check-in</h3>
                            <p class="qr-instruction">Quét mã QR bên dưới để nhanh chóng hoàn tất thủ tục check-in:</p>
                            <div class="qr-code">
                                <img src="%s" alt="QR Code" style="width: 200px; height: 200px;">
                            </div>
                            <p style="font-size: 12px; color: #999; margin-top: 10px;">
                                Mã QR này chứa thông tin đặt phòng của bạn
                            </p>
                        </div>
                
                        <div class="contact-info">
                            <h4>📞 Thông tin liên hệ:</h4>
                            <p><strong>Điện thoại:</strong> +84 123 456 789</p>
                            <p><strong>Email:</strong> info@luxuryhotel.com</p>
                            <p><strong>Địa chỉ:</strong> 123 Đường ABC, Quận 1, TP.HCM</p>
                        </div>
                
                        <p><strong>Lưu ý quan trọng:</strong></p>
                        <ul>
                            <li>Vui lòng đến khách sạn 15 phút trước giờ check-in</li>
                            <li>Mang theo giấy tờ tùy thân gốc để xác minh</li>
                            <li>Thời gian check-in: 14:00 - 24:00</li>
                            <li>Thời gian check-out: 06:00 - 12:00</li>
                        </ul>
                
                        <p>Chúng tôi rất mong được chào đón bạn tại Luxury Hotel!</p>
                
                        <div class="footer">
                            <p>Trân trọng,<br>
                            <strong>Đội ngũ Luxury Hotel</strong></p>
                            <p style="font-size: 12px;">
                                Email này được gửi tự động, vui lòng không trả lời.<br>
                                Nếu có thắc mắc, vui lòng liên hệ trực tiếp với chúng tôi.
                            </p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(userName, bookingId, qrCodeImageUrl);
    }
}
