package com.poly.booking.management.domain.mapper;

import com.poly.booking.management.domain.dto.RoomDto;
import com.poly.booking.management.domain.dto.message.CustomerCreatedMessageResponse;
import com.poly.booking.management.domain.dto.request.CreateBookingCommand;
import com.poly.booking.management.domain.dto.response.BookingCreatedResponse;
import com.poly.booking.management.domain.entity.Customer;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.event.BookingCreatedEvent;
import com.poly.booking.management.domain.event.BookingEvent;
import com.poly.booking.management.domain.event.BookingPaidEvent;
import com.poly.booking.management.domain.outbox.model.payment.BookingPaymentEventPayload;
import com.poly.booking.management.domain.outbox.model.room.BookingReservedEventPayload;
import com.poly.booking.management.domain.outbox.model.room.BookingRoomEventPayload;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
     * Chuyển đổi CustomerCreatedMessageResponse thành Customer Entity
     * <p>
     * Mục đích: Tạo customer entity từ message response khi nhận customer created event
     * Sử dụng: Trong CustomerListenerImpl để xử lý customer created event
     *
     * @param customerCreatedEvent CustomerCreatedMessageResponse từ Kafka message
     * @return Customer entity
     */
    public Customer customerCreatedEventToCustomer(CustomerCreatedMessageResponse customerCreatedEvent) {
        return Customer.createCustomer(
                UUID.fromString(customerCreatedEvent.getId()),
                customerCreatedEvent.getUsername(),
                customerCreatedEvent.getFirstName(),
                customerCreatedEvent.getLastName()
        );
    }

    /**
     * Chuyển đổi BookingPaidEvent thành BookingReservedEventPayload
     * <p>
     * Mục đích: Tạo payload cho Kafka message để thông báo room service về việc đặt phòng đã thanh toán
     * Sử dụng: Gửi message đến room service để cập nhật trạng thái phòng
     *
     * @param domainEvent BookingPaidEvent từ domain
     * @return BookingReservedEventPayload cho Kafka message
     */
    public BookingReservedEventPayload orderPaidEventToOrderApprovalEventPayload(BookingPaidEvent domainEvent) {
        return BookingReservedEventPayload.builder()
                .bookingId(domainEvent.getBooking().getId().getValue().toString())
                .customerId(domainEvent.getBooking().getCustomerId().getValue().toString())
                .bookingRoomId(domainEvent.getBooking().getId().getValue().toString())
                .price(domainEvent.getBooking().getTotalPrice().getAmount())
                .createdAt(domainEvent.getCreatedAt().getValue())
                .roomBookingStatus(domainEvent.getBooking().getStatus().toString())
                .rooms(domainEvent.getBooking().getRooms().stream()
                        .map(this::roomToBookingRoomEventPayload)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Chuyển đổi danh sách RoomDto thành danh sách Room entity
     * <p>
     * Mục đích: Chuyển đổi dữ liệu từ request DTO sang domain entity
     * Sử dụng: Khi tạo booking từ CreateBookingCommand
     *
     * @param roomsDto Danh sách RoomDto từ request
     * @return Danh sách Room entity
     */
    public List<Room> roomsDtoToRooms(List<RoomDto> roomsDto) {
        return roomsDto.stream().map(
                roomDto ->
                        new Room(new RoomId(roomDto.getRoomId()),
                                roomDto.getRoomNumber(),
                                Money.from(roomDto.getBasePrice()),
                                roomDto.getStatus())
        ).toList();
    }

    /**
     * Chuyển đổi BookingEvent thành BookingPaymentEventPayload
     * <p>
     * Mục đích: Tạo payload cho Kafka message để thông báo payment service về booking
     * Sử dụng: Gửi message đến payment service để xử lý thanh toán
     *
     * @param bookingCreatedEvent BookingEvent từ domain
     * @return BookingPaymentEventPayload cho Kafka message
     */
    public BookingPaymentEventPayload bookingEventToRoomBookingEventPayload(BookingEvent bookingCreatedEvent) {
        return BookingPaymentEventPayload.builder()
                .bookingId(bookingCreatedEvent.getBooking().getId().getValue().toString())
                .customerId(bookingCreatedEvent.getBooking().getCustomerId().getValue().toString())
                .paymentBookingStatus(bookingCreatedEvent.getBooking().getStatus().toString())
                .price(bookingCreatedEvent.getBooking().getTotalPrice().getAmount())
                .createdAt(bookingCreatedEvent.getCreatedAt().getValue())
                .build();
    }

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

    /**
     * Chuyển đổi BookingPaidEvent thành BookingReservedEventPayload
     * <p>
     * Mục đích: Tạo payload cho Kafka message để thông báo room service về việc đặt phòng đã thanh toán
     * Sử dụng: Gửi message đến room service để cập nhật trạng thái phòng
     *
     * @param domainEvent BookingPaidEvent từ domain
     * @return BookingReservedEventPayload cho Kafka message
     */
    public BookingReservedEventPayload bookingEventToRoomBookingEventPayload(BookingPaidEvent domainEvent) {
        return BookingReservedEventPayload.builder()
                .bookingId(domainEvent.getBooking().getId().getValue().toString())
                .customerId(domainEvent.getBooking().getCustomerId().getValue().toString())
                .bookingRoomId(domainEvent.getBooking().getId().getValue().toString())
                .price(domainEvent.getBooking().getTotalPrice().getAmount())
                .createdAt(domainEvent.getCreatedAt().getValue())
                .roomBookingStatus(domainEvent.getBooking().getStatus().toString())
                .rooms(domainEvent.getBooking().getRooms().stream()
                        .map(this::roomToBookingRoomEventPayload)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Chuyển đổi BookingPaidEvent thành BookingReservedEventPayload (alias method)
     * <p>
     * Mục đích: Tạo payload cho Kafka message để thông báo room service về việc đặt phòng đã thanh toán
     * Sử dụng: Gửi message đến room service để cập nhật trạng thái phòng
     *
     * @param domainEvent BookingPaidEvent từ domain
     * @return BookingReservedEventPayload cho Kafka message
     */
    public BookingReservedEventPayload bookingPaidEventToBookingReservedEventPayload(BookingPaidEvent domainEvent) {
        return BookingReservedEventPayload.builder()
                .bookingId(domainEvent.getBooking().getId().getValue().toString())
                .customerId(domainEvent.getBooking().getCustomerId().getValue().toString())
                .bookingRoomId(domainEvent.getBooking().getId().getValue().toString())
                .price(domainEvent.getBooking().getTotalPrice().getAmount())
                .createdAt(domainEvent.getCreatedAt().getValue())
                .roomBookingStatus(domainEvent.getBooking().getStatus().toString())
                .rooms(domainEvent.getBooking().getRooms().stream()
                        .map(this::roomToBookingRoomEventPayload)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Chuyển đổi Room entity thành BookingRoomEventPayload
     * <p>
     * Mục đích: Tạo payload cho room information trong booking event
     * Sử dụng: Trong booking event payload để gửi thông tin phòng
     *
     * @param room Room entity
     * @return BookingRoomEventPayload
     */
    private BookingRoomEventPayload roomToBookingRoomEventPayload(Room room) {
        return new BookingRoomEventPayload(
                room.getId().getValue().toString(),
                room.getRoomNumber(),
                "Standard", // Default room type name
                "Standard room", // Default room description
                1, // Default floor
                2, // Default capacity
                room.getBasePrice().getAmount()
        );
    }
}
