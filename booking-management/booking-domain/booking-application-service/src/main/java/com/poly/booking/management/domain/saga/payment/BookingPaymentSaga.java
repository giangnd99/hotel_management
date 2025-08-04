package com.poly.booking.management.domain.saga.payment;

import com.poly.booking.management.domain.dto.message.payment.PaymentMessageResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingPaidEvent;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.mapper.BookingDataMapper;
import com.poly.booking.management.domain.outbox.model.room.BookingRoomOutboxMessage;
import com.poly.booking.management.domain.outbox.model.payment.BookingPaymentOutboxMessage;
import com.poly.booking.management.domain.outbox.scheduler.room.RoomOutboxHelper;
import com.poly.booking.management.domain.outbox.scheduler.notification.NotificationOutboxHelper;
import com.poly.booking.management.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.valueobject.EBookingStatus;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.saga.SagaStatus;
import com.poly.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingPaymentSaga implements SagaStep<PaymentMessageResponse> {

    private final BookingDomainService bookingDomainService;
    private final BookingRepository bookingRepository;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final RoomOutboxHelper roomOutboxHelper;
    private final NotificationOutboxHelper notificationOutboxHelper;
    private final BookingSagaHelper bookingSagaHelper;
    private final BookingDataMapper bookingDataMapper;


    @Override
    @Transactional
    public void process(PaymentMessageResponse data) {
        Optional<BookingPaymentOutboxMessage> bookingPaymentOutboxMessageResponse =
                paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        SagaStatus.STARTED);

        if (bookingPaymentOutboxMessageResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already processed!", data.getSagaId());
            return;
        }
        BookingPaymentOutboxMessage bookingPaymentOutboxMessage = bookingPaymentOutboxMessageResponse.get();

        BookingPaidEvent bookingPaidEvent = bookingSagaHelper.completePaymentForBooking(data);

        SagaStatus sagaStatus = bookingSagaHelper.bookingStatusToSagaStatus(
                bookingPaidEvent.getBooking().getStatus());

        BookingPaymentOutboxMessage paymentUpdatedMessage =
                paymentOutboxHelper.getUpdatePaymentOutboxMessage(
                        bookingPaymentOutboxMessage,
                        bookingPaidEvent.getBooking().getStatus(),
                        sagaStatus);
        paymentOutboxHelper.save(paymentUpdatedMessage);

        roomOutboxHelper.save(reserveRoomMessage);
        log.info("Booking with id: {} has been paid successfully!", bookingPaidEvent.getBooking().getId().getValue());
    }


    @Override
    @Transactional
    public void rollback(PaymentMessageResponse paymentResponse) {

        //Tìm các khoảng thanh toán có saga id tương ứng và có status là STARTED hoặc PROCESSING vì
        //có thể có nhiều khoảng thanh toán cùng lúc
        //Nếu không tìm thấy thì trả về vì đã rollback
        //Nếu tìm thấy thì lấy ra và rollback
        //Lưu lại trạng thái của khoảng thanh toán
        Optional<BookingPaymentOutboxMessage> bookingPaymentOutboxMessageResponse =
                paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(paymentResponse.getSagaId()),
                        getCurrentSagaStatus(paymentResponse.getPaymentStatus()));

        if (bookingPaymentOutboxMessageResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already roll backed!", paymentResponse.getSagaId());
            return;
        }
        //Lấy ra khoảng thanh toán
        BookingPaymentOutboxMessage bookingPaymentOutboxMessage = bookingPaymentOutboxMessageResponse.get();
        //Rollback khoảng thanh toán
        //Lấy ra booking
        Booking booking = rollbackPaymentForBooking(paymentResponse);
        //Lấy ra trạng thái saga từ booking
        //Lưu lại trạng thái của khoảng thanh toán
        SagaStatus sagaStatus = bookingSagaHelper.bookingStatusToSagaStatus(booking.getStatus());

        //Lưu lại trạng thái của khoảng thanh toán
        //Trạng thái saga là COMPENSATING
        //Trạng thái payment là CANCELLED
        //Trạng thái booking là CANCELLED
        //Trạng thái outbox là COMPLETED
        paymentOutboxHelper.save(paymentOutboxHelper.getUpdatePaymentOutboxMessage(
                bookingPaymentOutboxMessage,
                booking.getStatus(),
                sagaStatus));
        if (paymentResponse.getPaymentStatus() == PaymentStatus.CANCELLED) {
            roomOutboxHelper.save(getUpdatedApprovalOutboxMessage(paymentResponse.getSagaId(), booking.getStatus(), sagaStatus));
        }
        log.info("Booking with id: {} has been roll backed successfully!", booking.getId().getValue());
    }

    //Rollback khoảng thanh toán
    //Lấy ra booking
    //Cancel booking
    //Lưu lại booking
    //Trả về booking
    private Booking rollbackPaymentForBooking(PaymentMessageResponse paymentResponse) {
        //Lấy ra booking
        log.info("Rolling back payment for booking with id: {}", paymentResponse.getBookingId());
        Booking booking = bookingSagaHelper.findBooking(paymentResponse.getBookingId());
        //Cancel booking
        bookingDomainService.cancelBooking(booking);
        //Lưu lại booking
        bookingRepository.save(booking);
        //Trả về booking đã cancel
        //Trạng thái saga là COMPENSATING
        return booking;
    }

    private SagaStatus[] getCurrentSagaStatus(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            case COMPLETED -> new SagaStatus[]{SagaStatus.STARTED};
            case CANCELLED -> new SagaStatus[]{SagaStatus.PROCESSING};
            case FAILED -> new SagaStatus[]{SagaStatus.STARTED, SagaStatus.PROCESSING};
        };
    }

    private BookingRoomOutboxMessage getUpdatedApprovalOutboxMessage(String sagaId,
                                                                     EBookingStatus bookingStatus,
                                                                     SagaStatus sagaStatus) {
        Optional<BookingRoomOutboxMessage> bookingApprovalOutboxMessageResponse =
                roomOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(sagaId),
                        SagaStatus.COMPENSATING);
        if (bookingApprovalOutboxMessageResponse.isEmpty()) {
            throw new BookingDomainException("Approval outbox message with saga id: " + sagaId + " is not found in" + SagaStatus.COMPENSATING.name() + " status !");
        }
        BookingRoomOutboxMessage bookingRoomOutboxMessage = bookingApprovalOutboxMessageResponse.get();
        bookingRoomOutboxMessage.setBookingStatus(bookingStatus);
        bookingRoomOutboxMessage.setSagaStatus(sagaStatus);
        bookingRoomOutboxMessage.setProcessedAt(LocalDateTime.now());
        return bookingRoomOutboxMessage;
    }
}

