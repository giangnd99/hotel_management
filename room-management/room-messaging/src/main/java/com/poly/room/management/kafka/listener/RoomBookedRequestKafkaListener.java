package com.poly.room.management.kafka.listener;

import com.poly.booking.management.domain.kafka.model.BookingRoomRequestAvro;
import com.poly.kafka.consumer.KafkaConsumer;
import com.poly.room.management.domain.port.in.listener.request.BookingRoomReserveListener;
import com.poly.room.management.kafka.mapper.RoomKafkaDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
@RequiredArgsConstructor
public class RoomBookedRequestKafkaListener implements KafkaConsumer<BookingRoomRequestAvro> {

    private final BookingRoomReserveListener bookingRoomReserveListener;
    private final RoomKafkaDataMapper roomKafkaDataMapper;



    @Override
    @KafkaListener(topics = "room-approval-request", groupId = "room-approval-request")
    public void receive(
            @Payload List<BookingRoomRequestAvro> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("Received {} room booked request messages from Kafka", messages.size());

        messages.forEach(message -> {
            try {
                log.info("Processing room booked request message: {}", message);
                log.info("Message details - ID: {}, SagaId: {}, BookingId: {}, Status: {}, Rooms count: {}", 
                        message.getId(), message.getSagaId(), message.getBookingId(), 
                        message.getBookingStatus(), message.getRooms() != null ? message.getRooms().size() : 0);
                
                // Chuyển đổi Avro message thành domain message
                var domainMessage = roomKafkaDataMapper.toBookingRoomRequestMessage(message);
                log.info("Converted to domain message: {}", domainMessage);
                
                // Xử lý nghiệp vụ đặt cọc phòng
                bookingRoomReserveListener.onBookingRoomReserve(domainMessage);
                
                log.info("Successfully processed room booked request for booking ID: {}", message.getBookingId());
                
            } catch (Exception e) {
                log.error("Error processing room booked request message: {}", message, e);
            }
        });
    }
}
