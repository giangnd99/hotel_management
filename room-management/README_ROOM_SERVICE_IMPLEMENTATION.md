# Room Service Implementation

## Tổng quan

Dự án đã triển khai đầy đủ `RoomServiceImpl` và các repository cần thiết để quản lý phòng trong hệ thống khách sạn.

## Các thành phần đã triển khai

### 1. RoomService Interface
- **File**: `room-management/room-domain/room-application-service/src/main/java/com/poly/room/management/domain/port/in/service/RoomService.java`
- **Chức năng**: Định nghĩa tất cả các method cần thiết cho việc quản lý phòng

### 2. RoomServiceImpl
- **File**: `room-management/room-domain/room-application-service/src/main/java/com/poly/room/management/domain/service/impl/RoomServiceImpl.java`
- **Chức năng**: Triển khai đầy đủ tất cả các method từ RoomService interface

#### Các nhóm chức năng chính:

##### Dashboard & Statistics
- `getRoomStatistics()` - Lấy thống kê tổng quan về phòng
- `getRoomCount()` - Đếm tổng số phòng
- `getAvailableRoomCount()` - Đếm số phòng có sẵn
- `getOccupiedRoomCount()` - Đếm số phòng đang có khách
- `getMaintenanceRoomCount()` - Đếm số phòng đang bảo trì
- `getOccupancyRatio()` - Tính tỷ lệ lấp đầy

##### CRUD Operations
- `getAllRooms(int page, int size)` - Lấy danh sách phòng với phân trang
- `getRoomById(Long roomId)` - Lấy phòng theo ID
- `getRoomByNumber(String roomNumber)` - Lấy phòng theo số phòng
- `createRoom(CreateRoomRequest request)` - Tạo phòng mới
- `updateRoom(Long roomId, UpdateRoomRequest request)` - Cập nhật thông tin phòng
- `deleteRoom(Long roomId)` - Xóa phòng

##### Search & Filter
- `searchRooms(...)` - Tìm kiếm phòng với nhiều tiêu chí
- `filterRoomsByStatus(...)` - Lọc phòng theo trạng thái
- `filterRoomsByType(...)` - Lọc phòng theo loại
- `filterRoomsByFloor(...)` - Lọc phòng theo tầng
- `filterRoomsByPriceRange(...)` - Lọc phòng theo khoảng giá

##### Status Management
- `updateRoomStatus(Long roomId, String status)` - Cập nhật trạng thái phòng
- `setRoomAvailable(Long roomId)` - Đặt phòng thành có sẵn
- `setRoomOccupied(Long roomId)` - Đặt phòng thành đang có khách
- `setRoomMaintenance(Long roomId)` - Đặt phòng thành đang bảo trì
- `setRoomCleaning(Long roomId)` - Đặt phòng thành đang dọn dẹp

##### Room Type Management
- `getAllRoomTypes()` - Lấy tất cả loại phòng
- `getRoomTypeById(Long typeId)` - Lấy loại phòng theo ID
- `createRoomType(RoomTypeDto request)` - Tạo loại phòng mới
- `updateRoomType(Long typeId, RoomTypeDto request)` - Cập nhật loại phòng
- `deleteRoomType(Long typeId)` - Xóa loại phòng

##### Availability Check
- `getAvailableRooms(...)` - Lấy danh sách phòng có sẵn với filter
- `checkRoomAvailability(Long roomId)` - Kiểm tra phòng có sẵn không

##### Maintenance Management
- `getMaintenanceRooms(int page, int size)` - Lấy danh sách phòng đang bảo trì
- `scheduleRoomMaintenance(...)` - Lên lịch bảo trì phòng
- `completeRoomMaintenance(Long roomId)` - Hoàn thành bảo trì phòng

##### Cleaning Management
- `getCleaningRooms(int page, int size)` - Lấy danh sách phòng đang dọn dẹp
- `completeRoomCleaning(Long roomId)` - Hoàn thành dọn dẹp phòng

##### Floor Management
- `getAllFloors()` - Lấy tất cả các tầng
- `getRoomsByFloor(Integer floor)` - Lấy phòng theo tầng

##### Pricing Management
- `updateRoomPrice(Long roomId, Double newPrice)` - Cập nhật giá phòng
- `getRoomPriceRange()` - Lấy khoảng giá phòng

