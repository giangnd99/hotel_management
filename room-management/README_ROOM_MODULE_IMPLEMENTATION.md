# Room Management Module Implementation

## Tổng quan
Module Room Management đã được triển khai với đầy đủ các layer theo Clean Architecture pattern, bao gồm:
- Domain Layer (Entities, Services, Ports)
- Application Layer (DTOs, Mappers, Services)
- Data Access Layer (JPA Repositories, Adapters, Mappers)
- Infrastructure Layer (Kafka Publishers)

## Các thành phần đã triển khai

### 1. Domain Layer
- **Entities**: `Room`, `RoomType`, `RoomMaintenance`, `MaintenanceType`
- **Services**: `RoomDomainService`, `RoomCommandService`
- **Ports**: 
  - In: `RoomService`
  - Out: `RoomRepository`, `RoomTypeRepository`, `RoomMaintenanceRepository`, `MaintenanceTypeRepository`, `RoomCheckOutRequestPublisher`

### 2. Application Layer
- **DTOs**: `RoomResponse`, `RoomStatisticsDto`, `RoomStatusDto`, `RoomTypeDto`, `CreateRoomRequest`, `UpdateRoomRequest`
- **Mappers**: `RoomDtoMapper`
- **Services**: `RoomServiceImpl` (đầy đủ implementation)

### 3. Data Access Layer
- **JPA Repositories**: 
  - `RoomJpaRepository` - với custom queries cho search, filter, pagination
  - `RoomMaintenanceJpaRepository` - với queries cho maintenance management
  - `MaintenanceTypeJpaRepository` - với findByName method
- **Adapters**: 
  - `RoomRepositoryImpl` - implementation đầy đủ cho RoomRepository
  - `RoomMaintenanceRepositoryImpl` - implementation đầy đủ cho RoomMaintenanceRepository
  - `MaintenanceTypeRepositoryImpl` - implementation đầy đủ cho MaintenanceTypeRepository
- **Mappers**: 
  - `RoomMapper` - chuyển đổi giữa Room domain và RoomEntity
  - `RoomMaintenanceMapper` - chuyển đổi giữa RoomMaintenance domain và RoomMaintenanceEntity
  - `MaintenanceTypeMapper` - chuyển đổi giữa MaintenanceType domain và MaintenanceTypeEntity

### 4. Infrastructure Layer
- **Kafka Publishers**: `RoomCheckOutKafkaPublisher` - gửi room checkout messages
- **Mappers**: `RoomKafkaDataMapper` - chuyển đổi domain messages sang Avro models

## Các tính năng đã triển khai

### Dashboard & Statistics
- Thống kê tổng quan về phòng (tổng số, có sẵn, đang có khách, bảo trì, dọn dẹp, tỷ lệ lấp đầy)
- Đếm số phòng theo trạng thái

### CRUD Operations
- Tạo, đọc, cập nhật, xóa phòng
- Quản lý loại phòng
- Quản lý bảo trì phòng

### Search & Filter
- Tìm kiếm phòng với nhiều tiêu chí (số phòng, loại, trạng thái, tầng, giá)
- Lọc phòng theo trạng thái, loại, tầng, khoảng giá
- Phân trang cho tất cả các query

### Status Management
- Cập nhật trạng thái phòng (VACANT, BOOKED, CHECKED_IN, MAINTENANCE, CLEANING)
- Các method tiện ích để set trạng thái cụ thể

### Maintenance Management
- Lên lịch bảo trì phòng
- Tìm kiếm bảo trì theo loại, trạng thái, ưu tiên
- Quản lý bảo trì theo phòng, nhân viên, ngày tháng

### Availability & Pricing
- Kiểm tra tính khả dụng của phòng
- Lấy khoảng giá phòng
- Quản lý theo tầng

## Các pattern đã áp dụng

### 1. Clean Architecture
- **Domain Layer**: Chứa business logic và entities
- **Application Layer**: Chứa use cases và orchestration
- **Data Access Layer**: Chứa data persistence logic
- **Infrastructure Layer**: Chứa external integrations

