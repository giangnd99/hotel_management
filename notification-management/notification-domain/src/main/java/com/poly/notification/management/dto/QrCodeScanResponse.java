package com.poly.notification.management.dto;

import java.time.LocalDateTime;

/**
 * DTO response khi quét QR code
 * Chứa thông tin đầy đủ về booking, customer và trạng thái QR code
 */
public class QrCodeScanResponse {
    
    private String qrCodeId;
    private String bookingId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String roomNumber;
    private String roomType;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private String bookingStatus;
    private LocalDateTime qrCodeScannedAt;
    private String qrCodeStatus;
    private String message;
    
    // Constructors
    public QrCodeScanResponse() {}
    
    public QrCodeScanResponse(String bookingId, String message) {
        this.bookingId = bookingId;
        this.message = message;
    }
    
    // Getters and Setters
    public String getQrCodeId() {
        return qrCodeId;
    }
    
    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }
    
    public String getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public String getCustomerPhone() {
        return customerPhone;
    }
    
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public String getRoomType() {
        return roomType;
    }
    
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    
    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }
    
    public void setCheckInDate(LocalDateTime checkInDate) {
        this.checkInDate = checkInDate;
    }
    
    public LocalDateTime getCheckOutDate() {
        return checkOutDate;
    }
    
    public void setCheckOutDate(LocalDateTime checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    
    public String getBookingStatus() {
        return bookingStatus;
    }
    
    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
    
    public LocalDateTime getQrCodeScannedAt() {
        return qrCodeScannedAt;
    }
    
    public void setQrCodeScannedAt(LocalDateTime qrCodeScannedAt) {
        this.qrCodeScannedAt = qrCodeScannedAt;
    }
    
    public String getQrCodeStatus() {
        return qrCodeStatus;
    }
    
    public void setQrCodeStatus(String qrCodeStatus) {
        this.qrCodeStatus = qrCodeStatus;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    // Builder pattern methods
    public QrCodeScanResponse withQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
        return this;
    }
    
    public QrCodeScanResponse withCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }
    
    public QrCodeScanResponse withCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }
    
    public QrCodeScanResponse withCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
        return this;
    }
    
    public QrCodeScanResponse withCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
        return this;
    }
    
    public QrCodeScanResponse withRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
        return this;
    }
    
    public QrCodeScanResponse withRoomType(String roomType) {
        this.roomType = roomType;
        return this;
    }
    
    public QrCodeScanResponse withCheckInDate(LocalDateTime checkInDate) {
        this.checkInDate = checkInDate;
        return this;
    }
    
    public QrCodeScanResponse withCheckOutDate(LocalDateTime checkOutDate) {
        this.checkOutDate = checkOutDate;
        return this;
    }
    
    public QrCodeScanResponse withBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
        return this;
    }
    
    public QrCodeScanResponse withQrCodeScannedAt(LocalDateTime qrCodeScannedAt) {
        this.qrCodeScannedAt = qrCodeScannedAt;
        return this;
    }
    
    public QrCodeScanResponse withQrCodeStatus(String qrCodeStatus) {
        this.qrCodeStatus = qrCodeStatus;
        return this;
    }
}
