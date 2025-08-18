# QR Code Service Đơn Giản - Hướng dẫn sử dụng

## Tổng quan
Service đơn giản để tạo QR code với `bookingId` và gửi email xác nhận đặt phòng.

## Tính năng chính

### 1. Tạo QR Code
- ✅ Tạo QR code với chỉ `bookingId` (đơn giản như yêu cầu)
- ✅ Sử dụng thư viện ZXing để tạo QR code chất lượng cao
- ✅ Lưu vào database và file system tự động

### 2. Quét QR Code
- ✅ **Method duy nhất**: `scanQrCodeAndMarkAsScanned(bookingId)`
- ✅ **Tự động đánh dấu**: QR code đã được quét
- ✅ **Trả về đầy đủ**: Thông tin booking, customer, room
- ✅ **Chống quét trùng**: Kiểm tra QR code đã quét chưa

### 2. Gửi Email
- ✅ Template email HTML đẹp, chuyên nghiệp
- ✅ Chứa QR code để user có thể quét
- ✅ Thông tin đầy đủ về đặt phòng

### 3. Đơn giản, dễ sử dụng
- ✅ Chỉ cần gọi 1 method duy nhất
- ✅ Không có tham số phức tạp
- ✅ Error handling đơn giản

## Cách sử dụng

### 1. Tạo QR code và gửi email
```java
@Autowired
private BookingQrCodeService bookingQrCodeService;

// Tạo QR code và gửi email
bookingQrCodeService.createQrCodeAndSendEmail(
    "BOOKING123",           // bookingId
    "user@example.com"      // userEmail
);
```

### 2. Quét QR code và đánh dấu đã quét
```java
@Autowired
private QrCodeService qrCodeService;

// Quét QR code từ frontend (bookingId từ scanner)
QrCodeScanResponse response = qrCodeService.scanQrCodeAndMarkAsScanned("BOOKING123");

// Response chứa thông tin đầy đủ về booking và customer
System.out.println("Customer: " + response.getCustomerName());
System.out.println("Room: " + response.getRoomNumber());
System.out.println("Status: " + response.getQrCodeStatus());
```

### 2. Sử dụng trong Command
```java
@Component
public class SendConfirmEmailCommandImpl implements SendBookingConfirmCommand {
    
    private final BookingQrCodeService bookingQrCodeService;
    
    @Override
    public void sendEmailConfirm(Notification message) {
        String bookingId = message.getMessage();
        String userEmail = message.getUserId();
        String userName = "Khách hàng";
        
        // Tạo QR code và gửi email
        bookingQrCodeService.createQrCodeAndSendEmail(bookingId, userEmail, userName);
    }
}
```

## Cấu trúc dự án

### Service chính
- `BookingQrCodeService.java` - Service đơn giản để tạo QR code và gửi email
- `QrCodeService.java` - Service chính để tạo và quét QR code

### DTO
- `QrCodeScanResponse.java` - Response khi quét QR code (chứa thông tin booking, customer)

### Template email
- `booking-qr-code.html` - Template Thymeleaf HTML đẹp

### Dependencies
- `QrCodeService` - Tạo và quét QR code
- `EmailService` - Gửi email

## Template Email

Email sẽ có:
- 🏨 Header đẹp với tên khách sạn
- 📋 Thông tin đặt phòng
- 📱 QR code để quét check-in
- 📞 Thông tin liên hệ
- ⚠️ Lưu ý quan trọng
- 🎯 Footer chuyên nghiệp

## Lưu ý quan trọng

1. **QR Code chỉ chứa `bookingId`** - Đúng như yêu cầu frontend
2. **Service đơn giản** - Không phức tạp, dễ maintain
3. **Template email đẹp** - Thể hiện sự chuyên nghiệp
4. **Error handling** - Fallback về email text nếu HTML lỗi

## Ví dụ kết quả

### QR Code
- Chứa: `BOOKING123`
- Format: PNG
- Kích thước: 300x300 pixels

### Email
- Subject: "Xác nhận đặt phòng - QR Code"
- Nội dung: HTML đẹp với QR code
- Fallback: Text đơn giản nếu HTML lỗi

## Troubleshooting

### Lỗi tạo QR code
- Kiểm tra `bookingId` không null
- Kiểm tra quyền ghi file

### Lỗi gửi email
- Kiểm tra cấu hình SMTP
- Kiểm tra template Thymeleaf
- Fallback về email text

## Kết luận

Service này đáp ứng đúng yêu cầu:
- ✅ Đơn giản, dễ sử dụng
- ✅ Chỉ có tham số `bookingId`
- ✅ Template email đẹp, chuyên nghiệp
- ✅ Không phức tạp, dễ maintain
