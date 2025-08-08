package com.poly.booking.management.domain.mapper;

import com.poly.booking.management.domain.event.BookingCreatedEvent;
import com.poly.booking.management.domain.event.CheckOutEvent;
import com.poly.booking.management.domain.outbox.model.payment.BookingPaymentEventPayload;
import com.poly.domain.valueobject.PaymentOrderStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentDataMapper {


    /**
     * Chuyển đổi BookingEvent thành BookingDepositedEventPayload
     * <p>
     * Mục đích: Tạo payload cho Kafka message để thông báo payment service về booking
     * Sử dụng: Gửi message đến payment service để xử lý thanh toán
     *
     * @param bookingCreatedEvent BookingEvent từ domain
     * @return BookingPaymentEventPayload cho Kafka message
     */
    public BookingPaymentEventPayload bookingCreatedEventToRoomBookingEventPayload(BookingCreatedEvent bookingCreatedEvent) {
        return BookingPaymentEventPayload.builder()
                .bookingId(bookingCreatedEvent.getBooking().getId().getValue().toString())
                .customerId(bookingCreatedEvent.getBooking().getCustomerId().getValue().toString())
                .paymentBookingStatus(PaymentOrderStatus.PENDING.toString())
                .price(bookingCreatedEvent.getBooking().getTotalPrice().getAmount())
                .createdAt(bookingCreatedEvent.getCreatedAt().getValue())
                .build();
    }

    public BookingPaymentEventPayload bookingCheckOutEventToBookingPaymentEventPayload(CheckOutEvent bookingCheckOutEvent) {
        return BookingPaymentEventPayload.builder()
                .bookingId(bookingCheckOutEvent.getBooking().getId().getValue().toString())
                .customerId(bookingCheckOutEvent.getBooking().getCustomerId().getValue().toString())
                .price(bookingCheckOutEvent.getBooking().getTotalPrice().getAmount())
                .createdAt(bookingCheckOutEvent.getCreatedAt().getValue())
                .paymentBookingStatus(PaymentOrderStatus.PENDING.toString())
                .build();
    }
}
