package com.poly.room.management.kafka.listener;

import com.poly.booking.management.domain.kafka.model.BookingRoomRequestAvro;
import com.poly.kafka.consumer.KafkaConsumer;
import com.poly.room.management.domain.port.in.listener.request.BookingRoomReserveListener;
import com.poly.room.management.kafka.mapper.RoomKafkaDataMapper;
import com.poly.room.management.kafka.publisher.RoomBookedResponseKafkaPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
@RequiredArgsConstructor
public class RoomBookedRequestKafkaListener implements KafkaConsumer<BookingRoomRequestAvro> {

    private final BookingRoomReserveListener bookingRoomReserveListener;
    private final RoomKafkaDataMapper roomKafkaDataMapper;
    private final RoomBookedResponseKafkaPublisher roomBookedResponseKafkaPublisher;


    @Override
    public void receive(List<BookingRoomRequestAvro> messages, List<String> keys, List<Integer> partitions, List<Long> offsets) {

    }
}
