package com.poly.booking.management.domain.service.impl;

import com.poly.booking.management.domain.entity.*;
import com.poly.booking.management.domain.event.*;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.valueobject.DateCustom;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Triển khai các nghiệp vụ domain cho Booking.
 * Đảm bảo các bước nghiệp vụ đặt phòng, xác nhận, check-in, thanh toán, hủy, v.v. đúng logic và clean code.
 */
@Slf4j
public class BookingDomainServiceImpl implements BookingDomainService {

    /**
     * Kiểm tra phòng còn trống, cập nhật giá, khởi tạo booking.
     *
     * @param booking booking cần khởi tạo
     * @param rooms   danh sách phòng khách sạn
     * @return BookingCreatedEvent
     */
    @Override
    public BookingCreatedEvent validateAndInitiateBooking(Booking booking, List<Room> rooms) {
        setRoomInfoAndValidate(booking, rooms);
        booking.updateAndValidateTotalPrice();
        booking.initiateBooking();
        log.info("Booking created with id: {}", booking.getId().getValue());
        return new BookingCreatedEvent(booking, DateCustom.now());
    }

    /**
     * Hủy đặt cọc booking đã xác nhận.
     *
     * @param booking booking cần hủy
     * @return BookingCancelledEvent
     */
    @Override
    public BookingCancelledEvent cancelDepositBooking(Booking booking) {
        booking.cancelConfirmedBooking();
        log.info("Deposit of booking cancelled with id: {}", booking.getId().getValue());
        return new BookingCancelledEvent(booking, DateCustom.now());
    }

    /**
     * Xác nhận đặt cọc booking, chuyển sang trạng thái CONFIRMED.
     *
     * @param booking booking cần xác nhận
     * @return BookingDepositedEvent
     */
    @Override
    public BookingDepositedEvent depositBooking(Booking booking) {
        booking.depositBooking();
        log.info("Booking deposit confirmed with id: {}", booking.getId().getValue());
        return new BookingDepositedEvent(booking, DateCustom.now());
    }

    /**
     * Xác nhận booking (nếu có logic riêng, hiện tại trả về null).
     *
     * @param booking booking cần xác nhận
     * @return BookingConfirmedEvent
     */
    @Override
    public BookingConfirmedEvent confirmBooking(Booking booking) {
        booking.confirmBooking();
        return new BookingConfirmedEvent(booking, DateCustom.now());
    }

    /**
     * Check-in booking, chuyển sang trạng thái CHECKED_IN.
     *
     * @param booking booking cần check-in
     * @return CheckInEvent
     */
    @Override
    public CheckInEvent checkInBooking(Booking booking) {
        booking.checkIn();
        log.info("Booking checked in with id: {}", booking.getId().getValue());
        return new CheckInEvent(booking, DateCustom.now());
    }

    /**
     * Cập nhật thông tin booking (chưa triển khai).
     *
     * @param booking booking cần cập nhật
     */
    @Override
    public void updateBooking(Booking booking) {
        // TODO: Triển khai cập nhật booking nếu cần
    }

    /**
     * Thanh toán booking, chuyển sang trạng thái PAID.
     *
     * @param booking booking cần thanh toán
     * @return BookingPaidEvent
     */
    @Override
    public BookingPaidEvent payBooking(Booking booking) {
        booking.paidBooking();
        log.info("Booking paid with id: {}", booking.getId().getValue());
        return new BookingPaidEvent(booking, DateCustom.now());
    }

    /**
     * Check-out booking, chuyển sang trạng thái CHECKED_OUT.
     *
     * @param booking booking cần check-out
     * @return CheckOutEvent
     */
    @Override
    public CheckOutEvent checkOutBooking(Booking booking) {
        booking.checkOut();
        log.info("Booking checked out with id: {}", booking.getId().getValue());
        return new CheckOutEvent(booking, DateCustom.now());
    }

    /**
     * Hủy booking (có thể là khi chưa xác nhận hoặc đã check-out).
     *
     * @param booking booking cần hủy
     * @return BookingCancelledEvent
     */
    @Override
    public BookingCancelledEvent cancelBooking(Booking booking) {
        booking.cancelBooking();
        log.info("Booking cancelled with id: {}", booking.getId().getValue());
        return new BookingCancelledEvent(booking, DateCustom.now());
    }

    /**
     * Kiểm tra phòng còn trống, cập nhật thông tin phòng cho booking.
     *
     * @param booking booking cần cập nhật
     * @param rooms   danh sách phòng khách sạn
     */
    private void setRoomInfoAndValidate(Booking booking, List<Room> rooms) {
        RoomManagement management = new RoomManagement(rooms);
        management.setRoomsInformation(booking);
    }
}
