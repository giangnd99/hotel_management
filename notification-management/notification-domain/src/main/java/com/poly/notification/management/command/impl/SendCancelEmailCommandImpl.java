package com.poly.notification.management.command.impl;

import com.poly.notification.management.command.SendBookingCancelCommand;
import com.poly.notification.management.command.SendBookingConfirmCommand;
import com.poly.notification.management.message.MessageStatus;
import com.poly.notification.management.message.NotificationMessage;
import com.poly.notification.management.port.out.publisher.BookingCancelEmailResponsePublisher;
import com.poly.notification.management.port.out.publisher.BookingConfirmedEmailResponsePublisher;
import com.poly.notification.management.service.BookingQrCodeService;
import com.poly.notification.management.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendCancelEmailCommandImpl implements SendBookingCancelCommand {

    private final NotificationService notificationService;
    private final BookingCancelEmailResponsePublisher bookingCancelEmailResponsePublisher;

    @Override
    public void sendEmailCancel(NotificationMessage message) {
        try {
            log.info("Bắt đầu xử lý xác nhận huỷ phòng cho booking id: {}", message.getBookingId());

            String bookingId = message.getBookingId();
            String userEmail = message.getCustomerEmail();
            bookingCancelEmailResponsePublisher.publish(createSuccessMessage(message));
            notificationService.sendBookingCancellation(message.getCustomerId(),userEmail,bookingId);

            log.info("Hoàn thành xử lý xác nhận huỷ phòng cho booking: {}", message.getBookingId());

        } catch (Exception e) {
            log.error("Lỗi khi xử lý xác nhận đặt phòng cho booking: {}", message.getBookingId(), e);
            throw new RuntimeException("Không thể xử lý xác nhận đặt phòng", e);
        }
    }

    NotificationMessage createSuccessMessage(NotificationMessage message) {
        return NotificationMessage.builder()
                .id(message.getId())
                .messageStatus(MessageStatus.SUCCESS)
                .customerEmail(message.getCustomerEmail())
                .bookingId(message.getBookingId())
                .notificationType(message.getNotificationType())
                .customerId(message.getCustomerId())
                .build();
    }
}
