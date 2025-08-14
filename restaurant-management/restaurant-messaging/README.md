# Restaurant Management - Messaging Module

Module messaging cho Restaurant Management Service, chịu trách nhiệm xử lý các message events với Payment Service và các service khác.

## Cấu trúc thư mục

```
restaurant-messaging/
├── src/main/java/com/poly/restaurant/management/
│   ├── adapter/           # Adapters cho message listeners
│   ├── config/           # Configuration classes
│   ├── helper/           # Helper utility classes
│   ├── listener/         # Kafka listeners
│   ├── mapper/           # Message mappers
│   ├── message/          # Domain message models
│   └── publisher/        # Kafka publishers
```

## Components

### 1. Publishers

#### PaymentRequestKafka
- **Chức năng**: Gửi payment request messages đến Payment Service
- **Topic**: `restaurant-payment-request`
- **Message Type**: `PaymentRequestMessageAvro`
- **Features**:
  - Validation input parameters
  - Message serialization (Domain → Avro)
  - Error handling với callback
  - Comprehensive logging

### 2. Listeners

#### PaymentResponseKafka
- **Chức năng**: Lắng nghe payment response messages từ Payment Service
- **Topic**: `payment-response`
- **Message Type**: `PaymentResponseMessageAvro`
- **Features**:
  - Batch message processing
  - Message deserialization (Avro → Domain)
  - Payment status routing
  - Error handling và retry logic

### 3. Adapters

#### PaymentResponseListenerImpl
- **Chức năng**: Xử lý business logic cho payment responses
- **Features**:
  - Payment success/failure handling
  - Order status updates
  - Comprehensive logging
  - Error handling với compensation logic

### 4. Mappers

#### PaymentMessageMapper
- **Chức năng**: Chuyển đổi giữa domain messages và Avro messages
- **Mappings**:
  - `PaymentRequestMessage` ↔ `PaymentRequestMessageAvro`
  - `PaymentResponseMessage` ↔ `PaymentResponseMessageAvro`

### 5. Helpers

#### PaymentMessageHelper
- **Chức năng**: Utility methods cho payment message processing
- **Features**:
  - Message validation
  - Payment status checking
  - Message creation
  - Error logging

#### OrderStatusHelper
- **Chức năng**: Utility methods cho order status management
- **Features**:
  - Payment status to order status mapping
  - Status transition validation
  - Order status descriptions
  - Status change logging

## Message Flow

### Payment Request Flow
```
Order Creation → PaymentRequestKafka → Payment Service
```

1. Restaurant service tạo order
2. `PaymentRequestKafka` gửi payment request message
3. Payment service xử lý payment
4. Payment service gửi response message

### Payment Response Flow
```
Payment Service → PaymentResponseKafka → PaymentResponseListenerImpl → Order Update
```

1. Payment service gửi payment response
2. `PaymentResponseKafka` nhận và xử lý message
3. `PaymentResponseListenerImpl` cập nhật order status
4. Order được cập nhật trong database

## Configuration

### Kafka Topics
```yaml
restaurant-service:
  restaurant-payment-request-topic-name: restaurant-payment-request
  restaurant-payment-response-topic-name: payment-response
```

### Consumer Groups
```yaml
kafka:
  group-id: restaurant-payment-consumer-group
```

## Usage Examples

### Tạo Payment Request
```java
@Autowired
private PaymentRequestPublisher paymentRequestPublisher;

public void createOrderWithPayment(OrderDTO orderDTO) {
    PaymentRequestMessage paymentRequest = PaymentMessageHelper.createPaymentRequestMessage(
        orderDTO.id(),
        orderDTO.totalAmount().toString(),
        "CREDIT_CARD"
    );
    
    paymentRequestPublisher.publish(paymentRequest);
}
```

### Xử lý Payment Response
```java
@Component
public class PaymentResponseListenerImpl implements PaymentResponseListener {
    
    @Override
    public void onPaymentSuccess(PaymentResponseMessage message) {
        // Update order status to COMPLETED
        orderHandler.updateOrderStatus(message.getOrderId(), OrderStatus.COMPLETED);
    }
    
    @Override
    public void onPaymentFailure(PaymentResponseMessage message) {
        // Update order status to CANCELLED
        orderHandler.updateOrderStatus(message.getOrderId(), OrderStatus.CANCELLED);
    }
}
```

## Error Handling

### Publisher Errors
- Validation errors: Throw `IllegalArgumentException`
- Kafka errors: Log error và throw `RuntimeException`
- Network errors: Retry với exponential backoff

### Listener Errors
- Message validation: Skip invalid messages
- Business logic errors: Log error và có thể retry
- Database errors: Compensation logic

## Monitoring & Logging

### Log Levels
- **INFO**: Normal operations, status changes
- **WARN**: Invalid messages, status transitions
- **ERROR**: Processing failures, system errors
- **DEBUG**: Detailed message processing

### Key Metrics
- Message processing rate
- Error rate
- Processing latency
- Order status update success rate

## Best Practices

1. **Message Validation**: Luôn validate messages trước khi xử lý
2. **Error Handling**: Implement proper error handling và compensation logic
3. **Logging**: Log đầy đủ thông tin cho debugging và monitoring
4. **Idempotency**: Đảm bảo operations có thể retry safely
5. **Monitoring**: Monitor message processing metrics

## Dependencies

- Spring Kafka
- Apache Avro
- Lombok
- Spring Boot
- Common Kafka Infrastructure
