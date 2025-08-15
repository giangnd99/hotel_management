package edu.poly.servicemanagement.repository;

import edu.poly.servicemanagement.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Integer> {
    
    Optional<ServiceOrder> findByOrderNumber(String orderNumber);
    
    List<ServiceOrder> findByCustomerId(String customerId);
    
    List<ServiceOrder> findByRoomId(String roomId);
    
    List<ServiceOrder> findByStatus(String status);
    
    List<ServiceOrder> findByPaymentStatus(String paymentStatus);
    
    @Query("SELECT so FROM ServiceOrder so WHERE so.customerId = :customerId AND so.status = :status")
    List<ServiceOrder> findByCustomerIdAndStatus(@Param("customerId") String customerId, @Param("status") String status);
    
    @Query("SELECT so FROM ServiceOrder so WHERE so.orderDate BETWEEN :startDate AND :endDate")
    List<ServiceOrder> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT so FROM ServiceOrder so WHERE so.serviceId = :serviceId AND so.status = :status")
    List<ServiceOrder> findByServiceIdAndStatus(@Param("serviceId") Integer serviceId, @Param("status") String status);
}
