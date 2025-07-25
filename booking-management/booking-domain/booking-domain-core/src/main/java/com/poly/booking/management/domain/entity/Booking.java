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

    private TrackingId trackingId;

    private DateCustom actualCheckInDate;
    private DateCustom actualCheckOutDate;

    private RestaurantId restaurantId;
    private ServiceId serviceId;

    private Money totalPrice;
    private String upgradeSuggestion;
    private QRCodeCheckIn qrCodeCheckIn;

    private Order order;
    private List<Service> service;
    public List<BookingRoom> bookingRooms;

    private List<String> failureMessages;
    public static final String FAILURE_MESSAGE_DELIMITER = ",";

    private void validateDateRange() {
        if (checkInDate.isAfter(checkOutDate)) {
            throw new BookingDomainException("Check-in date must be before check-out date");
        }
    }

    public void validateTotalPrice() {
        if(!totalPrice.isGreaterThanZero()){
            throw new BookingDomainException("Total price must be greater than zero");
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

    private void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
        }
        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }

    public void setTotalPrice(Money totalPrice) {
        validateTotalPrice();
        this.totalPrice = totalPrice;
    }

}
