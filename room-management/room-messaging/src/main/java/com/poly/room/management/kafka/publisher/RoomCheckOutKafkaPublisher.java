package com.poly.room.management.kafka.publisher;

import com.poly.booking.management.domain.kafka.model.BookingRoomRequestAvro;
import com.poly.kafka.producer.AbstractKafkaPublisher;
import com.poly.kafka.producer.service.KafkaProducer;
import com.poly.room.management.domain.message.BookingRoomRequestMessage;
import com.poly.room.management.domain.port.out.publisher.request.RoomCheckOutRequestPublisher;
import com.poly.room.management.kafka.mapper.RoomKafkaDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Room Check Out Kafka Publisher
 * <p>
 * CHỨC NĂNG:
 * - Gửi room check out request messages đến Kafka topic
 * - Xử lý việc checkout phòng trong hệ thống khách sạn
 * - Đảm bảo tính nhất quán dữ liệu thông qua messaging pattern
 * <p>
 * MỤC ĐÍCH:
 * - Gửi thông tin checkout phòng đến các service khác
 * - Xử lý bất đồng bộ cho việc checkout phòng
 * - Đảm bảo message delivery reliability
 * <p>
 * PATTERNS ÁP DỤNG:
 * - AbstractKafkaPublisher Pattern: Kế thừa từ base publisher
 * - Port-Adapter Pattern: Implement RoomCheckOutRequestPublisher interface
 * - Mapper Pattern: Chuyển đổi giữa domain message và Avro model
 * <p>
 * FLOW XỬ LÝ:
 * 1. Nhận room check out request message
 * 2. Chuyển đổi thành Avro model
 * 3. Gửi message đến Kafka topic
 * 4. Xử lý callback và logging
 */
@Slf4j
@Component
public class RoomCheckOutKafkaPublisher
        extends AbstractKafkaPublisher<String, BookingRoomRequestAvro, BookingRoomRequestMessage>
        implements RoomCheckOutRequestPublisher {

    private final RoomKafkaDataMapper roomKafkaDataMapper;
    private final String topicName = "room-check-out-request";
    private final String messageName = "RoomCheckOutRequest";

    protected RoomCheckOutKafkaPublisher(KafkaProducer<String, BookingRoomRequestAvro> kafkaProducer, 
                                       RoomKafkaDataMapper roomKafkaDataMapper) {
        super(kafkaProducer);
        this.roomKafkaDataMapper = roomKafkaDataMapper;
    }

    @Override
    public void publish(BookingRoomRequestMessage bookingRoomRequestMessage) {
        log.info("Publishing room check out request for booking: {}", 
                bookingRoomRequestMessage.getBookingId());
        
        try {
            // Gọi method publish từ AbstractKafkaPublisher
            super.publish(bookingRoomRequestMessage);
            
            log.info("Room check out request published successfully for booking: {}", 
                    bookingRoomRequestMessage.getBookingId());
                    
        } catch (Exception e) {
            log.error("Error publishing room check out request for booking: {}", 
                    bookingRoomRequestMessage.getBookingId(), e);
            throw new RuntimeException("Failed to publish room check out request", e);
        }
    }

    @Override
    protected String getTopicName() {
        return topicName;
    }

    @Override
    protected String getKey(BookingRoomRequestMessage message) {
        // Sử dụng sagaId làm key để đảm bảo message ordering
        return message.getSagaId();
    }

    @Override
    protected BookingRoomRequestAvro toAvro(BookingRoomRequestMessage message) {
        // Chuyển đổi domain message thành Avro model
        return roomKafkaDataMapper.toBookingRoomRequestAvro(message);
    }

    @Override
    protected String getMessageName() {
        return messageName;
    }
}
