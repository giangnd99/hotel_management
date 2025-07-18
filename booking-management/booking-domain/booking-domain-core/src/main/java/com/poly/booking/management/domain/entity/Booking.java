package com.poly.booking.management.domain.entity;

import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.valueobject.TrackingId;
import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.*;

import java.util.List;
import java.util.UUID;

public class Booking extends AggregateRoot<BookingId> {

    private CustomerId customerId;
    private DateCustom checkInDate;
    private DateCustom checkOutDate;
    private EBookingStatus status;
    private Deposit deposit;
    private TrackingId trackingId;
    private DateCustom actualCheckInDate;
    private DateCustom actualCheckOutDate;
    private RestaurantId restaurantId;
    private ServiceId serviceId;
    private Money totalPrice;
    private String upgradeSuggestion;
    private QRCodeCheckIn qrCodeCheckIn;
    public List<BookingRoom> bookingRooms;

    private void validateDateRange() {
        if (checkInDate.isAfter(checkOutDate)) {
            throw new BookingDomainException("Check-in date must be before check-out date");
        }
    }

    public void validateRoomsPrice() {
        Money finalPrice = bookingRooms.stream().map(
                bookingRoom ->
                        bookingRoom.getRoom().getBasePrice()).reduce(Money.ZERO, Money::add);
        if (!finalPrice.equals(totalPrice)) {
            throw new BookingDomainException("Total price of rooms is not equal to total price of booking");
        }
    }

    //Khi khoi tao booking se o trang thai pending
    public void initiateBooking() {
        setId(new BookingId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        validateDateRange();
        status = EBookingStatus.PENDING;
    }

    //Khi thanh toan tien coc thi se chuyen sang trang thai confirm
    public void confirmDepositBooking() {
        if (!EBookingStatus.PENDING.equals(status)) {
            throw new BookingDomainException("Booking is not pending for confirmation");
        }
        if (deposit == null) {
            throw new BookingDomainException("Deposit is not set for confirmation");
        }
        deposit.payDeposit();
        status = EBookingStatus.CONFIRMED;
    }

    //Sau khi da xac nhan ma QR thanh cong chuyen sang trang thai check-in
    public void checkIn() {
        if (!EBookingStatus.CONFIRMED.equals(status)) {
            throw new BookingDomainException("Booking is not confirmed for check-in");
        }
        status = EBookingStatus.CHECKED_IN;
    }
///
    public void cancelWhilePaidFailed() {
        if (!EBookingStatus.CHECKED_OUT.equals(status)) {
            throw new BookingDomainException("Booking is not checked-out for cancellation");
        } else if (!EBookingStatus.PENDING.equals(status)) {
            throw new BookingDomainException("Booking is not pending for cancellation");
        }
        status = EBookingStatus.CANCELLED;
    }

    ///
    public void cancelConfirmedBooking() {
        if (!EBookingStatus.CONFIRMED.equals(status)) {
            throw new BookingDomainException("Booking is not confirmed for cancellation");
        } else if (checkRegularity()) {
            throw new BookingDomainException("Booking is not regular for cancellation");
        }
        status = EBookingStatus.CANCELLED;
    }

    private boolean checkRegularity() {
        return checkInDate.getDay() - DateCustom.now().getDay() <= 3;
    }

    //Khach hang thanh toan sau khi check-in tai quay se chuyen duoc sang trang thai check-out
    public void checkOut() {
        if (!EBookingStatus.CHECKED_OUT.equals(status)) {
            throw new BookingDomainException("Booking is not available for payment");
        }
        status = EBookingStatus.CHECKED_OUT;
    }

    public void setActualCheckInDate(DateCustom actualCheckInDate) {
        this.actualCheckInDate = actualCheckInDate;
    }

    public void setActualCheckOutDate(DateCustom actualCheckOutDate) {
        this.actualCheckOutDate = actualCheckOutDate;
    }

    private Booking(Builder builder) {
        super.setId(builder.id);
        customerId = builder.customerId;
        checkInDate = builder.checkInDate;
        checkOutDate = builder.checkOutDate;
        status = builder.status;
        actualCheckInDate = builder.actualCheckInDate;
        actualCheckOutDate = builder.actualCheckOutDate;
        totalPrice = builder.totalPrice;
        upgradeSuggestion = builder.upgradeSuggestion;
    }

    public static final class Builder {
        private BookingId id;
        private CustomerId customerId;
        private DateCustom checkInDate;
        private DateCustom checkOutDate;
        private EBookingStatus status;
        private DateCustom actualCheckInDate;
        private DateCustom actualCheckOutDate;
        private Money totalPrice;
        private String upgradeSuggestion;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(BookingId val) {
            id = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
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

        public Builder status(EBookingStatus val) {
            status = val;
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

        public Booking build() {
            return new Booking(this);
        }
    }
}
