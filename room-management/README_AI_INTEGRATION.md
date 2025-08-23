# Hướng Dẫn Tích Hợp AI với Room Management

## Tổng Quan

Hệ thống Room Management đã được tích hợp với AI model thông qua Spring AI để cung cấp thông tin thông minh về khách sạn 5 sao Việt Nam. Tất cả dữ liệu được xử lý an toàn, không tiết lộ ID hoặc thông tin nhạy cảm.

## Tính Năng Bảo Mật

### ✅ Không Tiết Lộ ID
- Tất cả DTO đã loại bỏ ID fields
- Sử dụng tên, số phòng, hoặc mô tả để định danh
- Bảo vệ thông tin nội bộ khách sạn

### ✅ Dữ Liệu An Toàn
- Chỉ chia sẻ thông tin công khai cần thiết
- Không có thông tin cá nhân nhạy cảm
- Tuân thủ quy định bảo mật khách sạn

## Cấu Trúc DTO Mới

### 1. RoomResponse
```java
public class RoomResponse {
    private String roomNumber;           // Số phòng
    private int floor;                   // Tầng
    private String area;                 // Diện tích
    private RoomTypeResponse roomType;   // Loại phòng
    private String roomStatus;           // Trạng thái
    private String lastCleanedAt;        // Lần dọn dẹp cuối
    private String lastMaintenanceAt;    // Lần bảo trì cuối
    private String specialFeatures;      // Đặc điểm đặc biệt
    private String viewDescription;      // Mô tả view
    private String accessibilityFeatures; // Tiện nghi cho người khuyết tật
}
```

### 2. RoomTypeResponse
```java
public class RoomTypeResponse {
    private String typeName;             // Tên loại phòng
    private String description;          // Mô tả
    private String basePrice;            // Giá cơ bản
    private int maxOccupancy;            // Sức chứa tối đa
    private String amenities;            // Tiện nghi
    private String roomSize;             // Kích thước phòng
    private String bedConfiguration;     // Cấu hình giường
    private String bathroomType;         // Loại phòng tắm
    private String viewType;             // Loại view
    private String cancellationPolicy;   // Chính sách hủy
    private String breakfastIncluded;    // Bữa sáng có bao gồm
}
```

### 3. GuestResponse
```java
public class GuestResponse {
    private String firstName;            // Họ
    private String lastName;             // Tên
    private String fullName;             // Họ và tên đầy đủ
    private String phone;                // Số điện thoại
    private String email;                // Email
    private String nationality;          // Quốc tịch
    private String address;              // Địa chỉ
    private String dateOfBirth;          // Ngày sinh
    private String gender;               // Giới tính
    private String specialRequests;      // Yêu cầu đặc biệt
    private String loyaltyLevel;         // Cấp độ thành viên
    private String preferredLanguage;    // Ngôn ngữ ưa thích
    private String dietaryRestrictions;  // Hạn chế ăn uống
}
```

## Feign Client Mở Rộng

### RoomFeign Interface
```java
@FeignClient(name = "room-service", url = "localhost:8087/api")
public interface RoomFeign {
    
    // Room Management
    @GetMapping("/rooms")
    ResponseEntity<List<RoomResponse>> getAllRooms();
    
    @GetMapping("/rooms/{roomNumber}")
    ResponseEntity<RoomResponse> getRoomByNumber(@PathVariable String roomNumber);
    
    // Room Type Management
    @GetMapping("/rooms/types")
    ResponseEntity<List<RoomTypeResponse>> getAllRoomTypes();
    
    // Furniture Management
    @GetMapping("/furniture")
    ResponseEntity<List<FurnitureRequirementResponse>> getAllFurniture();
    
    // Maintenance Management
    @GetMapping("/maintenance")
    ResponseEntity<List<RoomMaintenanceResponse>> getAllMaintenance();
    
    // Cleaning Management
    @GetMapping("/cleaning")
    ResponseEntity<List<RoomCleaningResponse>> getAllCleaning();
    
    // Service Management
    @GetMapping("/services")
    ResponseEntity<List<RoomServiceResponse>> getAllServices();
    
    // Guest Management
    @GetMapping("/guests")
    ResponseEntity<List<GuestResponse>> getAllGuests(@RequestParam int page, @RequestParam int size);
    
    // Statistics
    @GetMapping("/rooms/statistics")
    ResponseEntity<Object> getRoomStatistics();
}
```

## Tích Hợp AI Model

