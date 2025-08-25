package com.poly.booking.management.domain.service.impl;

import com.poly.booking.management.domain.entity.*;
import com.poly.booking.management.domain.event.*;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.booking.management.domain.valueobject.BookingRoomId;
import com.poly.domain.valueobject.BookingId;
import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.RoomId;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * Triển khai các nghiệp vụ domain cho Booking.
 * Đảm bảo các bước nghiệp vụ đặt phòng, xác nhận, check-in, thanh toán, hủy, v.v. đúng logic và clean code.
 */
public class BookingDomainServiceImpl implements BookingDomainService {

    /**
     * Kiểm tra phòng còn trống, cập nhật giá, khởi tạo booking.
     *
     * @param booking booking cần khởi tạo
     * @param rooms   danh sách phòng khách sạn
     * @return BookingCreatedEvent
     */
    @Override
    public BookingCreatedEvent validateAndInitiateBooking(Booking booking, List<RoomId> requestRooms, List<Room> rooms) {
        List<Room> roomUpdated = setRoomInfoAndValidate(booking, requestRooms, rooms);
        setBookingRoom(booking, roomUpdated);
        booking.updateAndValidateTotalPrice();
        booking.initiateBooking();

        return new BookingCreatedEvent(booking, DateCustom.now());
    }

    private void setBookingRoom(Booking booking, List<Room> roomUpdated) {
        List<BookingRoom> bookingRooms = roomUpdated.stream().map(
                room -> BookingRoom.Builder.builder()
                        .room(room)
                        .price(room.getBasePrice())
                        .build()
        ).toList();
        booking.setBookingRooms(bookingRooms);
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

        return new BookingCancelledEvent(booking, DateCustom.now());
    }

    /**
     * Kiểm tra phòng còn trống, cập nhật thông tin phòng cho booking.
     *
     * @param booking
     * @param requestRooms booking cần cập nhật
     * @param rooms        danh sách phòng khách sạn
     */
    private List<Room> setRoomInfoAndValidate(Booking booking, List<RoomId> requestRooms, List<Room> rooms) {
        RoomManagement management = new RoomManagement(rooms);
        return management.setRoomsInformation(requestRooms);
    }
}
