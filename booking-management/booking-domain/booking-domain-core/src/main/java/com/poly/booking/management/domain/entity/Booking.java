package com.poly.booking.management.domain.entity;

import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.valueobject.BookingRoomId;
import com.poly.booking.management.domain.valueobject.TrackingId;
import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Entity Booking đại diện cho một lượt đặt phòng khách sạn.
 * Quản lý trạng thái, thông tin khách hàng, phòng, dịch vụ, QR check-in, v.v.
 */
@Slf4j
public class Booking extends AggregateRoot<BookingId> {

    private Customer customer;
    private DateCustom checkInDate;
    private DateCustom checkOutDate;
    private BookingStatus status;
    private TrackingId trackingId;
    private DateCustom actualCheckInDate;
    private DateCustom actualCheckOutDate;
    private Money totalPrice;
    private String upgradeSuggestion;
    private List<BookingRoom> bookingRooms;
    private List<String> failureMessages;
    public static final String FAILURE_MESSAGE_DELIMITER = ",";

    public Booking(Builder builder) {
        super.setId(builder.id);
        customer = builder.customer;
        checkInDate = builder.checkInDate;
        checkOutDate = builder.checkOutDate;
        status = builder.status;
        trackingId = builder.trackingId;
        actualCheckInDate = builder.actualCheckInDate;
        actualCheckOutDate = builder.actualCheckOutDate;
        totalPrice = builder.totalPrice;
        upgradeSuggestion = builder.upgradeSuggestion;
        bookingRooms = builder.bookingRooms;
        failureMessages = builder.failureMessages;
    }


    public Money calculateDepositAmount() {
        Money depositAmount = totalPrice.multiply(new BigDecimal("0.3"));
        log.info("Deposit amount: {}", depositAmount);
        return depositAmount;
    }