### DataIngestionServiceImpl
```java
@Component
public class DataIngestionServiceImpl implements DataIngestionService {
    
    @Override
    public void ingestHotelData() {
        // 1. Nạp thông tin phòng
        ingestRoomData(allDocuments);
        
        // 2. Nạp thông tin loại phòng
        ingestRoomTypeData(allDocuments);
        
        // 3. Nạp thông tin nội thất
        ingestFurnitureData(allDocuments);
        
        // 4. Nạp thông tin bảo trì
        ingestMaintenanceData(allDocuments);
        
        // 5. Nạp thông tin dọn dẹp
        ingestCleaningData(allDocuments);
        
        // 6. Nạp thông tin dịch vụ phòng
        ingestServiceData(allDocuments);
        
        // 7. Nạp thông tin khách hàng
        ingestGuestData(allDocuments);
        
        // 8. Nạp thống kê tổng quan
        ingestStatisticsData(allDocuments);
    }
}
```

## Định Dạng Nội Dung AI

### Thông Tin Phòng
```
THÔNG TIN PHÒNG KHÁCH SẠN 5 SAO VIỆT NAM
Số phòng: 101
Tầng: 1
Diện tích: 25m²
Loại phòng: Standard
Trạng thái: VACANT
Đặc điểm: View sông, ban công riêng
View: View sông Sài Gòn
Tiện nghi đặc biệt: Không có
Lần dọn dẹp cuối: 2024-01-15
Lần bảo trì cuối: 2024-01-10
```

### Thông Tin Loại Phòng
```
LOẠI PHÒNG KHÁCH SẠN 5 SAO VIỆT NAM
Tên loại: Standard
Mô tả: Phòng tiêu chuẩn với đầy đủ tiện nghi cơ bản
Giá cơ bản: 1200000 VND/đêm
Sức chứa tối đa: 2 người
Diện tích: 25m²
Cấu hình giường: 1 giường Queen
Loại phòng tắm: Phòng tắm riêng biệt
Loại view: View sông
Tiện nghi: TV, minibar, wifi, điều hòa
Chính sách hủy: Hủy miễn phí trước 24h
Bữa sáng: Có
```

### Thông Tin Khách Hàng
```
THÔNG TIN KHÁCH HÀNG KHÁCH SẠN 5 SAO VIỆT NAM
Họ và tên: Nguyễn Văn An
Tên đầy đủ: Nguyễn Văn An
Số điện thoại: 0901234567
Email: nguyenvanan@email.com
Quốc tịch: Việt Nam
Địa chỉ: 123 Đường ABC, Quận 1, TP.HCM
Ngày sinh: 1990-05-15
Giới tính: Nam
Yêu cầu đặc biệt: Phòng có view sông
Cấp độ thành viên: Silver
Ngôn ngữ ưa thích: Tiếng Việt
Hạn chế ăn uống: Không có
```

## Cách Sử Dụng

### 1. Khởi Động Services
```bash
# Khởi động Room Management Service
cd room-management
mvn spring-boot:run

# Khởi động AI Management Service
cd spring-ai-management
mvn spring-boot:run
```

### 2. Test API với Postman
- Import file: `Room_Management_API_Postman_Collection_Updated.json`
- Cập nhật `base_url` trong environment variables
- Test các endpoint theo thứ tự

### 3. Kiểm Tra AI Integration
```bash
# Kiểm tra logs của AI service
tail -f spring-ai-management/logs/ai-service.log

# Kiểm tra dữ liệu trong Vector Store
curl http://localhost:8088/api/ai/health
```

## Lưu Ý Quan Trọng

### 🔒 Bảo Mật
- Không bao giờ expose ID trong response
- Sử dụng authentication cho các API nhạy cảm
- Log tất cả truy cập vào hệ thống

### 📊 Dữ Liệu
- Dữ liệu được cập nhật mỗi giờ
- Backup dữ liệu hàng ngày
- Monitoring performance của AI model

### 🚀 Performance
- Sử dụng caching cho dữ liệu thường xuyên truy cập
- Optimize vector search với proper indexing
- Monitor memory usage của AI service

## Troubleshooting

### Lỗi Thường Gặp

1. **Feign Client Connection Error**
   - Kiểm tra port của room-management service
   - Đảm bảo service đang chạy
   - Kiểm tra network configuration

2. **Vector Store Error**
   - Kiểm tra database connection
   - Verify embedding model configuration
   - Check memory usage

3. **Data Ingestion Error**
   - Kiểm tra logs của DataIngestionService
   - Verify DTO mapping
   - Check API response format

### Giải Pháp

```bash
# Restart services
mvn spring-boot:run

# Clear cache
redis-cli flushall

# Check health
curl http://localhost:8087/actuator/health
curl http://localhost:8088/actuator/health
```

## Kết Luận

Hệ thống đã được tích hợp hoàn chỉnh với AI model, cung cấp thông tin thông minh về khách sạn 5 sao Việt Nam một cách an toàn và hiệu quả. Tất cả dữ liệu được xử lý theo chuẩn bảo mật cao, không tiết lộ thông tin nhạy cảm.
