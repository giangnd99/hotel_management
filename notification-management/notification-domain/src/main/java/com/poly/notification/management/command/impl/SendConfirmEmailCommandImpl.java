package com.poly.notification.management.command.impl;

import com.poly.notification.management.command.SendBookingConfirmCommand;
import com.poly.notification.management.message.MessageStatus;
import com.poly.notification.management.message.NotificationMessage;
import com.poly.notification.management.port.out.publisher.BookingConfirmedEmailResponsePublisher;
import com.poly.notification.management.service.BookingQrCodeService;
import com.poly.notification.management.service.CloudinaryQrCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendConfirmEmailCommandImpl implements SendBookingConfirmCommand {

    private final BookingQrCodeService bookingQrCodeService;
    private final BookingConfirmedEmailResponsePublisher bookingConfirmedEmailResponsePublisher;

    @Override
    public void sendEmailConfirm(NotificationMessage message) {
        try {
            log.info("Bắt đầu xử lý xác nhận đặt phòng cho booking id: {}", message.getBookingId());

            // Lấy thông tin từ notification
            String bookingId = message.getBookingId(); 
            String userEmail = message.getCustomerEmail();

            // Tạo QR code và gửi email
            bookingQrCodeService.createQrCodeAndSendEmail(bookingId, userEmail);

            log.info("Hoàn thành xử lý xác nhận đặt phòng cho booking: {}", message.getBookingId());
            bookingConfirmedEmailResponsePublisher.publish(createSuccessMessage(message));
        } catch (Exception e) {
            log.error("Lỗi khi xử lý xác nhận đặt phòng cho booking: {}", message.getBookingId(), e);
            bookingConfirmedEmailResponsePublisher.publish(createFailedMessage(message));
            throw new RuntimeException("Không thể xử lý xác nhận đặt phòng", e);
        }
    }

    private NotificationMessage createSuccessMessage(NotificationMessage message) {
        return NotificationMessage.builder()
                .id(message.getId())
                .messageStatus(MessageStatus.SUCCESS)
                .customerEmail(message.getCustomerEmail())
                .bookingId(message.getBookingId())
                .notificationType(message.getNotificationType())
                .qrCode(message.getQrCode())
                .customerId(message.getCustomerId())
                .build();
    }

    private NotificationMessage createFailedMessage(NotificationMessage message) {
        return NotificationMessage.builder()
                .id(message.getId())
                .messageStatus(MessageStatus.FAILED)
                .customerEmail(message.getCustomerEmail())
                .bookingId(message.getBookingId())
                .notificationType(message.getNotificationType())
                .qrCode(message.getQrCode())
                .customerId(message.getCustomerId())
                .build();
    }
}
