# Swagger Hub - Hotel Management

## Mô tả
Swagger Hub là một service tập trung để quản lý và hiển thị API documentation cho tất cả các service trong hệ thống Hotel Management. Thay vì phải truy cập từng service riêng lẻ với các port khác nhau, bạn có thể truy cập tất cả API docs từ một URL duy nhất.

## Tính năng
- 🏠 **Giao diện tập trung**: Một URL duy nhất cho tất cả API docs
- 🔄 **Chuyển đổi dễ dàng**: Dropdown trong Swagger UI để chọn service
- 🎨 **Giao diện chuẩn**: Sử dụng Swagger UI mặc định
- ⚡ **Proxy tự động**: Tự động fetch API docs từ các service
- 🔐 **Security**: Hỗ trợ JWT Bearer authentication

## Cách sử dụng

### 1. Khởi động service
```bash
# Từ thư mục gốc của project
mvn clean install
cd api-gateway/swagger-hub
mvn spring-boot:run
```

### 2. Truy cập Swagger Hub
Mở trình duyệt và truy cập: `http://localhost:8080/swagger-ui.html`

### 3. Chọn service
Sử dụng dropdown trong Swagger UI để chọn service cần xem API documentation:
- 📦 Inventory Service (Port 8081)
- 🍽️ Restaurant Service (Port 8082)
- 📅 Booking Management (Port 8083)
- 🏠 Room Management (Port 8087)
- 🤖 Spring AI Management (Port 8088)
- 👥 Staff Management (Port 8089)
- 🔧 Service Management (Port 8090)
- 🔔 Notification Management (Port 8091)
- 💳 Payment Management (Port 8092)
- 👤 Customer Management (Port 8093)
- 📊 Reporting Management (Port 8094)

## Cấu hình

### Thêm service mới
1. Cập nhật `application.yml` trong phần `services.swagger`
2. Thêm endpoint mới trong `SwaggerHubController`
3. Cập nhật `springdoc.swagger-ui.urls` trong `application.yml`

### Thay đổi port
Chỉ cần cập nhật URL trong `application.yml`:
```yaml
services:
  swagger:
    your-service:
      url: http://localhost:YOUR_PORT
      docs-path: /v3/api-docs
```

## Cấu trúc thư mục
```
swagger-hub/
├── src/main/java/com/poly/swaggerhub/
│   ├── SwaggerHubApplication.java
│   ├── config/
│   │   └── SwaggerHubConfig.java
│   └── controller/
│       └── SwaggerHubController.java
├── src/main/resources/
│   └── application.yml
├── pom.xml
├── README.md
├── run.bat
└── run.sh
```

## Lưu ý
- Đảm bảo các service khác đang chạy trước khi truy cập Swagger Hub
- Nếu service không khả dụng, sẽ hiển thị thông báo lỗi
- Swagger Hub sẽ tự động proxy các request API docs từ service gốc

## Troubleshooting

### Service không hiển thị
1. Kiểm tra service có đang chạy không
2. Kiểm tra port trong `application.yml`
3. Kiểm tra endpoint `/v3/api-docs` của service có hoạt động không

### Lỗi CORS
- Swagger Hub đã được cấu hình để xử lý CORS
- Nếu vẫn gặp lỗi, kiểm tra cấu hình CORS của service gốc

### Performance
- Swagger Hub sử dụng WebClient để fetch API docs
- Có thể cache kết quả để tăng performance nếu cần 