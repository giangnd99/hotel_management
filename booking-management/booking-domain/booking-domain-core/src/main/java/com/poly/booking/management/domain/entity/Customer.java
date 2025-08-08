package com.poly.booking.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.CustomerId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Customer - Domain Entity for Customer Management
 * 
 * CHỨC NĂNG:
 * - Đại diện cho customer trong hệ thống booking
 * - Quản lý thông tin customer và trạng thái
 * - Tích hợp với Event Driven Architecture
 * 
 * EVENT DRIVEN FEATURES:
 * - CustomerCreatedEvent: Khi customer được tạo mới
 * - CustomerUpdatedEvent: Khi customer được cập nhật
 * - CustomerDeletedEvent: Khi customer bị xóa
 * 
 * BUSINESS RULES:
 * - Customer ID phải unique
 * - Email phải valid format
 * - Name không được empty
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Customer extends BaseEntity<CustomerId> {

    private String name;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CustomerStatus status;

    /**
     * CustomerStatus - Enum định nghĩa trạng thái customer
     */
    public enum CustomerStatus {
        ACTIVE, INACTIVE, SUSPENDED, DELETED
    }

    /**
     * Constructor mặc định
     */
    public Customer() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = CustomerStatus.ACTIVE;
    }

    /**
     * Tạo customer mới từ CustomerCreatedMessageResponse
     * 
     * @param customerId Customer ID
     * @param username Username
     * @param firstName First name
     * @param lastName Last name
     * @return Customer instance
     */
    public static Customer createCustomer(UUID customerId, String username, String firstName, String lastName) {
        Customer customer = new Customer();
        customer.setId(new CustomerId(customerId));
        customer.setUsername(username);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setName(firstName + " " + lastName);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setStatus(CustomerStatus.ACTIVE);
        
        return customer;
    }

    /**
     * Cập nhật thông tin customer
     * 
     * @param firstName First name mới
     * @param lastName Last name mới
     */
    public void updateCustomerInfo(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = firstName + " " + lastName;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Cập nhật email
     * 
     * @param email Email mới
     */
    public void updateEmail(String email) {
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Kích hoạt customer
     */
    public void activate() {
        this.status = CustomerStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Vô hiệu hóa customer
     */
    public void deactivate() {
        this.status = CustomerStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Đình chỉ customer
     */
    public void suspend() {
        this.status = CustomerStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Xóa customer (soft delete)
     */
    public void delete() {
        this.status = CustomerStatus.DELETED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Kiểm tra customer có active không
     * 
     * @return true nếu customer active
     */
    public boolean isActive() {
        return CustomerStatus.ACTIVE.equals(this.status);
    }

    /**
     * Lấy full name của customer
     * 
     * @return Full name
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
