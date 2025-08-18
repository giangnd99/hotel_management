# QR Code Service - Hướng dẫn sử dụng

## Tổng quan
QR Code Service cung cấp đầy đủ chức năng để tạo, quản lý và lưu trữ mã QR code trong hệ thống hotel management.

## Tính năng chính

### 1. Tạo QR Code
- Tạo QR code với data tùy chỉnh
- Hỗ trợ tùy chỉnh kích thước (100x100 đến 1000x1000 pixels)
- Hỗ trợ nhiều format: PNG, JPG, JPEG, GIF
- Tự động lưu vào database và file system

### 2. Quản lý QR Code
- Lưu trữ thông tin QR code vào MySQL database
- Hỗ trợ CRUD operations đầy đủ
- Xóa mềm (soft delete) để bảo toàn dữ liệu
- Tìm kiếm và lọc theo nhiều tiêu chí

### 3. Xử lý Image
- Tự động tạo QR code image sử dụng thư viện ZXing
- Lưu image vào file system với tên duy nhất
- Convert image thành base64 để trả về API
- Quản lý file system tự động

## Cấu trúc dự án

### Entity
- `QrCode.java` - Entity chính để lưu trữ thông tin QR code

### Repository
- `QrCodeRepository.java` - Interface JPA với các method tìm kiếm tùy chỉnh

### DTO
- `QrCodeRequest.java` - DTO nhận thông tin tạo/cập nhật QR code
- `QrCodeResponse.java` - DTO trả về thông tin QR code

### Service
- `QrCodeService.java` - Service chính với đầy đủ business logic

### Exception
- `QrCodeException.java` - Custom exception để xử lý lỗi

## Cách sử dụng

### 1. Tạo QR Code đơn giản
```java
@Autowired
private QrCodeService qrCodeService;

// Tạo QR code với chỉ data
QrCodeResponse response = qrCodeService.createSimpleQrCode("https://example.com");
```

### 2. Tạo QR Code với tùy chỉnh
```java
QrCodeRequest request = new QrCodeRequest();
request.setData("https://example.com");
request.setWidth(400);
request.setHeight(400);
request.setFormat("PNG");
request.setDescription("QR code cho website");

QrCodeResponse response = qrCodeService.createQrCode(request);
```

### 3. Lấy QR Code theo ID
```java
QrCodeResponse response = qrCodeService.getQrCodeById(1L);
```

### 4. Lấy QR Code theo data
```java
QrCodeResponse response = qrCodeService.getQrCodeByData("https://example.com");
```

### 5. Cập nhật QR Code
```java
QrCodeRequest updateRequest = new QrCodeRequest();
updateRequest.setWidth(500);
updateRequest.setHeight(500);

QrCodeResponse response = qrCodeService.updateQrCode(1L, updateRequest);
```

### 6. Xóa QR Code
```java
qrCodeService.deleteQrCode(1L);
```

### 7. Tìm kiếm QR Code
```java
// Tìm theo mô tả
List<QrCodeResponse> results = qrCodeService.searchQrCodesByDescription("website");

// Lấy theo khoảng thời gian
LocalDateTime startDate = LocalDateTime.now().minusDays(7);
LocalDateTime endDate = LocalDateTime.now();
List<QrCodeResponse> results = qrCodeService.getQrCodesByDateRange(startDate, endDate);
```

## Cấu hình

### Properties
```yaml
qr:
  code:
    storage:
      path: ./qr-codes  # Đường dẫn lưu trữ QR code images
    image:
      format: PNG       # Format mặc định
```

### Database
- Sử dụng file `schemainit-schema.sql` để tạo bảng `qr_codes`
- Bảng tự động được tạo khi khởi động ứng dụng

## Dependencies

### Maven Dependencies
```xml
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>core</artifactId>
    <version>3.5.2</version>
</dependency>
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>javase</artifactId>
    <version>3.5.2</version>
</dependency>
```

## Lưu ý quan trọng

### 1. Bảo mật
- Data được validate trước khi xử lý
- Hỗ trợ xóa mềm để bảo toàn dữ liệu
- Kiểm tra trùng lặp data

### 2. Performance
- Sử dụng transaction để đảm bảo consistency
- Lazy loading cho image (chỉ load khi cần)
- Index database để tối ưu query

### 3. File Management
- Tự động tạo thư mục lưu trữ
- Tên file unique để tránh conflict
- Tự động dọn dẹp file khi xóa

### 4. Error Handling
- Custom exception với message rõ ràng
- Validation đầy đủ cho input
- Graceful handling cho file operations

## Ví dụ API Response

### Success Response
```json
{
  "id": 1,
  "data": "https://example.com",
  "qrCodeImagePath": "./qr-codes/qr_1234567890.png",
  "width": 300,
  "height": 300,
  "format": "PNG",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "isActive": true,
  "description": "QR code cho website",
  "qrCodeBase64": "iVBORw0KGgoAAAANSUhEUgAA..."
}
```

### Error Response
```json
{
  "error": "Lỗi khi tạo QR code: Data không được để trống",
  "timestamp": "2024-01-15T10:30:00"
}
```

## Troubleshooting

### 1. Lỗi tạo thư mục
- Kiểm tra quyền ghi vào thư mục `qr-codes`
- Đảm bảo đường dẫn trong properties là hợp lệ

### 2. Lỗi database
- Kiểm tra kết nối MySQL
- Chạy script `schemainit-schema.sql` để tạo bảng
- Kiểm tra quyền user database

### 3. Lỗi tạo QR code
- Kiểm tra data input có hợp lệ không
- Kiểm tra kích thước có trong khoảng cho phép không
- Kiểm tra format image có được hỗ trợ không
