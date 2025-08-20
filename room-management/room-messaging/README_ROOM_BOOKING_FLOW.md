# Room Booking Flow - Xử Lý Đặt Cọc Phòng

## Tổng Quan
Flow này xử lý việc nhận thông báo đặt cọc từ Booking Service và cập nhật trạng thái phòng tương ứng.

## Luồng Xử Lý

### 1. Nhận Message từ Kafka
- **Listener**: `RoomBookedRequestKafkaListener`
- **Topic**: Nhận message từ topic chứa thông tin đặt cọc
- **Message Type**: `BookingRoomRequestAvro`

### 2. Chuyển Đổi Dữ Liệu
- **Mapper**: `RoomKafkaDataMapper.toBookingRoomRequestMessage()`
- **Chuyển đổi**: Từ Avro model sang domain message
- **Dữ liệu**: ID, SagaId, BookingId, Status, Rooms, Price, etc.

### 3. Xử Lý Nghiệp Vụ
- **Command**: `RoomBookingCommand.process()`
- **Logic chính**:
  - Kiểm tra trạng thái `DEPOSITED` (đã đặt cọc)
  - Validate danh sách phòng
  - Cập nhật trạng thái từng phòng thành `BOOKED`

### 4. Cập Nhật Trạng Thái Phòng
- **Event Service**: `RoomEventService.bookedRoom()`
- **Trạng thái**: Chuyển từ `VACANT` → `BOOKED`
- **Repository**: `RoomRepository.update()` để lưu vào database

### 5. Gửi Response
- **Response Message**: `BookingRoomResponseMessage`
- **Publisher**: `RoomBookedResponseKafkaPublisher`
- **Topic**: `room-approval-response`
- **Nội dung**: Thông báo hoàn thành cập nhật tất cả phòng

## Cấu Trúc Dữ Liệu

### Input (BookingRoomRequestAvro)
```json
{
  "id": "UUID",
  "sagaId": "UUID", 
  "bookingId": "String",
  "bookingStatus": "DEPOSITED",
  "rooms": [
    {
      "id": "String",
      "roomNumber": "String",
      "basePrice": "String",
      "status": "String"
    }
  ],
  "price": "BigDecimal",
  "type": "String",
  "sagaStatus": "String"
}
```

### Output (BookingRoomResponseMessage)
```json
{
  "id": "UUID",
  "sagaId": "String",
  "bookingId": "String",
  "type": "String",
  "reservationStatus": "SUCCESS",
  "rooms": "List<Room>",
  "totalPrice": "BigDecimal",
  "reason": "All rooms have been successfully booked and updated"
}
```

## Xử Lý Lỗi

### 1. Validation Errors
- Kiểm tra danh sách phòng không rỗng
- Validate trạng thái phòng trước khi đặt

### 2. Business Logic Errors
- Phòng không ở trạng thái `VACANT`
- Phòng đang trong bảo trì hoặc đã được đặt

### 3. System Errors
- Lỗi database khi update
- Lỗi gửi message response
- Log chi tiết để debug

## Logging & Monitoring

### Log Levels
- **INFO**: Thông tin xử lý thành công
- **WARN**: Trạng thái không được hỗ trợ
- **ERROR**: Lỗi xử lý nghiệp vụ

### Metrics
- Số lượng message xử lý
- Thời gian xử lý mỗi message
- Tỷ lệ thành công/thất bại

## Cấu Hình

### Kafka Topics
- **Input**: Topic nhận thông báo đặt cọc
- **Output**: `room-approval-response`

### Retry Policy
- Có thể cấu hình retry cho message thất bại
- Dead letter queue cho message không thể xử lý

## Testing

### Unit Tests
- Test các method trong `RoomBookingCommand`
- Test mapping logic trong `RoomKafkaDataMapper`

### Integration Tests
- Test flow hoàn chỉnh từ Kafka đến Database
- Test các trường hợp lỗi khác nhau

## Deployment

### Requirements
- Spring Boot application
- Kafka cluster
- Database (PostgreSQL)
- Proper logging configuration

### Health Checks
- Kafka connectivity
- Database connectivity
- Message processing status
