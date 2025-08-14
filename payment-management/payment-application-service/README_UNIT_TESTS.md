# Unit Tests cho Payment Application Service

## Tổng quan

Bộ unit test này được tạo để kiểm tra logic của 6 use case implementations trong package `com.poly.paymentapplicationservice.service.ok`.

## Các Use Case được test

1. **AddRestaurantChargeUseCaseImpl** - Thêm phí nhà hàng vào hóa đơn
2. **AddServiceChargeUseCaseImpl** - Thêm phí dịch vụ vào hóa đơn  
3. **CreateFinalInvoiceUseCaseImpl** - Tạo hóa đơn cuối kỳ
4. **DepositPaymentLinkUseCaseImpl** - Tạo link thanh toán đặt cọc
5. **InvoicePaymentLinkUseCaseImpl** - Tạo link thanh toán hóa đơn
6. **ProcessDirectPaymentUseCaseImpl** - Xử lý thanh toán trực tiếp

## Cách chạy test

### Chạy tất cả test
```bash
mvn test -Dtest=AllUseCaseTests
```

### Chạy từng use case riêng lẻ
```bash
# Test AddRestaurantChargeUseCaseImpl
mvn test -Dtest=AddRestaurantChargeUseCaseImplTest

# Test AddServiceChargeUseCaseImpl  
mvn test -Dtest=AddServiceChargeUseCaseImplTest

# Test CreateFinalInvoiceUseCaseImpl
mvn test -Dtest=CreateFinalInvoiceUseCaseImplTest

# Test DepositPaymentLinkUseCaseImpl
mvn test -Dtest=DepositPaymentLinkUseCaseImplTest

# Test InvoicePaymentLinkUseCaseImpl
mvn test -Dtest=InvoicePaymentLinkUseCaseImplTest

# Test ProcessDirectPaymentUseCaseImpl
mvn test -Dtest=ProcessDirectPaymentUseCaseImplTest
```

### Chạy test với IDE
- Mở project trong IntelliJ IDEA hoặc Eclipse
- Chuột phải vào thư mục `test` → Run Tests
- Hoặc chạy từng test class riêng lẻ

## Các vấn đề logic đã phát hiện

### 1. AddRestaurantChargeUseCaseImpl
**Vấn đề**: Sử dụng `cmd.getServiceChargeId()` cho `RestaurantId`
- **File**: `AddRestaurantChargeUseCaseImpl.java` dòng 42
- **Mô tả**: Field name có thể gây nhầm lẫn, nên đổi thành `getRestaurantId()`
- **Ảnh hưởng**: Có thể gây nhầm lẫn khi sử dụng

### 2. AddServiceChargeUseCaseImpl  
**Vấn đề**: Tương tự AddRestaurantChargeUseCaseImpl
- **File**: `AddServiceChargeUseCaseImpl.java` dòng 42
- **Mô tả**: Field name có thể gây nhầm lẫn
- **Ảnh hưởng**: Có thể gây nhầm lẫn khi sử dụng

### 3. CreateFinalInvoiceUseCaseImpl
**Vấn đề nghiêm trọng**: Logic tạo InvoiceBooking có vấn đề
- **File**: `CreateFinalInvoiceUseCaseImpl.java` dòng 67-68
- **Mô tả**: Gán `invoiceId` 2 lần:
  ```java
  .invoiceId(exitedInvoiceBooking.get().getInvoiceId())  // Dòng 67
  .invoiceId(invoice.getId())                           // Dòng 68
  ```
- **Ảnh hưởng**: Dòng 68 sẽ ghi đè dòng 67, có thể gây mất dữ liệu

