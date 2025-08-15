# Service Management

Service Management là một microservice trong hệ thống quản lý khách sạn, chịu trách nhiệm quản lý các dịch vụ khách sạn và đơn đặt dịch vụ.

## Tính năng chính

### 1. Quản lý Dịch vụ (Services)
- Tạo, đọc, cập nhật, xóa dịch vụ
- Tìm kiếm dịch vụ theo tên, mô tả
- Lọc dịch vụ theo trạng thái, khoảng giá
- Quản lý tính khả dụng của dịch vụ

### 2. Quản lý Đơn đặt Dịch vụ (Service Orders)
- Tạo đơn đặt dịch vụ mới
- Theo dõi trạng thái đơn hàng
- Quản lý thanh toán
- Tìm kiếm đơn hàng theo nhiều tiêu chí

### 3. Tích hợp Kafka Messaging với Avro
- Gửi thông báo tạo đơn hàng
- Gửi yêu cầu thanh toán
- Nhận phản hồi thanh toán
- Cập nhật trạng thái tự động
- Serialization/Deserialization với Avro schemas

## Cấu trúc dự án

```
service-management/
├── src/main/java/edu/poly/servicemanagement/
│   ├── controller/           # REST Controllers
│   ├── service/             # Business Logic
│   ├── repository/          # Data Access Layer
│   ├── entity/              # JPA Entities
│   ├── dto/                 # Data Transfer Objects
│   ├── enums/               # Enumerations
│   ├── messaging/           # Kafka Messaging
│   │   ├── message/         # Message Models
│   │   ├── publisher/       # Kafka Publishers
│   │   ├── listener/        # Kafka Listeners
│   │   └── mapper/          # Message Mappers
│   └── config/              # Configuration Classes
├── src/main/resources/
│   ├── application.yml      # Application Configuration
│   └── db.sql              # Database Schema
└── pom.xml                 # Maven Dependencies
```

## Avro Schemas

Service Management sử dụng Avro schemas để đảm bảo type safety và consistency khi giao tiếp với các service khác. Các schemas được định nghĩa trong `infrastructure/kafka/kafka-model/src/main/resources/avro/`:

### Schemas chính:
- `service_order_request.avsc` - ServiceOrderRequestMessageAvro
- `service_order_response.avsc` - ServiceOrderResponseMessageAvro  
- `service_payment_request.avsc` - ServicePaymentRequestMessageAvro
- `service_payment_response.avsc` - ServicePaymentResponseMessageAvro
- `service_notification.avsc` - ServiceNotificationMessageAvro
- `service_model.avsc` - ServiceModelAvro

### Namespace:
Tất cả schemas đều sử dụng namespace: `edu.poly.servicemanagement.messaging.message`

## API Endpoints

### Services API
- `GET /api/services` - Lấy tất cả dịch vụ
- `GET /api/services/{id}` - Lấy dịch vụ theo ID
- `POST /api/services` - Tạo dịch vụ mới
- `PUT /api/services/{id}` - Cập nhật dịch vụ
- `DELETE /api/services/{id}` - Xóa dịch vụ

### Service Orders API
- `POST /api/service-orders` - Tạo đơn đặt dịch vụ
- `GET /api/service-orders` - Lấy tất cả đơn hàng
- `GET /api/service-orders/{id}` - Lấy đơn hàng theo ID
- `GET /api/service-orders/order-number/{orderNumber}` - Lấy đơn hàng theo số đơn hàng
- `GET /api/service-orders/customer/{customerId}` - Lấy đơn hàng theo khách hàng
- `GET /api/service-orders/room/{roomId}` - Lấy đơn hàng theo phòng
- `GET /api/service-orders/status/{status}` - Lấy đơn hàng theo trạng thái
- `PUT /api/service-orders/{id}/status` - Cập nhật trạng thái đơn hàng
- `PUT /api/service-orders/{id}/payment-status` - Cập nhật trạng thái thanh toán
- `PUT /api/service-orders/{id}/cancel` - Hủy đơn hàng
- `DELETE /api/service-orders/{id}` - Xóa đơn hàng

## Cấu hình

### Database
- **Database**: PostgreSQL
- **Schema**: service_management
- **Port**: 5433

### Kafka Topics
- `service-order-request` - Gửi yêu cầu tạo đơn hàng
- `service-order-response` - Nhận phản hồi đơn hàng
- `service-payment-request` - Gửi yêu cầu thanh toán
- `service-payment-response` - Nhận phản hồi thanh toán
- `service-notification` - Gửi thông báo

### Service Configuration
- **Port**: 8088
- **Application Name**: service-management
- **Eureka Client**: Enabled

