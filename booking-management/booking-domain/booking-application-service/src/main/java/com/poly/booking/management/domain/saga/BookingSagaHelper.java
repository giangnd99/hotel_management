package com.poly.booking.management.domain.saga;

import com.poly.booking.management.domain.dto.message.PaymentMessageResponse;
import com.poly.booking.management.domain.dto.message.RoomMessageResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingPaidEvent;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.mapper.RoomDataMapper;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.valueobject.BookingId;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingSagaHelper {
    private final BookingRepository bookingRepository;
    private final BookingDomainService bookingDomainService;
    private final RoomDataMapper roomDataMapper;

    /**
     * Tìm booking theo ID
     */
    public Booking findBooking(String bookingId) {
        Optional<Booking> booking = bookingRepository.findById(new BookingId(UUID.fromString(bookingId)));
        if (booking.isEmpty()) {
            log.error("Booking with id: {} could not be found!", bookingId);
            throw new BookingDomainException("Booking with id " + bookingId + " could not be found!");
        }
        return booking.get();
    }

    /**
     * Map trạng thái booking sang trạng thái Saga
     */
    public SagaStatus bookingStatusToSagaStatus(BookingStatus status) {
        return switch (status) {
            case CONFIRMED, CHECKED_OUT, CHECKED_IN -> SagaStatus.PROCESSING;
            case PAID -> SagaStatus.FINISHED;
            case CANCELLED -> SagaStatus.COMPENSATED;
            case CANCELLING -> SagaStatus.COMPENSATING;
            default -> SagaStatus.STARTED;
        };
    }

    /**
     * Lưu booking
     */
    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    /**
     * Xử lý khi thanh toán thành công
     */
    public BookingPaidEvent processPaymentCompleted(Booking booking, PaymentMessageResponse paymentResponse) {
        log.info("Processing payment completed for booking id: {}", booking.getId().getValue());
        return bookingDomainService.payBooking(booking);
    }

    /**
     * Xử lý khi thanh toán thất bại
     */
    public void processPaymentCancelled(Booking booking, String failureMessages) {
        log.warn("Cancelling booking id: {} due to payment failure: {}", booking.getId().getValue(), failureMessages);
        bookingDomainService.cancelBooking(booking);
    }

    /**
     * Xử lý khi Room service đã giữ phòng thành công
     */
    public void processRoomLocked(Booking booking, RoomMessageResponse roomMessage) {
        log.info("Room locked for booking id: {}", booking.getId().getValue());
        bookingDomainService.confirmBooking(booking);
    }

    /**
     * Xử lý khi Room service không giữ được phòng
     */
    public void processRoomLockFailed(Booking booking, String failureMessages) {
        log.warn("Room locking failed for booking id: {}, reason: {}", booking.getId().getValue(), failureMessages);
        bookingDomainService.cancelBooking(booking);
    }

    /**
     * Rollback Booking khi Saga gặp sự cố
     */
    public void rollbackBooking(Booking booking, String reason) {
        log.error("Rolling back booking id: {} - Reason: {}", booking.getId().getValue(), reason);
        bookingDomainService.cancelBooking(booking);
    }


}

