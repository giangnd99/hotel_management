package com.poly.booking.management.domain.service;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingCancelledEvent;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.domain.valueobject.DateCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Booking Cancellation Domain Service - Xử lý business logic hủy booking
 * <p>
 * CHỨC NĂNG:
 * - Validate điều kiện hủy booking
 * - Xác định có hoàn tiền hay không dựa trên thời gian
 * - Thực hiện hủy booking và tạo domain event
 * <p>
 * MỤC ĐÍCH:
 * - Đảm bảo business rules được tuân thủ khi hủy booking
 * - Xử lý logic hoàn tiền theo chính sách của khách sạn
 * - Cập nhật trạng thái booking một cách nhất quán
 */
@Slf4j
@Service
public class BookingCancellationDomainService {

    private static final int CANCELLATION_DEADLINE_DAYS = 1; // 1 ngày trước check-in

    /**
     * Hủy booking với validation đầy đủ
     * <p>
     * BUSINESS RULES:
     * - Chỉ cho phép hủy booking có trạng thái hợp lệ
     * - Nếu hủy trong vòng 1 ngày trước check-in → không hoàn tiền
     * - Nếu hủy sớm hơn → hoàn tiền đầy đủ
     * <p>
     * VALIDATION:
     * - Kiểm tra trạng thái booking có thể hủy
     * - Kiểm tra thời gian hủy so với check-in date
     * - Xác định có hoàn tiền hay không
     *
     * @param booking Booking cần hủy
     * @param cancellationReason Lý do hủy
     * @return BookingCancelledEvent chứa thông tin hủy
     */
    public BookingCancelledEvent cancelBooking(Booking booking, String cancellationReason) {
        log.info("Processing cancellation for booking: {} with reason: {}", 
                booking.getId().getValue(), cancellationReason);

        // Validate booking có thể hủy
        validateBookingCanBeCancelled(booking);

        // Xác định có hoàn tiền hay không
        boolean isRefundable = determineRefundability(booking);

        // Cập nhật trạng thái booking
        booking.cancelBooking();

        // Tạo domain event
        BookingCancelledEvent cancelledEvent = new BookingCancelledEvent(booking, cancellationReason, isRefundable);

        log.info("Booking cancelled successfully: {}. Refundable: {}. Reason: {}", 
                booking.getId().getValue(), isRefundable, cancellationReason);

        return cancelledEvent;
    }

    /**
     * Validate booking có thể hủy hay không
     * <p>
     * CHECKS:
     * - Trạng thái booking phải hợp lệ để hủy
     * - Không thể hủy booking đã hoàn tất hoặc đã hủy
     *
     * @param booking Booking cần validate
     * @throws BookingDomainException nếu không thể hủy
     */
    private void validateBookingCanBeCancelled(Booking booking) {
        if (booking == null) {
            throw new BookingDomainException("Booking cannot be null for cancellation");
        }

        BookingStatus currentStatus = booking.getStatus();
        
        if (currentStatus == BookingStatus.CANCELLED) {
            throw new BookingDomainException("Booking is already cancelled: " + booking.getId().getValue());
        }

        if (currentStatus == BookingStatus.CHECKED_OUT) {
            throw new BookingDomainException("Cannot cancel completed booking: " + booking.getId().getValue());
        }

        if (currentStatus == BookingStatus.PAID && isAfterCheckIn(booking)) {
            throw new BookingDomainException("Cannot cancel booking after check-in: " + booking.getId().getValue());
        }

        log.debug("Booking validation passed for cancellation: {}", booking.getId().getValue());
    }

    /**
     * Xác định có hoàn tiền hay không dựa trên thời gian hủy
     * <p>
     * REFUND POLICY:
     * - Hủy trong vòng 1 ngày trước check-in → KHÔNG hoàn tiền
     * - Hủy sớm hơn → HOÀN TIỀN đầy đủ
     *
     * @param booking Booking cần kiểm tra
     * @return true nếu có thể hoàn tiền, false nếu không
     */
    private boolean determineRefundability(Booking booking) {
        DateCustom checkInDate = booking.getCheckInDate();
        if (checkInDate == null) {
            log.warn("Check-in date is null for booking: {}. Assuming refundable.", booking.getId().getValue());
            return true;
        }

        LocalDate today = LocalDate.now();
        LocalDate checkInLocalDate = checkInDate.getValue().toLocalDate();
        
        long daysUntilCheckIn = ChronoUnit.DAYS.between(today, checkInLocalDate);

        boolean isRefundable = daysUntilCheckIn > CANCELLATION_DEADLINE_DAYS;

        log.info("Cancellation refundability for booking {}: {} days until check-in, refundable: {}", 
                booking.getId().getValue(), daysUntilCheckIn, isRefundable);

        return isRefundable;
    }

    /**
     * Kiểm tra xem thời gian hiện tại có sau check-in hay không
     *
     * @param booking Booking cần kiểm tra
     * @return true nếu đã qua check-in date
     */
    private boolean isAfterCheckIn(Booking booking) {
        DateCustom checkInDate = booking.getCheckInDate();
        if (checkInDate == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        LocalDate checkInLocalDate = checkInDate.getValue().toLocalDate();
        
        return today.isAfter(checkInLocalDate);
    }

    /**
     * Lấy số ngày còn lại trước check-in
     *
     * @param booking Booking cần kiểm tra
     * @return Số ngày còn lại trước check-in
     */
    public long getDaysUntilCheckIn(Booking booking) {
        DateCustom checkInDate = booking.getCheckInDate();
        if (checkInDate == null) {
            return -1; // Không có check-in date
        }

        LocalDate today = LocalDate.now();
        LocalDate checkInLocalDate = checkInDate.getValue().toLocalDate();
        
        return ChronoUnit.DAYS.between(today, checkInLocalDate);
    }

    /**
     * Kiểm tra xem có thể hủy booking hay không
     *
     * @param booking Booking cần kiểm tra
     * @return true nếu có thể hủy
     */
    public boolean canCancelBooking(Booking booking) {
        try {
            validateBookingCanBeCancelled(booking);
            return true;
        } catch (BookingDomainException e) {
            log.debug("Booking cannot be cancelled: {}", e.getMessage());
            return false;
        }
    }
}
