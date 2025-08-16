# Restaurant Messaging Module

## Tổng quan

Restaurant Messaging Module được refactor để sử dụng **kafka-json-messaging** infrastructure theo chuẩn chung của hệ thống.

## Kiến trúc mới

### 1. Message Models
- **RestaurantPaymentRequestMessage**: Extends `BaseMessage` từ kafka-json-messaging
- **RestaurantPaymentResponseMessage**: Extends `BaseResponse` từ kafka-json-messaging  
- **RestaurantRoomOrderRequestMessage**: Extends `BaseMessage` từ kafka-json-messaging
- **RestaurantRoomOrderResponseMessage**: Extends `BaseResponse` từ kafka-json-messaging

### 2. Publishers
- **PaymentRequestPublisher**: Interface cho payment request
- **PaymentRequestKafka**: Implementation sử dụng `KafkaJsonProducer`
- **RoomOrderRequestPublisher**: Interface cho room order request
- **RoomOrderRequestKafka**: Implementation sử dụng `KafkaJsonProducer`

### 3. Consumers
- **PaymentResponseConsumer**: Xử lý payment response messages
- **RoomOrderResponseConsumer**: Xử lý room order response messages
- Sử dụng `@KafkaListener` với topic configuration

### 4. Configuration
- **RestaurantKafkaConfig**: Quản lý topic names từ KafkaTopicsConfig
- Sử dụng **chính xác** topics chung, không tạo topic mới

## Topics được sử dụng

### Payment Topics (Chung với các service khác)
- **payment-request**: Gửi payment request đến Payment Service
- **payment-response**: Nhận payment response từ Payment Service

### Room Order Topics (Chung với Room Service)
- **room-approval-request**: Gửi room order request đến Room Service
- **room-approval-response**: Nhận room order response từ Room Service

### Restaurant Topics (Chung với các service khác)
- **restaurant-request**: Gửi restaurant request
- **restaurant-response**: Nhận restaurant response

## Cách triển khai

### Bước 1: Build kafka-json-messaging
```bash
cd infrastructure/kafka/kafka-json-message
mvn clean install -DskipTests
```

### Bước 2: Build restaurant-messaging
```bash
cd restaurant-management/restaurant-messaging
mvn clean compile
```

### Bước 3: Cập nhật application.yml
```yaml
kafka:
  group-id: restaurant-service-group
```

## Chuẩn chung

### Message Structure
- Tất cả messages extend `BaseMessage` hoặc `BaseResponse`
- Sử dụng `messageId`, `correlationId`, `sourceService`, `targetService`
- Timestamp và retry count được quản lý tự động

### Topic Naming
- **KHÔNG tạo topic mới** để tránh ảnh hưởng đến các service khác
- Sử dụng chính xác topics từ `KafkaTopicsConfig`
- Payment topics: `payment-request`, `payment-response`
- Room topics: `room-approval-request`, `room-approval-response`
- Restaurant topics: `restaurant-request`, `restaurant-response`

### Error Handling
- Sử dụng `ResponseStatus` enum cho status
- Error codes và messages được chuẩn hóa
- Dead Letter Queue (DLQ) được cấu hình tự động

## Lợi ích

1. **Tính nhất quán**: Sử dụng cùng message format cho tất cả services
2. **Dễ maintain**: Infrastructure chung, logic riêng biệt
3. **Scalability**: Có thể áp dụng cho nhiều microservices khác
4. **Reliability**: Dedup, retry, DLQ được xử lý tự động
5. **Không xung đột**: Sử dụng topics chung, không ảnh hưởng service khác

## Future Enhancements

1. **AbstractConsumer**: Extend để sử dụng dedup và caching
2. **Saga Pattern**: Implement distributed transaction management
3. **Outbox Pattern**: Ensure message delivery reliability
4. **Circuit Breaker**: Handle external service failures
