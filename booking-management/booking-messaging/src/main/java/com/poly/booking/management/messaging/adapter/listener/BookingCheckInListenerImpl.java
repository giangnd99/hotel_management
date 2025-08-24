package com.poly.booking.management.messaging.adapter.listener;

import com.poly.booking.management.domain.port.in.message.listener.BookingCheckInListener;
import com.poly.booking.management.domain.message.reponse.NotificationMessageResponse;
import com.poly.booking.management.domain.saga.checkin.BookingRoomCheckInStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookingCheckInListenerImpl implements BookingCheckInListener {

    private final BookingRoomCheckInStep bookingRoomCheckInStep;

    @Override
    public void processBookingCheckIn(NotificationMessageResponse notificationMessageResponse) {
        log.info("Processing successful check-in for booking: {}", notificationMessageResponse.getBookingId());

        try {
            validateCheckInRequest(notificationMessageResponse);

            bookingRoomCheckInStep.process(notificationMessageResponse);

            log.info("Booking check-in processed successfully for booking: {}", 
                    notificationMessageResponse.getBookingId());

        } catch (Exception e) {
            log.error("Error processing booking check-in for booking: {}", 
                    notificationMessageResponse.getBookingId(), e);
            throw new RuntimeException("Failed to process booking check-in", e);
        }
    }

    @Override
    public void processBookingCheckInFailed(NotificationMessageResponse notificationMessageResponse) {
        log.info("Processing failed check-in for booking: {}", notificationMessageResponse.getBookingId());

        try {
            validateCheckInRequest(notificationMessageResponse);

            bookingRoomCheckInStep.processFailed(notificationMessageResponse);

            log.info("Failed check-in processed successfully for booking: {}", 
                    notificationMessageResponse.getBookingId());

        } catch (Exception e) {
            log.error("Error processing failed check-in for booking: {}", 
                    notificationMessageResponse.getBookingId(), e);
            throw new RuntimeException("Failed to process failed check-in", e);
        }
    }

    @Override
    public void processBookingCheckInPending(NotificationMessageResponse notificationMessageResponse) {
        log.info("Processing pending check-in for booking: {}", notificationMessageResponse.getBookingId());

        try {
            validateCheckInRequest(notificationMessageResponse);

            bookingRoomCheckInStep.processPending(notificationMessageResponse);

            log.info("Pending check-in processed successfully for booking: {}", 
                    notificationMessageResponse.getBookingId());

        } catch (Exception e) {
            log.error("Error processing pending check-in for booking: {}", 
                    notificationMessageResponse.getBookingId(), e);
            throw new RuntimeException("Failed to process pending check-in", e);
        }
    }

    private void validateCheckInRequest(NotificationMessageResponse notificationMessageResponse) {
        if (notificationMessageResponse == null) {
            throw new IllegalArgumentException("Notification message response cannot be null");
        }

        if (notificationMessageResponse.getBookingId() == null || 
            notificationMessageResponse.getBookingId().trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }

        if (notificationMessageResponse.getNotificationStatus() == null) {
            throw new IllegalArgumentException("Notification status cannot be null");
        }

        log.debug("Check-in request validation passed for booking: {}", 
                notificationMessageResponse.getBookingId());
    }
}
