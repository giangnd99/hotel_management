# Restaurant Management System

## Tổng quan

Restaurant Management System là một hệ thống quản lý nhà hàng microservice được xây dựng theo kiến trúc Domain-Driven Design (DDD) và sử dụng Kafka messaging để giao tiếp giữa các service.

## Kiến trúc hệ thống

### 1. Cấu trúc thư mục
```
restaurant-management/
├── restaurant-messaging/          # Kafka messaging module
├── restaurant-domain/             # Domain layer
│   ├── restaurant-domain-core/    # Core domain entities
│   └── restaurant-application-service/ # Application services
├── restaurant-container/          # Container configuration
├── restaurant-data-access/        # Data access layer
└── README.md                      # This file
```

### 2. Kiến trúc DDD
- **Domain Layer**: Chứa business logic và entities
- **Application Layer**: Xử lý use cases và orchestration
- **Infrastructure Layer**: Kafka messaging, database access
- **Presentation Layer**: REST APIs (nếu có)

## Tính năng chính

### 1. Order Management
- **Direct Payment Orders**: Tạo order với thanh toán trực tiếp
- **Room-Attached Orders**: Tạo order đính kèm vào phòng, thanh toán khi checkout
- **Order Status Management**: Quản lý trạng thái order (NEW, IN_PROGRESS, COMPLETED, CANCELLED)

### 2. Menu Management
- **Menu Items**: Quản lý món ăn, giá cả, danh mục
- **Inventory Control**: Kiểm soát số lượng tồn kho
- **Category Management**: Phân loại món ăn

### 3. Table Management
- **Table Status**: Quản lý trạng thái bàn (AVAILABLE, RESERVED, OCCUPIED)
- **Table Reservation**: Đặt bàn và quản lý đặt chỗ
- **Table Operations**: Sử dụng bàn, giải phóng bàn

### 4. Customer Management
- **Customer Information**: Thông tin khách hàng
- **Order History**: Lịch sử đặt hàng
- **Room Association**: Liên kết với phòng khách sạn

## Kafka Messaging

### 1. Topics được sử dụng
- **payment-request**: Gửi payment request đến Payment Service
- **payment-response**: Nhận payment response từ Payment Service
- **room-approval-request**: Gửi room order request đến Room Service
- **room-approval-response**: Nhận room order response từ Room Service
- **restaurant-request**: Gửi restaurant request
- **restaurant-response**: Nhận restaurant response

### 2. Message Models
- **RestaurantPaymentRequestMessage**: Payment request message
- **RestaurantPaymentResponseMessage**: Payment response message
- **RestaurantRoomOrderRequestMessage**: Room order request message
- **RestaurantRoomOrderResponseMessage**: Room order response message

### 3. Publishers & Consumers
- **PaymentRequestPublisher**: Publish payment requests
- **PaymentResponseConsumer**: Consume payment responses
- **RoomOrderRequestPublisher**: Publish room order requests
- **RoomOrderResponseConsumer**: Consume room order responses

## Cách triển khai

### 1. Prerequisites
- Java 17+
- Maven 3.6+
- Apache Kafka
- PostgreSQL (nếu sử dụng database)

### 2. Build và chạy

#### Bước 1: Build kafka-json-messaging
```bash
cd infrastructure/kafka/kafka-json-message
mvn clean install -DskipTests
```

#### Bước 2: Build restaurant-management
```bash
cd restaurant-management
mvn clean compile
```

#### Bước 3: Chạy Kafka
```bash
cd infrastructure/docker
docker-compose up -d
```

#### Bước 4: Chạy Restaurant Service
```bash
cd restaurant-management/restaurant-container
mvn spring-boot:run
```

### 3. Configuration
Cập nhật `application.yml` với các cấu hình:
```yaml
kafka:
  bootstrap-servers: localhost:9092
  group-id: restaurant-service-group

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/restaurant_db
    username: postgres
    password: password
```

## API Endpoints

