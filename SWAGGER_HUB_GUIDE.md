# 🏨 Hotel Management - Swagger Hub Guide

## Tổng quan
Swagger Hub là giải pháp tập trung để quản lý API documentation cho tất cả các service trong hệ thống Hotel Management. Sử dụng **Swagger UI mặc định** với dropdown để chọn service, giúp bạn truy cập tất cả API docs từ một URL duy nhất: **http://localhost:8080/swagger-ui.html**

## 🚀 Cách sử dụng nhanh

### 1. Khởi động các service cần thiết
```bash
# Khởi động Eureka Server trước
cd api-gateway/eureka-server
mvn spring-boot:run

# Khởi động các service khác (chạy song song)
cd inventory-management/inventory-container
mvn spring-boot:run -Dspring-boot.run.profiles=dev

cd restaurant-management/restaurant-container  
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# ... các service khác
```

### 2. Khởi động Swagger Hub
```bash
# Từ thư mục gốc
cd api-gateway/swagger-hub
mvn spring-boot:run

# Hoặc sử dụng script
./run.sh  # Linux/Mac
run.bat   # Windows
```

### 3. Truy cập Swagger Hub
Mở trình duyệt: **http://localhost:8080/swagger-ui.html**

## 📋 Danh sách Service và Port

| Service | Port | Status | Description |
|---------|------|--------|-------------|
| **Swagger Hub** | 8080 | ✅ | Centralized API Documentation |
| Inventory Service | 8081 | ✅ | Quản lý kho hàng |
| Restaurant Service | 8082 | ✅ | Quản lý nhà hàng |
| Booking Management | 8083 | ✅ | Quản lý đặt phòng |
| Room Management | 8087 | ✅ | Quản lý phòng |
| Spring AI Management | 8088 | ✅ | AI và Machine Learning |
| Staff Management | 8089 | ✅ | Quản lý nhân viên |
| Service Management | 8090 | ✅ | Quản lý dịch vụ |
| Notification Management | 8091 | ✅ | Quản lý thông báo |
| Payment Management | 8092 | ✅ | Quản lý thanh toán |
| Customer Management | 8093 | ✅ | Quản lý khách hàng |
| Reporting Management | 8094 | ✅ | Báo cáo và thống kê |
| Eureka Server | 8761 | ✅ | Service Discovery |

## 🎯 Tính năng chính

### ✅ Đã hoàn thành
- [x] Giao diện tập trung cho tất cả API docs
- [x] Swagger UI mặc định với dropdown chọn service
- [x] Proxy tự động đến các service
- [x] JWT Bearer authentication support
- [x] CORS configuration
- [x] Hỗ trợ tất cả 11 service chính

### 🔄 Cách hoạt động
1. **Swagger Hub** chạy trên port 8080
2. Truy cập `http://localhost:8080/swagger-ui.html`
3. Sử dụng dropdown trong Swagger UI để chọn service
4. Swagger Hub sẽ proxy API docs và requests đến service tương ứng

## 🛠️ Cấu hình

### Dependencies (dựa trên Inventory Service)
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

### Thêm service mới
1. Cập nhật `application.yml` trong phần `services.swagger`
2. Thêm endpoint trong `SwaggerHubController.java`
3. Cập nhật `springdoc.swagger-ui.urls` trong `application.yml`

### Thay đổi port
Chỉ cần cập nhật URL trong `application.yml`:
```yaml
services:
  swagger:
    inventory:
      url: http://localhost:NEW_PORT
```

## 🔧 Troubleshooting

### Service không hiển thị
1. Kiểm tra service có đang chạy không
2. Kiểm tra port trong `application.yml`
3. Kiểm tra endpoint `/v3/api-docs` của service
4. Xem logs của Swagger Hub

### Lỗi CORS
- Swagger Hub đã được cấu hình CORS
- Kiểm tra cấu hình CORS của service gốc nếu cần

### Performance
- Swagger Hub sử dụng WebClient để fetch API docs
- Có thể thêm cache nếu cần tăng performance

## 📁 Cấu trúc Project

```
api-gateway/
├── swagger-hub/                    # 🆕 Swagger Hub Service
│   ├── src/main/java/com/poly/swaggerhub/
│   │   ├── SwaggerHubApplication.java
│   │   ├── config/SwaggerHubConfig.java
│   │   └── controller/SwaggerHubController.java
│   ├── src/main/resources/
│   │   └── application.yml
│   ├── pom.xml
│   ├── README.md
│   ├── run.bat
│   └── run.sh
├── eureka-server/
└── authentication-service/
```

## 🎉 Lợi ích

### Trước khi có Swagger Hub
- Phải nhớ port của từng service
- Phải mở nhiều tab để xem API docs
- Khó quản lý và so sánh API
- Không có giao diện thống nhất

### Sau khi có Swagger Hub
- ✅ Một URL duy nhất: `http://localhost:8080/swagger-ui.html`
- ✅ Dropdown trong Swagger UI để chọn service
- ✅ Giao diện Swagger UI chuẩn
- ✅ Dễ dàng so sánh và quản lý API
- ✅ Hỗ trợ JWT authentication
- ✅ Không cần nhớ port của từng service

## 🔐 Security Features

### JWT Bearer Authentication
- Tất cả API endpoints đều hỗ trợ JWT Bearer token
- Cấu hình security scheme trong Swagger UI
- Dễ dàng test API với authentication

### CORS Configuration
- Đã cấu hình CORS để cho phép cross-origin requests
- Hỗ trợ tất cả các service trong hệ thống

## 🚀 Next Steps

### Có thể cải thiện thêm
- [ ] Thêm authentication cho Swagger Hub
- [ ] Cache API docs để tăng performance
- [ ] Thêm metrics và monitoring
- [ ] Tích hợp với CI/CD pipeline
- [ ] Thêm search functionality
- [ ] Export API docs thành file

### Sử dụng trong production
- Cấu hình security
- Sử dụng HTTPS
- Thêm rate limiting
- Monitoring và logging
- Load balancing

---

**🎯 Mục tiêu đã đạt được**: Tạo một điểm tập trung để quản lý tất cả API documentation của hệ thống Hotel Management, sử dụng Swagger UI mặc định với dropdown để chọn service, giúp developers dễ dàng truy cập và quản lý API docs từ một URL duy nhất thay vì phải nhớ và chuyển đổi giữa nhiều port khác nhau. 