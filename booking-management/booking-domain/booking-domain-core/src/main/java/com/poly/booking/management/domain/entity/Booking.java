package com.poly.booking.management.domain.entity;

import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.valueobject.TrackingId;
import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Entity Booking đại diện cho một lượt đặt phòng khách sạn.
 * Quản lý trạng thái, thông tin khách hàng, phòng, dịch vụ, QR check-in, v.v.
 */
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
    private List<Room> rooms;
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
        rooms = builder.rooms;
        failureMessages = builder.failureMessages;
    }

    /**
     * Khởi tạo booking ở trạng thái PENDING.
     */
    public void initiateBooking() {
        setId(new BookingId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        validateDateRange();
        status = EBookingStatus.PENDING;
    }

    /**
     * Đặt cọc booking, chuyển sang trạng thái DEPOSITED.
     */
    public void depositBooking() {
        validateStatusForDeposit();
        status = EBookingStatus.DEPOSITED;
    }

    /**
     * Xác nhận đặt cọc, chuyển sang trạng thái CONFIRMED.
     */
    public void confirmBooking() {
        validateStatusForConfirmDeposit();
        qrCodeCheckIn = initQrCode();
        status = EBookingStatus.CONFIRMED;
    }

    /**
     * Check-in thành công, chuyển sang trạng thái CHECKED_IN.
     */
    public void checkIn() {
        validateStatusForCheckIn();
        status = EBookingStatus.CHECKED_IN;
    }

    /**
     * Thanh toán thành công, chuyển sang trạng thái PAID.
     */
    public void paidBooking() {
        validateStatusForPaid();
        status = EBookingStatus.PAID;
    }

    /**
     * Hủy booking khi thanh toán thất bại hoặc khi cần rollback.
     */
    public void cancelBooking() {
        validateStatusForCancel();
        status = EBookingStatus.CANCELLED;
    }

    /**
     * Hủy booking đã xác nhận.
     */
    public void cancelConfirmedBooking() {
        validateStatusForCancelConfirmed();
        status = EBookingStatus.CANCELLED;
    }

    /**
     * Check-out thành công, chuyển sang trạng thái CHECKED_OUT.
     */
    public void checkOut() {
        validateStatusForCheckOut();
        status = EBookingStatus.CHECKED_OUT;
    }

    // ================= PRIVATE VALIDATION METHODS =================

    private String formatData() {
        String bookingId = getId().getValue().toString();
        String customerName = customerId.toString();
        String checkInDateStr = getCheckInDate().toString();
        String checkOutDateStr = getCheckOutDate().toISOString();
        String roomsListString = rooms.stream()
                .map(Room::getRoomNumber)
                .collect(Collectors.joining(", "));
        String totalPriceStr = getTotalPrice().toString();

        return String.format(
                "Mã booking: %s|Tên khách hàng: %s|Ngày check-in: %s|Ngày check-out: %s|Phòng đã đặt: %s|Tổng tiền: %s",
                bookingId, customerName, checkInDateStr, checkOutDateStr, roomsListString, totalPriceStr);
    }

    private QRCodeCheckIn initQrCode() {
        qrCodeCheckIn = new QRCodeCheckIn();
        qrCodeCheckIn.initQRCode(formatData());
        return qrCodeCheckIn;
    }

    private void validateDateRange() {
        if (checkInDate.isAfter(checkOutDate)) {
            throw new BookingDomainException("Check-in date must be before check-out date");
        }
    }

    private void validateTotalPrice() {
        if (totalPrice == null || !totalPrice.isGreaterThanZero()) {
            throw new BookingDomainException("Total price must be greater than zero");
        }
    }

    private void validateStatusForDeposit() {
        if (!EBookingStatus.PENDING.equals(status)) {
            throw new BookingDomainException("Booking is not pending for deposit");
        }
    }

    private void validateStatusForConfirmDeposit() {
        if (!EBookingStatus.PENDING.equals(status)) {
            throw new BookingDomainException("Booking is not pending for confirmation");
        }
    }

    private void validateStatusForCheckIn() {
        if (!EBookingStatus.CONFIRMED.equals(status)) {
            throw new BookingDomainException("Booking is not confirmed for check-in");
        }
    }

    private void validateStatusForPaid() {
        if (!EBookingStatus.CHECKED_IN.equals(status)) {
            throw new BookingDomainException("Booking is not checked-in for payment");
        }
    }

    private void validateStatusForCancel() {
        if (!(EBookingStatus.PENDING.equals(status) || EBookingStatus.CHECKED_OUT.equals(status))) {
            throw new BookingDomainException("Booking is not in a cancellable state");
        }
    }

    private void validateStatusForCancelConfirmed() {
        if (!EBookingStatus.CONFIRMED.equals(status)) {
            throw new BookingDomainException("Booking is not confirmed for cancellation");
        }
        if (isRegularBooking()) {
            throw new BookingDomainException("Booking is not regular for cancellation");
        }
    }

    private void validateStatusForCheckOut() {
        if (!EBookingStatus.CHECKED_IN.equals(status)) {
            throw new BookingDomainException("Booking is not checked-in for check-out");
        }
    }

    private boolean isRegularBooking() {
        return checkInDate.getDay() - DateCustom.now().getDay() <= 3;
    }

    // ================= GETTER/SETTER =================
    public EBookingStatus getStatus() {
        return status;
    }

    public void setTotalPrice(Money totalPrice) {
        this.totalPrice = totalPrice;
        validateTotalPrice();
    }


    public void setOrder(Order order) {
        this.order = order;
    }

    public void updateAndValidateTotalPrice() {
        Money currentTotalRoomPrice = rooms != null ? rooms.stream()
                .map(Room::getBasePrice)
                .reduce(Money.ZERO, Money::add) : Money.ZERO;
        Money currentTotalOrderPrice = order != null ? order.getTotalCost() : Money.ZERO;
        this.totalPrice = currentTotalRoomPrice
                .add(currentTotalOrderPrice);
        validateTotalPrice();
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