### Order Management
- `POST /api/orders` - Tạo order mới
- `GET /api/orders/{id}` - Lấy thông tin order
- `PUT /api/orders/{id}/status` - Cập nhật trạng thái order
- `DELETE /api/orders/{id}` - Xóa order

### Menu Management
- `GET /api/menu` - Lấy danh sách menu
- `POST /api/menu` - Thêm món ăn mới
- `PUT /api/menu/{id}` - Cập nhật món ăn
- `DELETE /api/menu/{id}` - Xóa món ăn

### Table Management
- `GET /api/tables` - Lấy danh sách bàn
- `POST /api/tables` - Thêm bàn mới
- `PUT /api/tables/{id}/status` - Cập nhật trạng thái bàn
- `POST /api/tables/{id}/reserve` - Đặt bàn

## Business Logic

### 1. Order Processing Flow

#### Direct Payment Order
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

#### Room-Attached Order
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
11. Payment request + Room detach request
```

### 2. Status Transitions
- **NEW** → **IN_PROGRESS**: Khi bắt đầu xử lý order
- **IN_PROGRESS** → **COMPLETED**: Khi hoàn thành order
- **NEW/IN_PROGRESS** → **CANCELLED**: Khi hủy order

## Testing

### 1. Unit Tests
```bash
mvn test
```

### 2. Integration Tests
```bash
mvn verify
```

### 3. Manual Testing
- Sử dụng Postman hoặc curl để test API endpoints
- Kiểm tra Kafka messages bằng Kafka UI
- Monitor logs để verify business logic

## Monitoring và Logging

### 1. Logging
- Sử dụng SLF4J + Logback
- Structured logging với correlation IDs
- Log levels: DEBUG, INFO, WARN, ERROR

### 2. Metrics
- Kafka consumer lag monitoring
- Order processing time
- Error rates và success rates

### 3. Health Checks
- Kafka connectivity
- Database connectivity
- Service health status

## Troubleshooting

### 1. Common Issues
- **Kafka Connection**: Kiểm tra Kafka server và bootstrap servers
- **Message Processing**: Verify message format và validation
- **Database Issues**: Check connection string và credentials

### 2. Debug Mode
```yaml
logging:
  level:
    com.poly.restaurant: DEBUG
    org.springframework.kafka: DEBUG
```

### 3. Kafka Consumer Lag
```bash
# Check consumer lag
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group restaurant-service-group
```

## Future Enhancements

### 1. Planned Features
- **Saga Pattern**: Distributed transaction management
- **Circuit Breaker**: Handle external service failures
- **Caching**: Redis caching cho menu items
- **Analytics**: Order analytics và reporting

### 2. Performance Improvements
- **Async Processing**: Non-blocking order processing
- **Batch Operations**: Batch message processing
- **Connection Pooling**: Database connection optimization

### 3. Security
- **Authentication**: JWT token authentication
- **Authorization**: Role-based access control
- **Encryption**: Sensitive data encryption

## Contributing

### 1. Code Standards
- Follow Java coding conventions
- Use meaningful variable names
- Add comprehensive JavaDoc
- Write unit tests cho business logic

### 2. Git Workflow
- Create feature branches từ main
- Use descriptive commit messages
- Submit pull requests với detailed descriptions
- Ensure all tests pass before merging

### 3. Documentation
- Update README.md khi thêm tính năng mới
- Document API changes
- Update architecture diagrams

## Support

### 1. Team Contact
- **Lead Developer**: [Your Name]
- **Email**: [your.email@company.com]
- **Slack**: #restaurant-management

### 2. Resources
- **Architecture Docs**: [Link to architecture documentation]
- **API Documentation**: [Link to API docs]
- **Troubleshooting Guide**: [Link to troubleshooting guide]

### 3. Issue Reporting
- Use GitHub Issues cho bug reports
- Include error logs và reproduction steps
- Tag issues với appropriate labels

---

**Lưu ý**: Đây là tài liệu cơ bản. Vui lòng cập nhật khi có thay đổi trong hệ thống.
