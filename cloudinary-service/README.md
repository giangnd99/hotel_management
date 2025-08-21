# Cloudinary Service

Service quản lý upload ảnh sử dụng Cloudinary theo kiến trúc 3 layer pattern.

## Cấu trúc dự án

```
cloudinary-service/
├── src/main/java/com/poly/cloudinary/service/
│   ├── controller/          # Layer 1: Controller
│   │   └── ImageUploadController.java
│   ├── service/             # Layer 2: Service
│   │   ├── UploadImageFileService.java
│   │   └── UploadImageFileServiceImpl.java
│   ├── dto/                 # Data Transfer Objects
│   │   └── UploadResponseDto.java
│   ├── config/              # Configuration
│   │   └── CloudinaryConfig.java
│   ├── exception/           # Exception Handling
│   │   └── GlobalExceptionHandler.java
│   └── CloudinaryApplication.java
└── src/main/resources/
    └── application.properties
```

## Cài đặt và cấu hình

### 1. Cấu hình Cloudinary
Cập nhật file `application.properties` với thông tin Cloudinary của bạn:

```properties
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret
```

### 2. Chạy service
```bash
mvn spring-boot:run
```

Service sẽ chạy trên port 8085.

## API Endpoints

### Upload ảnh
- **URL**: `POST /api/v1/images/upload`
- **Content-Type**: `multipart/form-data`
- **Parameter**: `file` (MultipartFile)
- **Response**: JSON với cấu trúc:

```json
{
    "success": true,
    "imageUrl": "https://res.cloudinary.com/your_cloud/image/upload/...",
    "message": "Upload ảnh thành công"
}
```

### Health Check
- **URL**: `GET /api/v1/images/health`
- **Response**: `"Cloudinary Service is running!"`

## Tính năng

- ✅ Upload ảnh lên Cloudinary
- ✅ Xử lý lỗi và validation
- ✅ Giới hạn kích thước file (10MB)
- ✅ Tự động xóa file tạm sau khi upload
- ✅ Logging chi tiết
- ✅ Cross-origin support
- ✅ Exception handling toàn cục

## Sử dụng với Postman

1. Tạo request POST đến `http://localhost:8085/api/v1/images/upload`
2. Chọn tab `Body` → `form-data`
3. Thêm key `file` với type `File`
4. Chọn file ảnh cần upload
5. Gửi request

## Lưu ý

- File sẽ được upload lên Cloudinary với tên duy nhất (UUID + tên gốc)
- File tạm sẽ được tự động xóa sau khi upload thành công
- Service hỗ trợ các định dạng ảnh phổ biến (JPG, PNG, GIF, etc.)
