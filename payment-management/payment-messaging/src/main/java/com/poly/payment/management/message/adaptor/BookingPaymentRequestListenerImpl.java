package com.poly.payment.management.message.adaptor;

import com.poly.payment.management.domain.dto.CheckoutResponseData;
import com.poly.payment.management.domain.dto.ItemData;
import com.poly.payment.management.domain.dto.request.CreatePaymentLinkCommand;
import com.poly.payment.management.domain.message.*;
import com.poly.payment.management.domain.port.input.BookingPaymentRequestListener;
import com.poly.payment.management.domain.service.PaymentGateway;
import com.poly.payment.management.message.publisher.BookingPaymentPendingKafkaPublisher;
import com.poly.payment.management.message.publisher.BookingPaymentReplyKafkaPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingPaymentRequestListenerImpl implements BookingPaymentRequestListener {

    private final PaymentGateway paymentGateway;
    private final BookingPaymentPendingKafkaPublisher bookingPaymentPendingKafkaPublisher;

    @Override
    public void handleBookingPaymentRequest(BookingPaymentRequest bookingPaymentRequest) {

        log.info("Handling booking payment request");
        log.info("Booking payment request: {}", bookingPaymentRequest);

        List<ItemData> itemDataList = List.of(ItemData.builder()
                .name("Payment for booking id: " + bookingPaymentRequest.getBookingId())
                .price(bookingPaymentRequest.getPrice())
                .quantity(1)
                .build());

        CreatePaymentLinkCommand command =
                CreatePaymentLinkCommand.builder()
                        .orderCode(Long.decode(bookingPaymentRequest.getBookingId()))
                        .amount(bookingPaymentRequest.getPrice())
                        .description(bookingPaymentRequest.getPaymentBookingStatus().name() + " for booking " + bookingPaymentRequest.getBookingId())
                        .method("Booking Payment")
                        .items(itemDataList)
                        .build();
        try {
            CheckoutResponseData responseData = paymentGateway.createPaymentLink(command);
            String paymentUrl = responseData.getCheckoutUrl();
            log.info("Payment url: {}", paymentUrl);

            BookingPaymentPendingResponse bookingPaymentPending = toBookingPaymentPendingResponse(bookingPaymentRequest,responseData);
            log.info("Booking payment response: {}", bookingPaymentPending);
            bookingPaymentPendingKafkaPublisher.publish(bookingPaymentPending);
            log.info("Booking payment response published");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BookingPaymentPendingResponse toBookingPaymentPendingResponse(BookingPaymentRequest message,CheckoutResponseData responseData) {
        return BookingPaymentPendingResponse.builder()
                .bookingId(message.getBookingId())
                .customerId(message.getCustomerId())
                .paymentBookingStatus(PaymentBookingStatus.PENDING)
                .createdAt(message.getCreatedAt())
                .id(message.getId())
                .sagaId(message.getSagaId())
                .price(message.getPrice())
                .createdAt(message.getCreatedAt())
                .urlPayment(responseData.getCheckoutUrl())
                .build();
    }
}
