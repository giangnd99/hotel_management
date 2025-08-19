package com.poly.booking.management.domain.service.impl;

import com.poly.booking.management.domain.dto.PaymentLinkResponse;
import com.poly.booking.management.domain.dto.request.CreateDepositRequest;
import com.poly.booking.management.domain.dto.request.DepositBookingRequest;
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

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepositBookingCommandImpl implements DepositBookingCommand {

    private final BookingRepository bookingRepository;

    private final PaymentClient paymentClient;

    @Override
    public DepositBookingResponse depositBooking(DepositBookingRequest command) {
        try {
            log.info("Sending deposit booking request to message bus!");
            Booking booking = getBooking(command.getBookingId());

            CreateDepositRequest createDepositRequest = createDepositRequest(booking);
            PaymentLinkResponse responseClient = paymentClient.pay(createDepositRequest).getBody();
            assert responseClient != null;
            log.info("{} is the payment link!", responseClient.getPaymentLink());


            return DepositBookingResponse.builder()
                    .bookingId(command.getBookingId())
                    .customerId(command.getCustomerId())
                    .customerName(booking.getCustomer().getName())
                    .urlPayment(responseClient.getPaymentLink())
                    .build();
        }catch (Exception e) {
            log.error("Error when deposit booking: {}", e.getMessage());
            throw new BookingDomainException("Error when deposit booking: " + e.getMessage());
        }
    }


    private CreateDepositRequest createDepositRequest(Booking booking) {
        return CreateDepositRequest.builder()
                .description("Deposit for booking id: " + booking.getId().getValue())
                .referenceId(booking.getId().getValue())
                .amount(booking.calculateDepositAmount().getAmount())
                .items(List.of(ItemRequest.builder().name("Deposit")
                        .quantity(1)
                        .unitPrice(booking.calculateDepositAmount().getAmount())
                        .build()))
                .build();
    }


    private Booking getBooking(String bookingId) {
        return bookingRepository.findById(
                        new com.poly.domain.valueobject.BookingId(UUID.fromString(bookingId)))
                .orElseThrow(() -> new IllegalArgumentException("Booking with id: " + bookingId + " could not be found!"));
    }
}
