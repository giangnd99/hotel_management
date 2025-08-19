# Room Check Out Kafka Publisher

## Tổng quan

`RoomCheckOutKafkaPublisher` là một Kafka publisher được triển khai theo pattern của dự án để gửi room check out request messages đến Kafka topic.

## Chức năng

- Gửi room check out request messages đến Kafka topic `room-check-out-request`
- Xử lý việc checkout phòng trong hệ thống khách sạn
- Đảm bảo tính nhất quán dữ liệu thông qua messaging pattern

## Patterns được áp dụng

### 1. AbstractKafkaPublisher Pattern
- Kế thừa từ `AbstractKafkaPublisher<String, BookingRoomRequestAvro, BookingRoomRequestMessage>`
- Tận dụng các tính năng có sẵn như logging, error handling, callback processing

### 2. Port-Adapter Pattern
- Implement interface `RoomCheckOutRequestPublisher`
- Tách biệt business logic khỏi messaging infrastructure
- Hỗ trợ testing và mocking

### 3. Mapper Pattern
- Sử dụng `RoomKafkaDataMapper` để chuyển đổi giữa domain message và Avro model
- Đảm bảo tính nhất quán dữ liệu giữa các service

## Cấu trúc

```java
@Component
public class RoomCheckOutKafkaPublisher
        extends AbstractKafkaPublisher<String, BookingRoomRequestAvro, BookingRoomRequestMessage>
        implements RoomCheckOutRequestPublisher {
    
    private final RoomKafkaDataMapper roomKafkaDataMapper;
    private final String topicName = "room-check-out-request";
    private final String messageName = "RoomCheckOutRequest";
}
```

## Sử dụng

### 1. Dependency Injection
```java
@Autowired
private RoomCheckOutRequestPublisher roomCheckOutRequestPublisher;
```

### 2. Gửi Message
```java
BookingRoomRequestMessage message = BookingRoomRequestMessage.builder()
    .id("uuid-string")
    .sagaId("saga-uuid-string")
    .bookingId("booking-123")
    .type("ROOM_CHECKOUT_REQUEST")
    .sagaStatus("STARTED")
    .bookingStatus("CONFIRMED")
    .createdAt(Instant.now())
    .build();

roomCheckOutRequestPublisher.publish(message);
```

## Configuration

### Kafka Topic
- **Topic Name**: `room-check-out-request`
- **Key Type**: String (sử dụng sagaId)
- **Value Type**: `BookingRoomRequestAvro`

### Message Structure
```json
{
  "id": "uuid",
  "sagaId": "uuid",
  "bookingId": "string",
  "createdAt": "timestamp",
  "processedAt": "timestamp|null",
  "type": "string",
  "sagaStatus": "string",
  "bookingStatus": "string",
  "price": "decimal",
  "rooms": "array|null"
}
```

## Error Handling

- Logging chi tiết cho success và error cases
- Exception propagation để business layer có thể xử lý
- Sử dụng callback pattern từ AbstractKafkaPublisher

## Testing

### Unit Test
```java
@ExtendWith(MockitoExtension.class)
class RoomCheckOutKafkaPublisherTest {
    
    @Mock
    private KafkaProducer<String, BookingRoomRequestAvro> kafkaProducer;
    
    @Mock
    private RoomKafkaDataMapper roomKafkaDataMapper;
    
    @InjectMocks
    private RoomCheckOutKafkaPublisher publisher;
    
    @Test
    void shouldPublishMessageSuccessfully() {
        // Test implementation
    }
}
```

### Integration Test
- Test với Kafka container
- Verify message được gửi đến đúng topic
- Test error scenarios

## Dependencies

- `com.poly.kafka.producer.AbstractKafkaPublisher`
- `com.poly.kafka.producer.service.KafkaProducer`
- `com.poly.room.management.kafka.mapper.RoomKafkaDataMapper`
- `com.poly.room.management.domain.port.out.publisher.request.RoomCheckOutRequestPublisher`

## Monitoring

- Log level: INFO cho success, ERROR cho failures
- Metrics: Message count, success rate, error rate
- Health check: Kafka connectivity

## Best Practices

1. **Message Ordering**: Sử dụng sagaId làm key để đảm bảo message ordering
2. **Error Handling**: Log đầy đủ thông tin lỗi để debugging
3. **Validation**: Validate message trước khi gửi
4. **Monitoring**: Theo dõi message delivery success/failure rate
5. **Testing**: Test đầy đủ các scenarios success và error

## Related Components

- `RoomCheckOutRequestPublisher` - Interface định nghĩa contract
- `RoomKafkaDataMapper` - Mapper chuyển đổi message
- `AbstractKafkaPublisher` - Base class cung cấp common functionality
- `BookingRoomRequestMessage` - Domain message model
- `BookingRoomRequestAvro` - Kafka Avro model
