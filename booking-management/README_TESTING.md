# Hướng Dẫn Testing Booking Management API

## Tổng Quan
Hệ thống quản lý đặt phòng khách sạn với đầy đủ các API endpoints để quản lý booking, customer, room và các chức năng liên quan.

## Cài Đặt và Chạy

### 1. Khởi động Database
```bash
# Chạy script tạo schema
psql -U your_username -d your_database -f schema.sql

# Chạy script insert data mẫu
psql -U your_username -d your_database -f sample-data.sql
```

### 2. Khởi động Application
```bash
# Từ thư mục gốc
mvn spring-boot:run

# Hoặc từ module booking-container
cd booking-container
mvn spring-boot:run
```

### 3. Kiểm tra ứng dụng
- Swagger UI: http://localhost:8080/swagger-ui.html
- Health Check: http://localhost:8080/actuator/health

## Postman Collection

### Import Collection
1. Mở Postman
2. Click "Import" 
3. Chọn file `Booking_API_Postman_Collection.json`
4. Collection sẽ được import với đầy đủ các API endpoints

### Environment Variables
Collection sử dụng các biến môi trường sau:
- `{{base_url}}`: http://localhost:8080
- `{{booking_id}}`: ID của booking để test
- `{{customer_id}}`: ID của customer để test
- `{{room_id}}`: ID của room để test
- `{{check_in_date}}`: Ngày check-in mẫu
- `{{check_out_date}}`: Ngày check-out mẫu

## Data Mẫu

### Customers (10 records)
- Nguyễn Văn A - nguyenvana@email.com
- Trần Thị B - tranthib@email.com
- Lê Văn C - levanc@email.com
- Phạm Thị D - phamthid@email.com
- Hoàng Văn E - hoangvane@email.com
- Đặng Thị F - dangthif@email.com
- Bùi Thế G - buitheg@email.com
- Vũ Thị H - vuthih@email.com
- Nguyễn Thị I - nguyenthi@email.com
- Trần Văn J - tranvanj@email.com

### Rooms (10 records)
- **101**: Phòng Standard 1 giường đôi - 1,500,000 VND
- **102**: Phòng Standard 2 giường đơn - 1,800,000 VND
- **201**: Phòng Deluxe 1 giường king - 2,500,000 VND
- **202**: Phòng Deluxe 2 giường đôi - 2,800,000 VND
- **301**: Phòng Suite 1 giường king - 3,500,000 VND
- **302**: Phòng Suite 2 giường king - 4,200,000 VND
- **401**: Phòng Presidential - 8,000,000 VND
- **501**: Phòng Family 3 giường - 3,200,000 VND
- **601**: Phòng Honeymoon - 4,500,000 VND
- **701**: Phòng Business - 2,200,000 VND

### Bookings (10 records)
- **Booking 1**: Nguyễn Văn A - 2 phòng - 1-3/12/2024 - CHECKED_OUT
- **Booking 2**: Trần Thị B - 2 phòng - 5-7/12/2024 - CHECKED_IN
- **Booking 3**: Lê Văn C - 2 phòng - 10-12/12/2024 - PAID
- **Booking 4**: Phạm Thị D - 1 phòng - 15-17/12/2024 - APPROVED
- **Booking 5**: Hoàng Văn E - 3 phòng - 20-22/12/2024 - PENDING
- **Booking 6**: Đặng Thị F - 1 phòng - 25-27/12/2024 - PENDING
- **Booking 7**: Bùi Thế G - 2 phòng - 30/12-1/1/2025 - PENDING
- **Booking 8**: Vũ Thị H - 2 phòng - 5-7/1/2025 - PENDING
- **Booking 9**: Nguyễn Thị I - 2 phòng - 10-12/1/2025 - PENDING
- **Booking 10**: Trần Văn J - 2 phòng - 15-17/1/2025 - PENDING

## Testing Scenarios

### 1. Dashboard & Statistics
- **Get Today Booking Statistics**: Lấy thống kê tổng quan
- **Get Today Booking Count**: Đếm số booking hôm nay
- **Get Today Success/Pending/Cancelled Count**: Đếm theo trạng thái

### 2. CRUD Operations
- **Create Booking**: Tạo booking mới
- **Get All Bookings**: Lấy danh sách với phân trang
- **Get Booking By ID**: Lấy chi tiết booking
- **Update Booking**: Cập nhật thông tin
- **Delete Booking**: Xóa booking

