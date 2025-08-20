package com.poly.room.management.kafka.publisher;

import com.poly.booking.management.domain.kafka.model.BookingRoomResponseAvro;
import com.poly.kafka.producer.AbstractKafkaPublisher;
import com.poly.kafka.producer.service.KafkaProducer;
import com.poly.room.management.domain.port.out.publisher.request.BookingCancellationRequestPublisher;
import com.poly.room.management.domain.message.RoomCancellationRequestMessage;
import com.poly.room.management.kafka.mapper.RoomKafkaDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Room Booking Cancellation Kafka Publisher
 * <p>
 * CHỨC NĂNG:
 * - Gửi room cancellation request messages đến Kafka topic
 * - Xử lý việc hủy đặt phòng trong hệ thống khách sạn
 * - Đảm bảo tính nhất quán dữ liệu thông qua messaging pattern
 * <p>
 * MỤC ĐÍCH:
 * - Gửi thông tin hủy đặt phòng đến các service khác
 * - Xử lý bất đồng bộ cho việc hủy đặt phòng
 * - Đảm bảo message delivery reliability
 * <p>
 * PATTERNS ÁP DỤNG:
 * - AbstractKafkaPublisher Pattern: Kế thừa từ base publisher
 * - Port-Adapter Pattern: Implement BookingCancellationRequestPublisher interface
 * - Mapper Pattern: Chuyển đổi giữa domain message và Avro model
 * <p>
 * FLOW XỬ LÝ:
 * 1. Nhận room cancellation request message
 * 2. Chuyển đổi thành Avro model
 * 3. Gửi message đến Kafka topic
 * 4. Xử lý callback và logging
 */
@Slf4j
@Component
public class RoomBookingCancellationPublisher
        extends AbstractKafkaPublisher<String, BookingRoomResponseAvro, RoomCancellationRequestMessage>
        implements BookingCancellationRequestPublisher {

    private final RoomKafkaDataMapper roomKafkaDataMapper;
    private final String topicName = "room-cancellation-request-topic";
    private final String messageName = "RoomCancellationRequest";

    protected RoomBookingCancellationPublisher(KafkaProducer<String, BookingRoomResponseAvro> kafkaProducer,
                                             RoomKafkaDataMapper roomKafkaDataMapper) {
        super(kafkaProducer);
        this.roomKafkaDataMapper = roomKafkaDataMapper;
    }

    @Override
    public void publish(RoomCancellationRequestMessage roomCancellationRequestMessage) {
        log.info("Publishing room cancellation request for booking: {}", 
                roomCancellationRequestMessage.getBookingId());
        
        try {
            // Gọi method publish từ AbstractKafkaPublisher
            super.publish(roomCancellationRequestMessage);
            
            log.info("Room cancellation request published successfully for booking: {}", 
                    roomCancellationRequestMessage.getBookingId());
                    
        } catch (Exception e) {
            log.error("Error publishing room cancellation request for booking: {}", 
                    roomCancellationRequestMessage.getBookingId(), e);
            throw new RuntimeException("Failed to publish room cancellation request", e);
        }
    }

    @Override
    protected String getTopicName() {
        return topicName;
    }

    @Override
    protected String getKey(RoomCancellationRequestMessage message) {
        // Sử dụng bookingId làm key để đảm bảo message ordering
        return message.getBookingId().toString();
    }

    @Override
    protected BookingRoomResponseAvro toAvro(RoomCancellationRequestMessage message) {
        // Chuyển đổi RoomCancellationRequestMessage thành BookingRoomResponseAvro
        // Sử dụng mapper để đảm bảo tính nhất quán dữ liệu
        return roomKafkaDataMapper.toBookingRoomResponseAvroFromCancellationRequest(message);
    }

    @Override
    protected String getMessageName() {
        return messageName;
    }
}
