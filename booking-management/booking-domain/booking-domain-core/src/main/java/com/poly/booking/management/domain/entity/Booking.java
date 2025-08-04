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
    private List<Service> services;
    public List<Room> rooms;

    private List<String> failureMessages;
    public static final String FAILURE_MESSAGE_DELIMITER = ",";

    private Booking(Builder builder) {
        super.setId(builder.id);
        customerId = builder.customerId;
        checkInDate = builder.checkInDate;
        checkOutDate = builder.checkOutDate;
        status = builder.status;
        trackingId = builder.trackingId;
        setActualCheckInDate(builder.actualCheckInDate);
        setActualCheckOutDate(builder.actualCheckOutDate);
        restaurantId = builder.restaurantId;
        serviceId = builder.serviceId;
        setTotalPrice(builder.totalPrice);
        upgradeSuggestion = builder.upgradeSuggestion;
        qrCodeCheckIn = builder.qrCodeCheckIn;
        setOrder(builder.order);
        services = builder.services;
        rooms = builder.rooms;
        failureMessages = builder.failureMessages;
    }

    private void validateDateRange() {
        if (checkInDate.isAfter(checkOutDate)) {
            throw new BookingDomainException("Check-in date must be before check-out date");
        }
    }

    public void validateTotalPrice() {
        if (!totalPrice.isGreaterThanZero()) {
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

    public void paidBooking() {
        if (!EBookingStatus.CHECKED_IN.equals(status)) {
            throw new BookingDomainException("Booking is not checked-in for payment");
        }
        status = EBookingStatus.PAID;
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

    public EBookingStatus getStatus() {
        return status;
    }

    public void setTotalPrice(Money totalPrice) {
        validateTotalPrice();
        this.totalPrice = totalPrice;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void updateAndValidateTotalPrice() {
        Money currentTotalRoomPrice = rooms.stream()
                .map(Room::getBasePrice)
                .reduce(Money.ZERO, Money::add);
        Money currentTotalServicePrice = services.stream()
                .map(Service::getTotalCost)
                .reduce(Money.ZERO, Money::add);
        Money currentTotalOrderPrice = order.getTotalCost();

        this.totalPrice = currentTotalRoomPrice
                .add(currentTotalServicePrice)
                .add(currentTotalOrderPrice);
    }

    public Money getTotalPrice() {
        return totalPrice;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public List<Room> getRooms() {
        return rooms;
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

    public DateCustom getActualCheckOutDate() {
        return actualCheckOutDate;
    }

    public static final class Builder {
        private BookingId id;
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
        private List<Service> services;
        private List<Room> rooms;
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

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder serviceId(ServiceId val) {
            serviceId = val;
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

        public Builder qrCodeCheckIn(QRCodeCheckIn val) {
            qrCodeCheckIn = val;
            return this;
        }

        public Builder order(Order val) {
            order = val;
            return this;
        }

        public Builder services(List<Service> val) {
            services = val;
            return this;
        }

        public Builder rooms(List<Room> val) {
            rooms = val;
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
