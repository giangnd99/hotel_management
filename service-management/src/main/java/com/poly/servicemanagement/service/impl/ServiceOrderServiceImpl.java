package com.poly.servicemanagement.service.impl;

import com.poly.servicemanagement.dto.CreateServiceOrderRequest;
import com.poly.servicemanagement.dto.ServiceOrderDTO;
import com.poly.servicemanagement.entity.ServiceOrder;
import com.poly.servicemanagement.entity.Service_;
import com.poly.servicemanagement.enums.PaymentStatus;
import com.poly.servicemanagement.enums.ServiceOrderStatus;
import com.poly.servicemanagement.messaging.message.PaymentRequestMessage;
import com.poly.servicemanagement.messaging.message.ServiceOrderRequestMessage;
import com.poly.servicemanagement.messaging.publisher.PaymentRequestPublisher;
import com.poly.servicemanagement.messaging.publisher.ServiceOrderRequestPublisher;
import com.poly.servicemanagement.repository.ServiceOrderRepository;
import com.poly.servicemanagement.repository.Service_Repository;
import com.poly.servicemanagement.service.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ServiceOrderServiceImpl implements ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final Service_Repository serviceRepository;
    private final ServiceOrderRequestPublisher serviceOrderRequestPublisher;
    private final PaymentRequestPublisher paymentRequestPublisher;

    @Override
    public ServiceOrderDTO createServiceOrder(CreateServiceOrderRequest request) {
        log.info("Creating service order for customer: {}, service: {}", request.getCustomerId(), request.getServiceId());
        
        // Validate service exists and is available
        Service_ service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + request.getServiceId()));
        
        if (!"Available".equals(service.getAvailability())) {
            throw new IllegalStateException("Service is not available: " + service.getServiceName());
        }
        
        // Calculate total amount
        BigDecimal totalAmount = service.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        
        // Generate order number
        String orderNumber = generateOrderNumber();
        
        // Create service order
        ServiceOrder serviceOrder = ServiceOrder.builder()
                .orderNumber(orderNumber)
                .serviceId(request.getServiceId())
                .customerId(request.getCustomerId())
                .roomId(request.getRoomId())
                .quantity(request.getQuantity())
                .totalAmount(totalAmount)
                .status(ServiceOrderStatus.NEW.name())
                .paymentStatus(PaymentStatus.PENDING.name())
                .specialInstructions(request.getSpecialInstructions())
                .orderDate(LocalDateTime.now())
                .build();
        
        ServiceOrder savedOrder = serviceOrderRepository.save(serviceOrder);
        
        log.info("Service order created successfully with order number: {}", orderNumber);
        
        // Publish service order request message
        publishServiceOrderRequest(savedOrder);
        
        // Publish payment request message
        publishPaymentRequest(savedOrder);
        
        return convertToDTO(savedOrder, service);
    }

    @Override
    public Optional<ServiceOrderDTO> getServiceOrderById(Integer orderId) {
        return serviceOrderRepository.findById(orderId)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<ServiceOrderDTO> getServiceOrderByOrderNumber(String orderNumber) {
        return serviceOrderRepository.findByOrderNumber(orderNumber)
                .map(this::convertToDTO);
    }

    @Override
    public List<ServiceOrderDTO> getAllServiceOrders() {
        return serviceOrderRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<ServiceOrderDTO> getServiceOrdersByCustomerId(String customerId) {
        return serviceOrderRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<ServiceOrderDTO> getServiceOrdersByRoomId(String roomId) {
        return serviceOrderRepository.findByRoomId(roomId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<ServiceOrderDTO> getServiceOrdersByStatus(String status) {
        return serviceOrderRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<ServiceOrderDTO> getServiceOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return serviceOrderRepository.findByOrderDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public ServiceOrderDTO updateServiceOrderStatus(Integer orderId, String status) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Service order not found with ID: " + orderId));
        
        serviceOrder.setStatus(status);
        ServiceOrder updatedOrder = serviceOrderRepository.save(serviceOrder);
        
        log.info("Service order status updated to {} for order ID: {}", status, orderId);
        
        return convertToDTO(updatedOrder);
    }

    @Override
    public ServiceOrderDTO updatePaymentStatus(Integer orderId, String paymentStatus) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Service order not found with ID: " + orderId));
        
        serviceOrder.setPaymentStatus(paymentStatus);
        ServiceOrder updatedOrder = serviceOrderRepository.save(serviceOrder);
        
        log.info("Payment status updated to {} for order ID: {}", paymentStatus, orderId);
        
        return convertToDTO(updatedOrder);
    }

    @Override
    public boolean cancelServiceOrder(Integer orderId) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Service order not found with ID: " + orderId));
        
        if (ServiceOrderStatus.COMPLETED.name().equals(serviceOrder.getStatus())) {
            throw new IllegalStateException("Cannot cancel completed order");
        }
        
        serviceOrder.setStatus(ServiceOrderStatus.CANCELLED.name());
        serviceOrderRepository.save(serviceOrder);
        
        log.info("Service order cancelled for order ID: {}", orderId);
        
        return true;
    }

    @Override
    public boolean deleteServiceOrder(Integer orderId) {
        if (serviceOrderRepository.existsById(orderId)) {
            serviceOrderRepository.deleteById(orderId);
            log.info("Service order deleted for order ID: {}", orderId);
            return true;
        }
        return false;
    }

    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return "SO-" + timestamp + "-" + uuid;
    }

    private void publishServiceOrderRequest(ServiceOrder serviceOrder) {
        try {
            ServiceOrderRequestMessage message = ServiceOrderRequestMessage.builder()
                    .orderId(serviceOrder.getOrderId().toString())
                    .orderNumber(serviceOrder.getOrderNumber())
                    .serviceId(serviceOrder.getServiceId())
                    .customerId(serviceOrder.getCustomerId())
                    .roomId(serviceOrder.getRoomId())
                    .quantity(serviceOrder.getQuantity())
                    .totalAmount(serviceOrder.getTotalAmount())
                    .status(serviceOrder.getStatus())
                    .paymentStatus(serviceOrder.getPaymentStatus())
                    .specialInstructions(serviceOrder.getSpecialInstructions())
                    .orderDate(serviceOrder.getOrderDate())
                    .createdAt(serviceOrder.getCreatedAt())
                    .updatedAt(serviceOrder.getUpdatedAt())
                    .build();
            
            serviceOrderRequestPublisher.publish(message);
            log.info("Service order request message published for order: {}", serviceOrder.getOrderNumber());
        } catch (Exception e) {
            log.error("Failed to publish service order request message for order: {}", 
                    serviceOrder.getOrderNumber(), e);
        }
    }

    private void publishPaymentRequest(ServiceOrder serviceOrder) {
        try {
            PaymentRequestMessage message = PaymentRequestMessage.builder()
                    .orderId(serviceOrder.getOrderId().toString())
                    .orderNumber(serviceOrder.getOrderNumber())
                    .customerId(serviceOrder.getCustomerId())
                    .amount(serviceOrder.getTotalAmount())
                    .paymentMethod("CREDIT_CARD") // Default payment method
                    .currency("VND")
                    .description("Payment for service order: " + serviceOrder.getOrderNumber())
                    .build();
            
            paymentRequestPublisher.publish(message);
            log.info("Payment request message published for order: {}", serviceOrder.getOrderNumber());
        } catch (Exception e) {
            log.error("Failed to publish payment request message for order: {}", 
                    serviceOrder.getOrderNumber(), e);
        }
    }

    private ServiceOrderDTO convertToDTO(ServiceOrder serviceOrder) {
        Service_ service = serviceRepository.findById(serviceOrder.getServiceId()).orElse(null);
        return convertToDTO(serviceOrder, service);
    }

    private ServiceOrderDTO convertToDTO(ServiceOrder serviceOrder, Service_ service) {
        return ServiceOrderDTO.builder()
                .orderId(serviceOrder.getOrderId())
                .orderNumber(serviceOrder.getOrderNumber())
                .serviceId(serviceOrder.getServiceId())
                .customerId(serviceOrder.getCustomerId())
                .roomId(serviceOrder.getRoomId())
                .quantity(serviceOrder.getQuantity())
                .totalAmount(serviceOrder.getTotalAmount())
                .status(serviceOrder.getStatus())
                .paymentStatus(serviceOrder.getPaymentStatus())
                .specialInstructions(serviceOrder.getSpecialInstructions())
                .orderDate(serviceOrder.getOrderDate())
                .createdAt(serviceOrder.getCreatedAt())
                .updatedAt(serviceOrder.getUpdatedAt())
                .serviceName(service != null ? service.getServiceName() : null)
                .servicePrice(service != null ? service.getPrice() : null)
                .build();
    }
}