### 3. Search & Filter
- **Search Bookings**: Tìm kiếm theo nhiều tiêu chí
- **Filter By Status**: Lọc theo trạng thái
- **Filter By Date Range**: Lọc theo khoảng thời gian

### 4. Customer Specific
- **Get Bookings By Customer ID**: Lấy booking của khách hàng
- **Get Customer Booking History**: Lấy lịch sử booking

### 5. Room Search
- **Search Available Rooms**: Tìm phòng khả dụng
- Hỗ trợ filter theo: ngày, số khách, loại phòng, giá

### 6. Status Management
- **Confirm Booking**: Xác nhận booking
- **Check-in**: Check-in khách
- **Check-out**: Check-out khách
- **Cancel Booking**: Hủy booking

### 7. Payment
- **Get Payment Status**: Lấy trạng thái thanh toán
- **Confirm Payment**: Xác nhận thanh toán

### 8. Notifications
- **Send Confirmation**: Gửi email xác nhận
- **Send Reminder**: Gửi email nhắc nhở

## Test Cases Mẫu

### Test Case 1: Tạo Booking Mới
```bash
POST {{base_url}}/api/bookings
Content-Type: application/json

{
  "customerId": "550e8400-e29b-41d4-a716-446655440001",
  "roomId": "550e8400-e29b-41d4-a716-446655440011",
  "checkInDate": "2024-12-25",
  "checkOutDate": "2024-12-27",
  "numberOfGuests": 2,
  "specialRequests": "Không hút thuốc, view biển"
}
```

### Test Case 2: Tìm Kiếm Phòng
```bash
GET {{base_url}}/api/bookings/rooms/search?checkInDate=2024-12-25&checkOutDate=2024-12-27&numberOfGuests=2&minPrice=1000000&maxPrice=5000000
```

### Test Case 3: Lấy Thống Kê
```bash
GET {{base_url}}/api/bookings/statistics/today
```

## Validation Rules

### Booking Validation
- Check-in date phải trước check-out date
- Số khách không được vượt quá capacity của phòng
- Phòng phải available trong khoảng thời gian đặt

### Customer Validation
- Email phải unique
- Username phải unique
- Thông tin bắt buộc: first_name, last_name, email

### Room Validation
- Room number phải unique
- Price phải > 0
- Status phải hợp lệ: AVAILABLE, OCCUPIED, MAINTENANCE

## Error Handling

### HTTP Status Codes
- **200**: Success
- **201**: Created
- **400**: Bad Request (validation error)
- **404**: Not Found
- **500**: Internal Server Error

### Error Response Format
```json
{
  "timestamp": "2024-12-20T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": ["Check-in date must be before check-out date"]
}
```

## Performance Testing

### Load Test
- Sử dụng Apache JMeter hoặc Postman Collection Runner
- Test với 100 concurrent users
- Response time < 2 seconds

### Database Performance
- Index trên các trường thường query: customer_id, status, check_in, check_out
- Sử dụng connection pooling
- Monitor slow queries

## Monitoring & Logging

### Logs
- Application logs: `logs/booking-management.log`
- Access logs: `logs/access.log`
- Error logs: `logs/error.log`

### Metrics
- Prometheus metrics endpoint: `/actuator/prometheus`
- Health check: `/actuator/health`
- Info: `/actuator/info`

## Troubleshooting

### Common Issues
1. **Database Connection Failed**
   - Kiểm tra database service
   - Kiểm tra connection string
   - Kiểm tra firewall

2. **Port Already in Use**
   - Thay đổi port trong application.properties
   - Kill process đang sử dụng port

3. **Data Not Found**
   - Kiểm tra data mẫu đã được insert
   - Kiểm tra UUID format
   - Kiểm tra foreign key constraints

### Debug Mode
```bash
# Enable debug logging
logging.level.com.poly.booking=DEBUG
logging.level.org.springframework.web=DEBUG
```

## Support

Nếu gặp vấn đề, vui lòng:
1. Kiểm tra logs
2. Kiểm tra database connection
3. Kiểm tra network connectivity
4. Liên hệ development team

---

**Lưu ý**: Đây là hệ thống demo, không sử dụng cho production. Tất cả data mẫu sẽ bị xóa khi restart application.
