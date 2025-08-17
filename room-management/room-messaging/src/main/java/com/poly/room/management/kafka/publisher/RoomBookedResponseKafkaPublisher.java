package com.poly.room.management.kafka.publisher;

import com.poly.booking.management.domain.kafka.model.BookingRoomResponseAvro;
import com.poly.kafka.producer.AbstractKafkaPublisher;
import com.poly.kafka.producer.service.KafkaProducer;
import com.poly.room.management.domain.port.out.publisher.reponse.BookingRoomReservePublisher;
import com.poly.room.management.kafka.mapper.RoomKafkaDataMapper;
import com.poly.room.management.kafka.message.BookingRoomResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoomBookedResponseKafkaPublisher
        extends AbstractKafkaPublisher<String, BookingRoomResponseAvro, BookingRoomResponseMessage>
        implements BookingRoomReservePublisher {

    private final RoomKafkaDataMapper roomKafkaDataMapper;
    private final String topicName = "room-approval-response";

    protected RoomBookedResponseKafkaPublisher(KafkaProducer<String, BookingRoomResponseAvro> kafkaProducer, RoomKafkaDataMapper roomKafkaDataMapper) {
        super(kafkaProducer);
        this.roomKafkaDataMapper = roomKafkaDataMapper;
    }

    @Override
    protected String getTopicName() {
        return topicName;
    }

    @Override
    protected String getKey(BookingRoomResponseMessage message) {
        return message.getSagaId();
    }

    @Override
    protected BookingRoomResponseAvro toAvro(BookingRoomResponseMessage message) {
        return roomKafkaDataMapper.toBookingRoomResponseAvro(message);
    }

    @Override
    protected String getMessageName() {
        return "Room Booked Response";
    }

    @Override
    public void publish(BookingRoomResponseMessage bookingRoomResponseMessage) {
        super.publish(bookingRoomResponseMessage);
    }

}
