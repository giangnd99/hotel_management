package com.poly.booking.management.domain.mapper;

import com.poly.booking.management.domain.dto.request.CreateBookingCommand;
import com.poly.booking.management.domain.dto.response.BookingCreatedResponse;
import com.poly.booking.management.domain.event.*;
import org.springframework.stereotype.Component;

/**
 * BookingDataMapper - Mapper class để chuyển đổi dữ liệu giữa các layer
 * <p>
 * Mục đích:
 * - Mapping giữa Domain Entity và DTO cho Frontend
 * - Mapping giữa Domain Event và Event Payload cho Kafka Message
 * - Đảm bảo tính nhất quán dữ liệu giữa các layer
 * - Tối ưu hóa việc serialize/deserialize cho JSON và Avro
 * <p>
 * EVENT DRIVEN MAPPING:
 * - CustomerCreatedMessageResponse -> Customer Entity
 * - Booking Events -> Event Payloads
 * - Domain Events -> Response DTOs
 */
@Component
public class BookingDataMapper {

    /**
     * Chuyển đổi BookingCreatedEvent thành BookingCreatedResponse
     * <p>
     * Mục đích: Tạo response DTO cho frontend sau khi tạo booking thành công
     * Sử dụng: Trả về thông tin booking cho client
     *
     * @param bookingCreatedEvent  BookingCreatedEvent từ domain
     * @param createBookingCommand Command ban đầu từ request
     * @return BookingCreatedResponse cho frontend
     */
    public BookingCreatedResponse bookingCreatedEventToBookingCreatedResponse(BookingCreatedEvent bookingCreatedEvent, CreateBookingCommand createBookingCommand) {
        return BookingCreatedResponse.builder()
                .bookingId(bookingCreatedEvent.getBooking().getId().getValue())
                .customerId(bookingCreatedEvent.getBooking().getCustomerId().getValue())
                .rooms(bookingCreatedEvent.getBooking().getRooms())
                .checkInDate(bookingCreatedEvent.getBooking().getCheckInDate().getValue())
                .checkOutDate(bookingCreatedEvent.getBooking().getCheckOutDate().getValue())
                .numberOfGuests(createBookingCommand.getNumberOfGuests())
                .totalAmount(bookingCreatedEvent.getBooking().getTotalPrice().getAmount())
                .bookingDate(bookingCreatedEvent.getCreatedAt().getValue())
                .status(bookingCreatedEvent.getBooking().getStatus())
                .build();
    }


}