    /**
     * Khởi tạo booking ở trạng thái PENDING.
     */
    public void initiateBooking() {
        setId(new BookingId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        validateDateRange();
        status = BookingStatus.PENDING;
        initializeBookingRooms();
    }

    public void initializeBookingRooms() {
        for (BookingRoom bookingRoom : bookingRooms) {
            bookingRoom.initialize(this, new BookingRoomId(UUID.randomUUID()));
        }
    }

    /**
     * Đặt cọc booking, chuyển sang trạng thái DEPOSITED.
     */
    public void depositBooking() {
        validateStatusForDeposit();
        status = BookingStatus.DEPOSITED;
    }

    /**
     * Xác nhận đặt cọc, chuyển sang trạng thái CONFIRMED.
     */
    public void confirmBooking() {
        validateStatusForConfirmDeposit();
        status = BookingStatus.CONFIRMED;
    }

    /**
     * Check-in thành công, chuyển sang trạng thái CHECKED_IN.
     */
    public void checkIn() {
        validateStatusForCheckIn();
        status = BookingStatus.CHECKED_IN;
    }

    /**
     * Thanh toán thành công, chuyển sang trạng thái PAID.
     */
    public void paidBooking() {
        validateStatusForPaid();
        status = BookingStatus.PAID;
    }

    /**
     * Hủy booking khi thanh toán thất bại hoặc khi cần rollback.
     */
    public void cancelBooking() {
        validateStatusForCancel();
        status = BookingStatus.CANCELLED;
    }

    /**
     * Hủy booking đã xác nhận.
     */
    public void cancelConfirmedBooking() {
        validateStatusForCancelConfirmed();
        status = BookingStatus.CANCELLED;
    }

    /**
     * Check-out thành công, chuyển sang trạng thái CHECKED_OUT.
     */
    public void checkOut() {
        validateStatusForCheckOut();
        status = BookingStatus.CHECKED_OUT;
    }

    // ================= PRIVATE VALIDATION METHODS =================

    private String formatData() {
        String bookingId = getId().getValue().toString();
        String customerName = customer.toString();
        String checkInDateStr = getCheckInDate().toString();
        String checkOutDateStr = getCheckOutDate().toISOString();
        String roomsListString = bookingRooms.stream()
                .map(bookingRoom ->
                        bookingRoom.getRoom().getRoomNumber()
                ).collect(Collectors.joining(", "));
        String totalPriceStr = getTotalPrice().toString();

        return String.format(
                "Mã booking: %s|Tên khách hàng: %s|Ngày check-in: %s|Ngày check-out: %s|Phòng đã đặt: %s|Tổng tiền: %s",
                bookingId, customerName, checkInDateStr, checkOutDateStr, roomsListString, totalPriceStr);
    }

    private void validateDateRange() {
        if (checkInDate.isAfter(checkOutDate)) {
            throw new BookingDomainException("Check-in date must be before check-out date");
        }
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    private void validateTotalPrice() {
        if (totalPrice == null || !totalPrice.isGreaterThanZero()) {
            throw new BookingDomainException("Total price must be greater than zero");
        }
    }

    private void validateStatusForDeposit() {
        if (!BookingStatus.PENDING.equals(status)) {
            throw new BookingDomainException("Booking is not pending for deposit");
        }
    }

    private void validateStatusForConfirmDeposit() {
        if (!BookingStatus.PENDING.equals(status)) {
            throw new BookingDomainException("Booking is not pending for confirmation");
        }
    }

    private void validateStatusForCheckIn() {
        if (!BookingStatus.CONFIRMED.equals(status)) {
            throw new BookingDomainException("Booking is not confirmed for check-in");
        }
    }

    private void validateStatusForPaid() {
        if (!BookingStatus.CHECKED_IN.equals(status)) {
            throw new BookingDomainException("Booking is not checked-in for payment");
        }
    }

    private void validateStatusForCancel() {
        if (!(BookingStatus.PENDING.equals(status) || BookingStatus.CHECKED_OUT.equals(status))) {
            throw new BookingDomainException("Booking is not in a cancellable state");
        }
    }

    private void validateStatusForCancelConfirmed() {
        if (!BookingStatus.CONFIRMED.equals(status)) {
            throw new BookingDomainException("Booking is not confirmed for cancellation");
        }
        if (isRegularBooking()) {
            throw new BookingDomainException("Booking is not regular for cancellation");
        }
    }

    private void validateStatusForCheckOut() {
        if (!BookingStatus.CHECKED_IN.equals(status)) {
            throw new BookingDomainException("Booking is not checked-in for check-out");
        }
    }

    private boolean isRegularBooking() {
        return checkInDate.getDay() - DateCustom.now().getDay() <= 3;
    }

    // ================= GETTER/SETTER =================
    public BookingStatus getStatus() {
        return status;
    }

    public void setTotalPrice(Money totalPrice) {
        this.totalPrice = totalPrice;
        validateTotalPrice();
    }

    public void updateAndValidateTotalPrice() {
        Money currentTotalRoomPrice = bookingRooms != null ? bookingRooms.stream()
                .map(bookingRoom -> bookingRoom.getRoom().getBasePrice())
                .reduce(Money.ZERO, Money::add) : Money.ZERO;
        this.totalPrice = currentTotalRoomPrice;
        validateTotalPrice();
    }

    public void setBookingRooms(List<BookingRoom> bookingRooms) {
        this.bookingRooms = bookingRooms;
        updateAndValidateTotalPrice();
    }

    public Money getTotalPrice() {
        return totalPrice;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<BookingRoom> getBookingRooms() {
        return bookingRooms;
    }

    public DateCustom getCheckInDate() {
        return checkInDate;
    }

    public DateCustom getCheckOutDate() {
        return checkOutDate;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public DateCustom getActualCheckInDate() {
        return actualCheckInDate;
    }

    public void setActualCheckInDate(DateCustom actualCheckInDate) {
        this.actualCheckInDate = actualCheckInDate;
    }

    public DateCustom getActualCheckOutDate() {
        return actualCheckOutDate;
    }

    public void setActualCheckOutDate(DateCustom actualCheckOutDate) {
        this.actualCheckOutDate = actualCheckOutDate;
    }

    // ================= BUILDER =================
    public static final class Builder {
        private BookingId id;
        private Customer customer;
        private DateCustom checkInDate;
        private DateCustom checkOutDate;
        private BookingStatus status;
        private TrackingId trackingId;
        private DateCustom actualCheckInDate;
        private DateCustom actualCheckOutDate;
        private RestaurantId restaurantId;
        private ServiceId serviceId;
        private Money totalPrice;
        private String upgradeSuggestion;
        private List<BookingRoom> bookingRooms;
        private List<String> failureMessages;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(BookingId val) {
            id = val;
            return this;
        }

        public Builder customer(Customer val) {
            customer = val;
            return this;
        }

        public Builder checkInDate(DateCustom val) {
            checkInDate = val;
            return this;
        }

        public Builder checkOutDate(DateCustom val) {
            checkOutDate = val;
            return this;
        }

        public Builder status(BookingStatus val) {
            status = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder actualCheckInDate(DateCustom val) {
            actualCheckInDate = val;
            return this;
        }

        public Builder actualCheckOutDate(DateCustom val) {
            actualCheckOutDate = val;
            return this;
        }

        public Builder totalPrice(Money val) {
            totalPrice = val;
            return this;
        }

        public Builder upgradeSuggestion(String val) {
            upgradeSuggestion = val;
            return this;
        }

        public Builder bookingRooms(List<BookingRoom> val) {
            bookingRooms = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }
}
