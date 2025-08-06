package com.poly.booking.management.domain.event;

import com.poly.booking.management.domain.entity.Customer;
import com.poly.domain.event.DomainEvent;
import com.poly.domain.valueobject.DateCustom;

/**
 * CustomerCreatedEvent - Domain Event for Customer Creation
 * 
 * CHỨC NĂNG:
 * - Đại diện cho event khi customer được tạo mới
 * - Sử dụng trong Event Driven Architecture
 * - Publish cho các service khác subscribe
 * 
 * EVENT DRIVEN FEATURES:
 * - Event Type: CUSTOMER_CREATED
 * - Event Data: Customer entity và timestamp
 * - Event Consumers: Notification service, Analytics service, etc.
 * 
 * USAGE:
 * - Publish khi customer được tạo thành công
 * - Consume bởi các service cần thông tin customer mới
 * - Trigger các business process khác
 */
public class CustomerCreatedEvent implements DomainEvent<Customer> {

    private final Customer customer;
    private final DateCustom createdAt;

    /**
     * Constructor cho CustomerCreatedEvent
     * 
     * @param customer Customer entity được tạo
     * @param createdAt Thời gian tạo event
     */
    public CustomerCreatedEvent(Customer customer, DateCustom createdAt) {
        this.customer = customer;
        this.createdAt = createdAt;
    }

    /**
     * Constructor cho CustomerCreatedEvent với timestamp hiện tại
     * 
     * @param customer Customer entity được tạo
     */
    public CustomerCreatedEvent(Customer customer) {
        this.customer = customer;
        this.createdAt = DateCustom.now();
    }

    /**
     * Lấy customer từ event
     * 
     * @return Customer entity
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Lấy customer ID từ event
     * 
     * @return Customer ID
     */
    public String getCustomerId() {
        return customer.getId().getValue().toString();
    }

    /**
     * Lấy username từ event
     * 
     * @return Username
     */
    public String getUsername() {
        return customer.getUsername();
    }

    /**
     * Lấy full name từ event
     * 
     * @return Full name
     */
    public String getFullName() {
        return customer.getFullName();
    }

    /**
     * Lấy email từ event
     * 
     * @return Email
     */
    public String getEmail() {
        return customer.getEmail();
    }

    /**
     * Lấy customer status từ event
     * 
     * @return Customer status
     */
    public Customer.CustomerStatus getStatus() {
        return customer.getStatus();
    }

    /**
     * Lấy thời gian tạo event
     * 
     * @return Thời gian tạo event
     */
    public DateCustom getCreatedAt() {
        return createdAt;
    }
} 