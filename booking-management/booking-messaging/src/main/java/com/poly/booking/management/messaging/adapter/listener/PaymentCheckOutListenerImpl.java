package com.poly.booking.management.messaging.adapter.listener;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.message.reponse.PaymentMessageResponse;
import com.poly.booking.management.domain.port.in.message.listener.PaymentCheckOutListener;
import com.poly.booking.management.domain.port.out.client.RoomClient;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentCheckOutListenerImpl implements PaymentCheckOutListener {

    private final BookingRepository bookingRepository;
    private final RoomClient roomClient;

    @Override
    public void paymentResponseCheckOutCompleted(PaymentMessageResponse messageResponse) {
        log.info("Processing room check out completed event for booking: {}",
                messageResponse.getBookingId());
        try {
            if(messageResponse.getPaymentStatus().name().equalsIgnoreCase("PAID")) {
                Booking bookingCheckingOut = bookingRepository.findById(UUID.fromString(messageResponse.getBookingId())).orElseThrow(() ->
                        new IllegalArgumentException("Can't check out. Booking not found for ID: " + messageResponse.getBookingId()));
                bookingCheckingOut.checkOut();
                bookingRepository.save(bookingCheckingOut);

                roomClient.performCheckOut(bookingCheckingOut.getId().getValue());
                log.info("Room check out completed for booking: {}", messageResponse.getBookingId());
                log.info("Room check out completed successfully for booking: {}",
                        messageResponse.getBookingId());
            }

        } catch (Exception e) {
            log.error("Error processing room check out completed event for booking: {}",
                    messageResponse.getBookingId(), e);
            throw new RuntimeException("Failed to process room check out completed event", e);
        }
    }

}
