package com.poly.booking.management.domain.service.impl;

import com.poly.booking.management.domain.dto.PaymentLinkResponse;
import com.poly.booking.management.domain.dto.request.CreateDepositRequest;
import com.poly.booking.management.domain.dto.request.ItemRequest;
import com.poly.booking.management.domain.dto.response.DepositBookingResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.port.out.client.PaymentClient;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.service.DepositBookingCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepositBookingCommandImpl implements DepositBookingCommand {

    private final BookingRepository bookingRepository;

    private final PaymentClient paymentClient;

    @Override
    public DepositBookingResponse depositBooking(UUID command) {
        try {
            log.info("Sending deposit booking request to message bus!");
            Booking booking = getBooking(command);

            CreateDepositRequest createDepositRequest = createDepositRequest(booking);
            PaymentLinkResponse responseClient = paymentClient.pay(createDepositRequest).getBody();
            assert responseClient != null;
            log.info("{} is the payment link!", responseClient.getPaymentLink());


            return DepositBookingResponse.builder()
                    .booking(booking)
                    .urlPayment(responseClient.getPaymentLink())
                    .build();
        } catch (Exception e) {
            log.error("Error when deposit booking: {}", e.getMessage());
            throw new BookingDomainException("Error when deposit booking: " + e.getMessage());
        }
    }


    private CreateDepositRequest createDepositRequest(Booking booking) {
        BigDecimal depositAmount = booking.calculateDepositAmount().getAmount();
        depositAmount = depositAmount.setScale(0, RoundingMode.HALF_UP);
        return CreateDepositRequest.builder()
                .description("Deposit for booking id: " + booking.getId().getValue())
                .referenceId(booking.getId().getValue())
                .amount(depositAmount)
                .items(List.of(ItemRequest.builder().name("Deposit")
                        .quantity(1)
                        .unitPrice(depositAmount)
                        .build()))
                .build();
    }


    private Booking getBooking(UUID bookingId) {
        log.info("Getting booking with id: {}", bookingId);
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking with id: " + bookingId + " could not be found!"));
    }
}
