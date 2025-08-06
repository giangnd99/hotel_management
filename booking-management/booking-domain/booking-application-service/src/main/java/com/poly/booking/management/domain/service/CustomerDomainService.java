package com.poly.booking.management.domain.service;

import com.poly.booking.management.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

/**
 * CustomerDomainService - Domain Service Interface for Customer Operations
 * 
 * CHỨC NĂNG:
 * - Định nghĩa business logic cho customer operations
 * - Tách biệt domain logic khỏi application logic
 * - Hỗ trợ Event Driven Architecture
 * 
 * EVENT DRIVEN FEATURES:
 * - CustomerCreatedEvent: Khi customer được tạo mới
 * - CustomerUpdatedEvent: Khi customer được cập nhật
 * - CustomerDeletedEvent: Khi customer bị xóa
 * 
 * BUSINESS RULES:
 * - Customer ID phải unique
 * - Username phải unique
 * - Email phải valid format
 * - Name không được empty
 */
public interface CustomerDomainService {

    /**
     * Tạo customer mới
     * 
     * @param customer Customer entity cần tạo
     * @return Customer đã được tạo
     */
    Customer createCustomer(Customer customer);

    /**
     * Tìm customer theo ID
     * 
     * @param customerId Customer ID
     * @return Optional chứa customer nếu tìm thấy
     */
    Optional<Customer> findCustomerById(UUID customerId);

    /**
     * Tìm customer theo username
     * 
     * @param username Username
     * @return Optional chứa customer nếu tìm thấy
     */
    Optional<Customer> findCustomerByUsername(String username);

    /**
     * Cập nhật customer
     * 
     * @param customer Customer entity cần cập nhật
     * @return Customer đã được cập nhật
     */
    Customer updateCustomer(Customer customer);

    /**
     * Xóa customer (soft delete)
     * 
     * @param customerId Customer ID cần xóa
     */
    void deleteCustomer(UUID customerId);

    /**
     * Kích hoạt customer
     * 
     * @param customerId Customer ID cần kích hoạt
     * @return Customer đã được kích hoạt
     */
    Customer activateCustomer(UUID customerId);

    /**
     * Vô hiệu hóa customer
     * 
     * @param customerId Customer ID cần vô hiệu hóa
     * @return Customer đã được vô hiệu hóa
     */
    Customer deactivateCustomer(UUID customerId);

    /**
     * Kiểm tra customer có tồn tại không
     * 
     * @param customerId Customer ID
     * @return true nếu customer tồn tại
     */
    boolean customerExists(UUID customerId);
} 