### 2. Port-Adapter Pattern
- **Ports**: Định nghĩa contracts (interfaces)
- **Adapters**: Implementation cụ thể của ports

### 3. Repository Pattern
- **Repository Interface**: Định nghĩa data access methods
- **Repository Implementation**: JPA implementation với custom queries

### 4. Mapper Pattern
- **Domain Mappers**: Chuyển đổi giữa domain entities và JPA entities
- **DTO Mappers**: Chuyển đổi giữa domain entities và DTOs
- **Kafka Mappers**: Chuyển đổi giữa domain messages và Avro models

### 5. Builder Pattern
- Sử dụng cho các entities phức tạp như `RoomMaintenance`

## Các file đã tạo

### Domain Layer
- `RoomService.java` - Interface cho room management
- `RoomServiceImpl.java` - Implementation đầy đủ
- `RoomRepository.java` - Interface cho room data access
- `RoomMaintenanceRepository.java` - Interface cho maintenance data access
- `MaintenanceTypeRepository.java` - Interface cho maintenance type data access

### Data Access Layer
- `RoomJpaRepository.java` - JPA repository với custom queries
- `RoomMaintenanceJpaRepository.java` - JPA repository cho maintenance
- `MaintenanceTypeJpaRepository.java` - JPA repository cho maintenance type
- `RoomRepositoryImpl.java` - Adapter implementation cho RoomRepository
- `RoomMaintenanceRepositoryImpl.java` - Adapter implementation cho RoomMaintenanceRepository
- `MaintenanceTypeRepositoryImpl.java` - Adapter implementation cho MaintenanceTypeRepository
- `RoomMapper.java` - Mapper cho Room entity
- `RoomMaintenanceMapper.java` - Mapper cho RoomMaintenance entity
- `MaintenanceTypeMapper.java` - Mapper cho MaintenanceType entity

### Infrastructure Layer
- `RoomCheckOutKafkaPublisher.java` - Kafka publisher cho room checkout
- `RoomKafkaDataMapper.java` - Mapper cho Kafka messages

### Testing
- `Room_Management_API_Postman_Collection.json` - Postman collection để test API

## Những gì cần làm tiếp theo

### 1. Domain Entities
- Tạo `RoomCleaning` domain entity
- Tạo `CleaningId` value object
- Hoàn thiện `RoomMaintenance` domain entity

### 2. Room Cleaning Management
- Tạo `RoomCleaningRepository` interface và implementation
- Tạo `RoomCleaningEntity` và `RoomCleaningJpaRepository`
- Tạo `RoomCleaningMapper`
- Implement các method liên quan đến cleaning trong `RoomServiceImpl`

### 3. Room Pricing Management
- Tạo `RoomPricing` entity và repository
- Implement method `updateRoomPrice` trong `RoomServiceImpl`

### 4. Testing
- Unit tests cho tất cả các services
- Integration tests với database
- API tests với Postman collection

### 5. Documentation
- API documentation với Swagger
- Database schema documentation
- Deployment guide

### 6. Performance & Security
- Caching cho các query thường xuyên
- Pagination optimization
- Security validation
- Error handling improvement

## Cách sử dụng

### 1. Import Postman Collection
- Mở Postman
- Import file `Room_Management_API_Postman_Collection.json`
- Cấu hình biến môi trường:
  - `base_url`: URL cơ sở của API (mặc định: http://localhost:8080)
  - `room_id`: ID phòng để test (mặc định: 1)

### 2. Test các API
- **Dashboard & Statistics**: Lấy thống kê tổng quan
- **CRUD Operations**: Quản lý phòng cơ bản
- **Search & Filter**: Tìm kiếm và lọc phòng
- **Status Management**: Quản lý trạng thái phòng
- **Maintenance Management**: Quản lý bảo trì

### 3. Database Setup
- Tạo database schema theo các entity đã định nghĩa
- Insert sample data cho testing
- Cấu hình connection properties

## Kết luận
Module Room Management đã được triển khai với kiến trúc rõ ràng, tuân thủ các best practices và design patterns. Các tính năng cơ bản đã hoàn thiện, sẵn sàng cho việc testing và development tiếp theo.
