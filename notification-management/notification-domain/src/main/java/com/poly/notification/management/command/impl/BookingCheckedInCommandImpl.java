package com.poly.notification.management.command.impl;

import com.poly.notification.management.command.BookingCheckedInCommand;
import com.poly.notification.management.dto.QrCodeScanResponse;
import com.poly.notification.management.message.MessageStatus;
import com.poly.notification.management.message.NotificationMessage;
import com.poly.notification.management.message.NotificationType;
import com.poly.notification.management.port.out.publisher.BookingConfirmedEmailResponsePublisher;
import com.poly.notification.management.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingCheckedInCommandImpl implements BookingCheckedInCommand {

    private final QrCodeService qrCodeService;
    private final BookingConfirmedEmailResponsePublisher bookingConfirmedEmailResponsePublisher;

    @Override
    public void processCheckInByQrCodeWithBookingId(String bookingId) {
        QrCodeScanResponse response = qrCodeService.scanQrCodeAndMarkAsScanned(bookingId);
        log.info("Response from scanning qr code: {}", response);
        bookingConfirmedEmailResponsePublisher.publish(createMessageByQrCode(response));
        log.info("Successfully process check in by qr code with booking id: {}", bookingId);
    }

    private NotificationMessage createMessageByQrCode(QrCodeScanResponse response) {
        return NotificationMessage.builder()
                .id(UUID.randomUUID().toString())
                .messageStatus(MessageStatus.SUCCESS)
                .customerEmail(response.getCustomerEmail())
                .bookingId(response.getBookingId())
                .notificationType(NotificationType.CHECK_IN)
                .qrCode(response.getQrCodeId())
                .customerId(response.getCustomerId())
                .build();
    }
}
