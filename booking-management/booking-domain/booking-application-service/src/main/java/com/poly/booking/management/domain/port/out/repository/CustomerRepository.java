package com.poly.booking.management.domain.port.out.repository;

import com.poly.booking.management.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

/**
 * CustomerRepository - Repository Interface for Customer Data Access
 * 
 * CHỨC NĂNG:
 * - Định nghĩa contract cho customer data access
 * - Tách biệt domain logic khỏi data access logic
 * - Hỗ trợ Event Driven Architecture
 * 
 * EVENT DRIVEN FEATURES:
 * - Lưu customer sau khi nhận CustomerCreatedEvent
 * - Cập nhật customer sau khi nhận CustomerUpdatedEvent
 * - Xóa customer sau khi nhận CustomerDeletedEvent
 */
public interface CustomerRepository {

    /**
     * Lưu customer mới
     * 
     * @param customer Customer entity cần lưu
     * @return Customer đã được lưu
     */
    Customer save(Customer customer);

    /**
     * Tìm customer theo ID
     * 
     * @param customerId Customer ID
     * @return Optional chứa customer nếu tìm thấy
     */
    Optional<Customer> findById(UUID customerId);

    /**
     * Tìm customer theo username
     * 
     * @param username Username
     * @return Optional chứa customer nếu tìm thấy
     */
    Optional<Customer> findByUsername(String username);

    /**
     * Tìm customer theo email
     * 
     * @param email Email
     * @return Optional chứa customer nếu tìm thấy
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Cập nhật customer
     * 
     * @param customer Customer entity cần cập nhật
     * @return Customer đã được cập nhật
     */
    Customer update(Customer customer);

    /**
     * Xóa customer (soft delete)
     * 
     * @param customerId Customer ID cần xóa
     */
    void deleteById(UUID customerId);

    /**
     * Kiểm tra customer có tồn tại không
     * 
     * @param customerId Customer ID
     * @return true nếu customer tồn tại
     */
    boolean existsById(UUID customerId);
}
