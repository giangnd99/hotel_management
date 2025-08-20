package com.poly.notification.management.command.impl;

import com.poly.notification.management.command.BookingCheckedInCommand;
import com.poly.notification.management.dto.QrCodeScanResponse;
import com.poly.notification.management.dto.checkin.CheckInDto;
import com.poly.notification.management.dto.checkin.CheckInRequest;
import com.poly.notification.management.message.MessageStatus;
import com.poly.notification.management.message.NotificationMessage;
import com.poly.notification.management.message.NotificationType;
import com.poly.notification.management.port.out.publisher.BookingConfirmedEmailResponsePublisher;
import com.poly.notification.management.repository.feign.ReceptionClient;
import com.poly.notification.management.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingCheckedInCommandImpl implements BookingCheckedInCommand {

    private final QrCodeService qrCodeService;
    private final BookingConfirmedEmailResponsePublisher bookingConfirmedEmailResponsePublisher;
    private final ReceptionClient receptionClient;
    //Dùng feign client yêu cầu phòng cập nhật trạng thái thành check-in

    @Override
    public void processCheckInByQrCodeWithBookingId(String bookingId, String staffId) {
        try {


            QrCodeScanResponse response = qrCodeService.scanQrCodeAndMarkAsScanned(bookingId);
            log.info("Response from scanning qr code: {}", response);
            bookingConfirmedEmailResponsePublisher.publish(createMessageByQrCode(response));
            CheckInDto responseFromRoom = receptionClient.performCheckIn(UUID.fromString(bookingId), createCheckInRequest(response, staffId)).getBody();
            log.info("Response from reception client: {}", responseFromRoom);
            log.info("Successfully process check in by qr code with booking id: {}", bookingId);
            
        } catch (Exception e) {
            log.error("failed to process check in by qr code with booking id: {} ", bookingId, e);
            throw new RuntimeException(e.getMessage(), e);
        }
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

    private CheckInRequest createCheckInRequest(QrCodeScanResponse response, String staffId) {

        return CheckInRequest.builder()
                .checkedInBy(staffId)
                .checkInTime(LocalDateTime.now())
                .numberOfGuests(1)
                .roomNumber(response.getRoomNumber())
                .notes("Check-in by QR code")
                .specialRequests("No special requests")
                .guestId(UUID.fromString(response.getCustomerId()))
                .build();
    }
}