### 4. InvoicePaymentLinkUseCaseImpl
**Vấn đề nghiêm trọng**: Có thể gây NullPointerException
- **File**: `InvoicePaymentLinkUseCaseImpl.java` dòng 58-60
- **Mô tả**: Không kiểm tra null trước khi gọi `.get()` trên Optional:
  ```java
  Optional<InvoicePayment> invoicePayment = invoicePaymentRepository.findByInvoiceId(command.getInvoiceId());
  Optional<InvoiceBooking> invoiceBooking = invoiceBookingRepository.findByInvoiceId(invoicePayment.get().getPaymentId().getValue());
  Optional<Payment> payment = paymentRepository.findById(invoicePayment.get().getPaymentId().getValue());
  ```
- **Ảnh hưởng**: Nếu `invoicePayment` là empty, sẽ gây NullPointerException

### 5. ProcessDirectPaymentUseCaseImpl
**Vấn đề nghiêm trọng**: Logic tính toán thuế sai
- **File**: `ProcessDirectPaymentUseCaseImpl.java` dòng 125
- **Mô tả**: Hàm `calculateTotalAmount()` nhân thay vì cộng thuế:
  ```java
  private BigDecimal calculateTotalAmount(BigDecimal amount1, BigDecimal amount2) {
      return amount1.multiply(amount2);  // Sai: nhân thay vì cộng
  }
  ```
- **Ảnh hưởng**: Tính sai tổng tiền, có thể gây thiệt hại tài chính
- **Cách sửa**: 
  ```java
  private BigDecimal calculateTotalAmount(BigDecimal subTotal, BigDecimal taxRate) {
      return subTotal.add(subTotal.multiply(taxRate));  // Đúng: cộng thuế
  }
  ```

### 6. DepositPaymentLinkUseCaseImpl
**Vấn đề**: Không có vấn đề logic nghiêm trọng
- **Mô tả**: Logic xử lý các trạng thái payment khác nhau đã đúng
- **Ảnh hưởng**: Không có

## Cách sửa các vấn đề

### Sửa CreateFinalInvoiceUseCaseImpl
```java
// Thay vì:
.invoiceId(exitedInvoiceBooking.get().getInvoiceId())
.invoiceId(invoice.getId())

// Sửa thành:
.invoiceId(invoice.getId())
```

### Sửa InvoicePaymentLinkUseCaseImpl
```java
// Thay vì:
Optional<InvoicePayment> invoicePayment = invoicePaymentRepository.findByInvoiceId(command.getInvoiceId());
Optional<InvoiceBooking> invoiceBooking = invoiceBookingRepository.findByInvoiceId(invoicePayment.get().getPaymentId().getValue());

// Sửa thành:
Optional<InvoicePayment> invoicePayment = invoicePaymentRepository.findByInvoiceId(command.getInvoiceId());
if (invoicePayment.isEmpty()) {
    throw new ApplicationServiceException("Invoice payment not found");
}

Optional<InvoiceBooking> invoiceBooking = invoiceBookingRepository.findByInvoiceId(invoicePayment.get().getPaymentId().getValue());
if (invoiceBooking.isEmpty()) {
    throw new ApplicationServiceException("Invoice booking not found");
}
```

### Sửa ProcessDirectPaymentUseCaseImpl
```java
// Thay vì:
private BigDecimal calculateTotalAmount(BigDecimal amount1, BigDecimal amount2) {
    return amount1.multiply(amount2);
}

// Sửa thành:
private BigDecimal calculateTotalAmount(BigDecimal subTotal, BigDecimal taxRate) {
    return subTotal.add(subTotal.multiply(taxRate));
}
```

## Kết luận

Các unit test đã phát hiện được 3 vấn đề logic nghiêm trọng cần sửa ngay:
1. **CreateFinalInvoiceUseCaseImpl**: Gán invoiceId 2 lần
2. **InvoicePaymentLinkUseCaseImpl**: Có thể gây NullPointerException  
3. **ProcessDirectPaymentUseCaseImpl**: Tính toán thuế sai

Các vấn đề còn lại là về naming convention, không ảnh hưởng đến logic nhưng nên sửa để code dễ hiểu hơn.

