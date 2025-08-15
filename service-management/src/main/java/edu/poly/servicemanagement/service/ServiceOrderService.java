package edu.poly.servicemanagement.service;

import edu.poly.servicemanagement.dto.CreateServiceOrderRequest;
import edu.poly.servicemanagement.dto.ServiceOrderDTO;
import edu.poly.servicemanagement.entity.ServiceOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ServiceOrderService {
    
    ServiceOrderDTO createServiceOrder(CreateServiceOrderRequest request);
    
    Optional<ServiceOrderDTO> getServiceOrderById(Integer orderId);
    
    Optional<ServiceOrderDTO> getServiceOrderByOrderNumber(String orderNumber);
    
    List<ServiceOrderDTO> getAllServiceOrders();
    
    List<ServiceOrderDTO> getServiceOrdersByCustomerId(String customerId);
    
    List<ServiceOrderDTO> getServiceOrdersByRoomId(String roomId);
    
    List<ServiceOrderDTO> getServiceOrdersByStatus(String status);
    
    List<ServiceOrderDTO> getServiceOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    ServiceOrderDTO updateServiceOrderStatus(Integer orderId, String status);
    
    ServiceOrderDTO updatePaymentStatus(Integer orderId, String paymentStatus);
    
    boolean cancelServiceOrder(Integer orderId);
    
    boolean deleteServiceOrder(Integer orderId);
}