### 3. Repository Interfaces

#### RoomRepository
- **File**: `room-management/room-domain/room-application-service/src/main/java/com/poly/room/management/domain/port/out/repository/RoomRepository.java`
- **Chức năng**: Định nghĩa contract cho việc truy xuất dữ liệu phòng

#### RoomMaintenanceRepository
- **File**: `room-management/room-domain/room-application-service/src/main/java/com/poly/room/management/domain/port/out/repository/RoomMaintenanceRepository.java`
- **Chức năng**: Định nghĩa contract cho việc quản lý bảo trì phòng

#### RoomCleaningRepository
- **File**: `room-management/room-domain/room-application-service/src/main/java/com/poly/room/management/domain/port/out/repository/RoomCleaningRepository.java`
- **Chức năng**: Định nghĩa contract cho việc quản lý dọn dẹp phòng

### 4. JPA Repository Implementations

#### RoomJpaRepository
- **File**: `room-management/room-data-access/src/main/java/com/poly/room/management/dao/room/repository/RoomJpaRepository.java`
- **Chức năng**: Triển khai các query JPA cho phòng

#### RoomMaintenanceJpaRepository
- **File**: `room-management/room-data-access/src/main/java/com/poly/room/management/dao/room/repository/RoomMaintenanceJpaRepository.java`
- **Chức năng**: Triển khai các query JPA cho bảo trì phòng

### 5. Repository Adapters

#### RoomRepositoryImpl
- **File**: `room-management/room-data-access/src/main/java/com/poly/room/management/dao/room/adapter/RoomRepositoryImpl.java`
- **Chức năng**: Adapter kết nối domain layer với data access layer

#### RoomMaintenanceRepositoryImpl
- **File**: `room-management/room-data-access/src/main/java/com/poly/room/management/dao/room/adapter/RoomMaintenanceRepositoryImpl.java`
- **Chức năng**: Adapter kết nối domain layer với data access layer cho bảo trì

## Các query JPA đã triển khai

### Room Queries
- Tìm kiếm phòng theo nhiều tiêu chí
- Lọc phòng theo trạng thái, loại, tầng, giá
- Phân trang cho tất cả các query
- Thống kê số lượng phòng theo trạng thái
- Tìm kiếm phòng có sẵn
- Quản lý tầng và giá

### Maintenance Queries
- Tìm kiếm bảo trì theo phòng, trạng thái, loại
- Lọc theo ưu tiên và nhân viên được giao
- Quản lý lịch bảo trì và hoàn thành
- Thống kê bảo trì theo các tiêu chí

## Patterns được áp dụng

1. **Clean Architecture**: Tách biệt rõ ràng các layer
2. **Repository Pattern**: Abstract hóa data access
3. **Adapter Pattern**: Kết nối domain với infrastructure
4. **Strategy Pattern**: Xử lý các loại trạng thái phòng khác nhau
5. **Builder Pattern**: Tạo entity objects
6. **Mapper Pattern**: Chuyển đổi giữa domain và data entities

## Cách sử dụng

### 1. Dependency Injection
```java
@Autowired
private RoomService roomService;
```

### 2. Sử dụng các method
```java
// Lấy thống kê
RoomStatisticsDto stats = roomService.getRoomStatistics();

// Tìm kiếm phòng
List<RoomResponse> rooms = roomService.searchRooms(
    "101", "STANDARD", "VACANT", 1, 50.0, 200.0, 0, 10
);

// Cập nhật trạng thái
RoomResponse updatedRoom = roomService.setRoomOccupied(1L);
```

## Các bước tiếp theo

1. **Triển khai RoomCleaningEntity và Repository**: Hoàn thiện việc quản lý dọn dẹp
2. **Thêm Unit Tests**: Viết test cho tất cả các method
3. **Integration Tests**: Test với database thực
4. **API Documentation**: Hoàn thiện Swagger documentation
5. **Error Handling**: Cải thiện xử lý lỗi và validation
6. **Performance Optimization**: Tối ưu hóa các query phức tạp

## Lưu ý

- Một số method như `createRoomType`, `updateRoomType` cần được implement dựa trên cấu trúc entity thực tế
- Các method liên quan đến maintenance và cleaning cần entity tương ứng
- Cần implement proper error handling và validation
- Cần thêm logging và monitoring cho production use
