# 🍽️ Restaurant Service Configuration Guide

## 📋 Tổng quan

Restaurant Service được cấu hình với các file YAML cho các môi trường khác nhau:

- `application.yml` - Cấu hình chung
- `application-dev.yml` - Môi trường development
- `application-test.yml` - Môi trường test
- `application-prod.yml` - Môi trường production

## 🚀 Cấu hình chính

### 1. Server Configuration
```yaml
server:
  port: 8082
```

### 2. Spring Boot Configuration
```yaml
spring:
  application:
    name: restaurant-service
  profiles:
    active: dev
```

### 3. Eureka Service Discovery
```yaml
spring:
  eureka:
    client:
      service-url:
        defaultZone: http://localhost:8761/eureka/
      register-with-eureka: true
      fetch-registry: true
    instance:
      prefer-ip-address: true
```

### 4. Database Configuration (PostgreSQL)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/postgres?currentSchema=restaurant
    username: postgres
    password: admin
    driver-class-name: org.postgresql.Driver
  jpa:
    open-in-view: false
    show-sql: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

### 5. Kafka Configuration
```yaml
kafka-config:
  bootstrap-servers: localhost:19092, localhost:29092, localhost:39092
  schema-registry-url: http://localhost:8081
  num-of-partitions: 3
  replication-factor: 3
```

## 📊 Kafka Topics

### Restaurant Service Topics:
- `restaurant-payment-request` - Gửi yêu cầu thanh toán
- `restaurant-payment-response` - Nhận phản hồi thanh toán
- `restaurant-room-order-request` - Gửi yêu cầu đính kèm order vào room
- `restaurant-room-order-response` - Nhận phản hồi từ room service

### Consumer Groups:
- `restaurant-payment-topic-consumer` - Payment messages
- `restaurant-room-order-topic-consumer` - Room order messages

## 🔗 Service Dependencies

### External Services:
- **Payment Service**: `http://localhost:8092`
- **Customer Service**: `http://localhost:8093`
- **Notification Service**: `http://localhost:8091`
- **Room Service**: `http://localhost:8087`

## 🗄️ Database Schema

### Tables:
- `restaurant.categories` - Danh mục món ăn
- `restaurant.menu_items` - Các món ăn trong menu
- `restaurant.orders` - Đơn hàng
- `restaurant.order_items` - Chi tiết đơn hàng

### Schema Initialization:
- File: `init-schemainit-schema.sql`
- Tự động tạo schema và sample data
- Chỉ chạy trong development và test

## 🌍 Environment Profiles

### Development (dev)
- Port: 8082
- Database: PostgreSQL với schema `restaurant`
- Logging: DEBUG level
- Kafka: Local development topics

### Test
- Port: 8082
- Database: PostgreSQL với schema `restaurant_test`
- Logging: INFO level
- Kafka: Test topics với suffix `-test`

### Production
- Port: 8082
- Database: PostgreSQL với environment variables
- Logging: INFO level với file rotation
- Kafka: Production topics với suffix `-prod`
- Security: Basic auth enabled
- Monitoring: Health checks và metrics

## 🔧 Environment Variables (Production)

```bash
# Database
DB_HOST=postgres
DB_PORT=5432
DB_NAME=postgres
DB_USERNAME=postgres
DB_PASSWORD=admin

# Kafka
KAFKA_BOOTSTRAP_SERVERS=localhost:19092,localhost:29092,localhost:39092
SCHEMA_REGISTRY_URL=http://localhost:8081

# Services
PAYMENT_SERVICE_URL=http://payment-service:8092
CUSTOMER_SERVICE_URL=http://customer-service:8093
NOTIFICATION_SERVICE_URL=http://notification-service:8091
ROOM_SERVICE_URL=http://room-service:8087

# Security
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123
```

## 🚀 Khởi chạy Service

### Development:
```bash
cd restaurant-management/restaurant-container
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Test:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

### Production:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## 📊 Monitoring & Health Checks

### Health Endpoint:
- URL: `http://localhost:8082/actuator/health`
- Method: GET

### Metrics Endpoint:
- URL: `http://localhost:8082/actuator/metrics`
- Method: GET

### Prometheus Metrics:
- URL: `http://localhost:8082/actuator/prometheus`
- Method: GET

## 🔍 Troubleshooting

### 1. Database Connection Issues
- Kiểm tra PostgreSQL có đang chạy không
- Verify connection string và credentials
- Check schema `restaurant` đã được tạo chưa

### 2. Kafka Connection Issues
- Kiểm tra Kafka cluster có đang chạy không
- Verify bootstrap servers configuration
- Check Schema Registry URL

### 3. Service Discovery Issues
- Kiểm tra Eureka Server có đang chạy không
- Verify service registration
- Check network connectivity

### 4. Topic Creation Issues
- Topics sẽ được tạo tự động khi service khởi động
- Kiểm tra Kafka cluster permissions
- Verify topic naming conventions

## 📝 Logs

### Development Logs:
```yaml
logging:
  level:
    com.poly.restaurant: DEBUG
    org.hibernate.SQL: DEBUG
    org.springframework.kafka: DEBUG
```

### Production Logs:
```yaml
logging:
  file:
    name: logs/restaurant-service.log
    max-size: 100MB
    max-history: 30
```

## 🔄 Migration từ MySQL sang PostgreSQL

### Thay đổi chính:
1. **Database URL**: Từ MySQL sang PostgreSQL
2. **Driver**: Từ `com.mysql.cj.jdbc.Driver` sang `org.postgresql.Driver`
3. **Dialect**: Từ `MySQLDialect` sang `PostgreSQLDialect`
4. **Schema**: Sử dụng schema `restaurant` thay vì database riêng

### Dependencies cần thêm:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

## ✅ Checklist Deployment

- [ ] PostgreSQL database đã được setup
- [ ] Kafka cluster đã được khởi động
- [ ] Eureka Server đã được khởi động
- [ ] Schema `restaurant` đã được tạo
- [ ] Environment variables đã được cấu hình (production)
- [ ] Service dependencies đã được khởi động
- [ ] Health checks đã pass
- [ ] Logs đã được monitor

---

**🎯 Mục tiêu**: Cung cấp cấu hình hoàn chỉnh cho Restaurant Service với khả năng scale và maintain cao, hỗ trợ đầy đủ các môi trường development, test và production.