## Luồng xử lý đơn hàng

### 1. Tạo đơn hàng
```
Client → POST /api/service-orders → ServiceOrderService → Database
                                    ↓
                              Kafka Publishers (Avro)
                                    ↓
                              service-order-request topic
                                    ↓
                              payment-request topic
```

### 2. Xử lý thanh toán
```
Payment Service → service-payment-response topic → PaymentResponseKafka (Avro)
                                                       ↓
                                               PaymentResponseListenerImpl
                                                       ↓
                                               Update Order Status
```

### 3. Trạng thái đơn hàng
- **NEW**: Đơn hàng mới được tạo
- **CONFIRMED**: Thanh toán thành công
- **IN_PROGRESS**: Đang xử lý
- **COMPLETED**: Hoàn thành
- **CANCELLED**: Đã hủy

### 4. Trạng thái thanh toán
- **PENDING**: Chờ thanh toán
- **PROCESSING**: Đang xử lý
- **COMPLETED**: Thanh toán thành công
- **FAILED**: Thanh toán thất bại
- **CANCELLED**: Đã hủy

## Chạy ứng dụng

### 1. Yêu cầu hệ thống
- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Kafka 2.8+
- Eureka Server
- Schema Registry

### 2. Cấu hình database
```sql
-- Tạo database và schema
CREATE DATABASE hotel_management;
CREATE SCHEMA service_management;
```

### 3. Build kafka-model trước
```bash
# Build kafka-model module để generate Avro classes
cd infrastructure/kafka/kafka-model
mvn clean install
```

### 4. Chạy ứng dụng
```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

### 5. Truy cập API
- **Swagger UI**: http://localhost:8088/swagger-ui.html
- **Health Check**: http://localhost:8088/actuator/health

## Ví dụ sử dụng

### Tạo đơn đặt dịch vụ
```bash
curl -X POST http://localhost:8088/api/service-orders \
  -H "Content-Type: application/json" \
  -d '{
    "serviceId": 1,
    "customerId": "customer_001",
    "roomId": "room_101",
    "quantity": 2,
    "specialInstructions": "Không cay"
  }'
```

### Lấy đơn hàng theo khách hàng
```bash
curl -X GET http://localhost:8088/api/service-orders/customer/customer_001
```

### Cập nhật trạng thái đơn hàng
```bash
curl -X PUT "http://localhost:8088/api/service-orders/1/status?status=CONFIRMED"
```

## Avro Message Examples

### Service Order Request
```json
{
  "orderId": "123",
  "orderNumber": "SO-20250115120000-abc123",
  "serviceId": 1,
  "customerId": "customer_001",
  "roomId": "room_101",
  "quantity": 2,
  "totalAmount": "200.00",
  "status": "NEW",
  "paymentStatus": "PENDING",
  "specialInstructions": "Không cay",
  "orderDate": "2025-01-15T12:00:00",
  "createdAt": "2025-01-15T12:00:00",
  "updatedAt": "2025-01-15T12:00:00"
}
```

### Payment Request
```json
{
  "orderId": "123",
  "orderNumber": "SO-20250115120000-abc123",
  "customerId": "customer_001",
  "amount": "200.00",
  "paymentMethod": "CREDIT_CARD",
  "currency": "VND",
  "description": "Payment for service order: SO-20250115120000-abc123"
}
```

## Monitoring và Logging

### Logging
- **Level**: DEBUG cho development
- **Format**: JSON với timestamp
- **Topics**: Service operations, Kafka messages, Database queries, Avro serialization

### Health Checks
- **Database**: Connection status
- **Kafka**: Producer/Consumer status
- **Eureka**: Service registration status
- **Schema Registry**: Schema availability

## Troubleshooting

### Lỗi thường gặp
1. **Database Connection**: Kiểm tra PostgreSQL service và credentials
2. **Kafka Connection**: Kiểm tra Kafka brokers và topics
3. **Eureka Registration**: Kiểm tra Eureka server URL
4. **Avro Schema**: Kiểm tra Schema Registry và schema compatibility
5. **Kafka Model**: Đảm bảo kafka-model đã được build và install

### Debug Mode
```bash
# Enable debug logging
export LOGGING_LEVEL_EDU_POLY_SERVICEMANAGEMENT=DEBUG
export LOGGING_LEVEL_ORG_APACHE_KAFKA=DEBUG
mvn spring-boot:run
```

### Schema Registry Issues
```bash
# Kiểm tra schema registry
curl -X GET http://localhost:8081/subjects

# Kiểm tra schema version
curl -X GET http://localhost:8081/subjects/service-order-request/versions
```
