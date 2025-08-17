# Restaurant Order Processing Implementation Guide

## Tổng quan

Hệ thống đã được triển khai 2 loại order process chính:

1. **Direct Payment Order**: Tạo order với thanh toán trực tiếp
2. **Room-Attached Order**: Tạo order đính kèm vào room, thanh toán khi checkout

## Kiến trúc tổng thể

### 1. Command Pattern
- `CreateOrderDirectlyCommand`: Xử lý order thanh toán trực tiếp
- `CreateOrderWithRoomDetailCommand`: Xử lý order đính kèm room

### 2. Kafka Messaging
- **Payment Messages**: Giao tiếp với payment service
- **Room Order Messages**: Giao tiếp với room service

### 3. Helper Classes
- `OrderProcessingHelper`: Logic xử lý order
- `PaymentMessageHelper`: Utility cho payment messages
- `RoomOrderMessageHelper`: Utility cho room order messages
- `OrderStatusHelper`: Quản lý trạng thái order

## Quy trình xử lý

### Direct Payment Order Flow

```
1. Client Request → OrderController.createDirectOrder()
2. OrderUseCase.createDirectOrder()
3. CreateOrderDirectlyCommand.createOrder()
4. OrderProcessingHelper.createDirectOrder()
5. OrderProcessingHelper.triggerDirectPaymentRequest()
6. PaymentRequestKafka.publish() → Payment Service
7. Payment Service Response → PaymentResponseKafka.receive()
8. PaymentResponseListenerImpl.onPaymentSuccess/Failure()
9. Order Status Update
```

### Room-Attached Order Flow

```
1. Client Request → OrderController.createRoomAttachedOrder()
2. OrderUseCase.createRoomAttachedOrder()
3. CreateOrderWithRoomDetailCommand.createOrderWithRoomDetail()
4. OrderProcessingHelper.createRoomAttachedOrder()
5. OrderProcessingHelper.triggerRoomOrderRequest()
6. RoomOrderRequestKafka.publish() → Room Service
7. Room Service Response → RoomOrderResponseKafka.receive()
8. RoomOrderResponseListenerImpl.onRoomOrderSuccess/Failure()
9. Order Status Update

Khi checkout:
10. OrderController.triggerRoomOrderPaymentRequest()
11. OrderProcessingHelper.triggerRoomOrderPaymentRequest()
12. Payment + Detach Room Order Requests
```

## Order Status Transitions

### Direct Payment Order
```
NEW → IN_PROGRESS → COMPLETED/CANCELLED
```

### Room-Attached Order
```
NEW → IN_PROGRESS (after room attachment) → COMPLETED/CANCELLED
```

## API Endpoints

### Direct Payment Order
```http
POST /api/restaurant/orders/direct
POST /api/restaurant/orders/{id}/trigger-direct-payment
```

### Room-Attached Order
```http
POST /api/restaurant/orders/room-attached
POST /api/restaurant/orders/{id}/trigger-room-payment
```

## Kafka Topics

### Payment Topics
- `restaurant-payment-request`: Gửi payment request
- `payment-response`: Nhận payment response

### Room Order Topics
- `restaurant-room-order-request`: Gửi room order request
- `restaurant-room-order-response`: Nhận room order response

## Configuration

### Application Properties
```yaml
restaurant-service:
  restaurant-payment-request-topic-name: restaurant-payment-request
  restaurant-payment-response-topic-name: payment-response
  restaurant-room-order-request-topic-name: restaurant-room-order-request
  restaurant-room-order-response-topic-name: restaurant-room-order-response

kafka:
  group-id: restaurant-payment-consumer-group
  topic:
    payment-response: payment-response
    room-order-response: restaurant-room-order-response
```

## Message Formats

### PaymentRequestMessage
```java
{
  "id": "uuid",
  "orderId": "order-123",
  "amount": "100.00",
  "orderPaymentStatus": "PENDING",
  "paymentMethod": "CREDIT_CARD"
}
```

### RoomOrderRequestMessage
```java
{
  "id": "uuid",
  "orderId": "order-123",
  "roomId": "room-456",
  "customerId": "customer-789",
  "amount": "100.00",
  "orderStatus": "NEW",
  "requestType": "ATTACH_ORDER"
}
```

## Error Handling

### Validation
- Order validation trong `OrderProcessingHelper`
- Message validation trong helper classes
- Status transition validation trong `OrderStatusHelper`

### Exception Handling
- Comprehensive logging ở tất cả layers
- Graceful error handling với fallback strategies
- Error propagation với meaningful messages

## Monitoring & Logging

### Key Log Points
- Order creation start/completion
- Payment request/response processing
- Room order request/response processing
- Status transitions
- Error conditions

### Metrics to Monitor
- Order creation success rate
- Payment processing time
- Room attachment success rate
- Message processing latency
- Error rates by operation type

## Best Practices

### Code Organization
- Separation of concerns với Command Pattern
- Helper classes cho common logic
- Consistent error handling patterns
- Comprehensive logging

### Message Processing
- Idempotent message handling
- Proper validation trước khi processing
- Graceful degradation khi external services fail

### Testing Strategy
- Unit tests cho helper classes
- Integration tests cho Kafka messaging
- End-to-end tests cho complete flows

## Future Enhancements

### Planned Improvements
1. **Saga Pattern**: Implement distributed transaction management
2. **Outbox Pattern**: Ensure message delivery reliability
3. **Circuit Breaker**: Handle external service failures
4. **Retry Logic**: Automatic retry for failed operations
5. **Event Sourcing**: Complete audit trail of order changes

### Scalability Considerations
1. **Message Partitioning**: Partition by order ID for parallel processing
2. **Caching**: Cache frequently accessed order data
3. **Database Optimization**: Indexes for order queries
4. **Load Balancing**: Distribute processing across instances

## Troubleshooting

### Common Issues
1. **Message Processing Failures**: Check Kafka connectivity và consumer group
2. **Order Status Inconsistencies**: Verify status transition logic
3. **Payment Failures**: Monitor payment service health
4. **Room Attachment Failures**: Check room service availability

### Debug Commands
```bash
# Check Kafka topics
kafka-topics --list --bootstrap-server localhost:9092

# Monitor messages
kafka-console-consumer --topic restaurant-payment-request --bootstrap-server localhost:9092

# Check consumer groups
kafka-consumer-groups --bootstrap-server localhost:9092 --list
```

## Dependencies

### Required Services
- **Kafka**: Message broker
- **Payment Service**: Payment processing
- **Room Service**: Room management
- **Customer Service**: Customer information
- **Notification Service**: Notifications

### Internal Dependencies
- `restaurant-domain`: Core domain logic
- `restaurant-messaging`: Kafka messaging
- `common-application`: Shared utilities
- `common-domain`: Shared domain objects
