package com.poly.servicemanagement.controller;

import com.poly.servicemanagement.dto.CreateServiceOrderRequest;
import com.poly.servicemanagement.dto.ServiceOrderDTO;
import com.poly.servicemanagement.service.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/service-orders")
@RequiredArgsConstructor
@Slf4j
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;

    /**
     * Tạo một service order mới
     * POST /api/service-orders
     */
    @PostMapping
    public ResponseEntity<ServiceOrderDTO> createServiceOrder(@Valid @RequestBody CreateServiceOrderRequest request) {
        log.info("Creating service order for customer: {}", request.getCustomerId());
        ServiceOrderDTO createdOrder = serviceOrderService.createServiceOrder(request);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * Lấy tất cả service orders
     * GET /api/service-orders
     */
    @GetMapping
    public ResponseEntity<List<ServiceOrderDTO>> getAllServiceOrders() {
        List<ServiceOrderDTO> orders = serviceOrderService.getAllServiceOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Lấy service order theo ID
     * GET /api/service-orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderDTO> getServiceOrderById(@PathVariable Integer id) {
        return serviceOrderService.getServiceOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lấy service order theo order number
     * GET /api/service-orders/order-number/{orderNumber}
     */
    @GetMapping("/order-number/{orderNumber}")
    public ResponseEntity<ServiceOrderDTO> getServiceOrderByOrderNumber(@PathVariable String orderNumber) {
        return serviceOrderService.getServiceOrderByOrderNumber(orderNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lấy service orders theo customer ID
     * GET /api/service-orders/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ServiceOrderDTO>> getServiceOrdersByCustomerId(@PathVariable String customerId) {
        List<ServiceOrderDTO> orders = serviceOrderService.getServiceOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Lấy service orders theo room ID
     * GET /api/service-orders/room/{roomId}
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ServiceOrderDTO>> getServiceOrdersByRoomId(@PathVariable String roomId) {
        List<ServiceOrderDTO> orders = serviceOrderService.getServiceOrdersByRoomId(roomId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Lấy service orders theo status
     * GET /api/service-orders/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ServiceOrderDTO>> getServiceOrdersByStatus(@PathVariable String status) {
        List<ServiceOrderDTO> orders = serviceOrderService.getServiceOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /**
     * Lấy service orders theo khoảng thời gian
     * GET /api/service-orders/date-range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<ServiceOrderDTO>> getServiceOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ServiceOrderDTO> orders = serviceOrderService.getServiceOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    /**
     * Cập nhật status của service order
     * PUT /api/service-orders/{id}/status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ServiceOrderDTO> updateServiceOrderStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        try {
            ServiceOrderDTO updatedOrder = serviceOrderService.updateServiceOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cập nhật payment status của service order
     * PUT /api/service-orders/{id}/payment-status
     */
    @PutMapping("/{id}/payment-status")
    public ResponseEntity<ServiceOrderDTO> updatePaymentStatus(
            @PathVariable Integer id,
            @RequestParam String paymentStatus) {
        try {
            ServiceOrderDTO updatedOrder = serviceOrderService.updatePaymentStatus(id, paymentStatus);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Hủy service order
     * PUT /api/service-orders/{id}/cancel
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelServiceOrder(@PathVariable Integer id) {
        try {
            boolean cancelled = serviceOrderService.cancelServiceOrder(id);
            if (cancelled) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Xóa service order
     * DELETE /api/service-orders/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceOrder(@PathVariable Integer id) {
        boolean deleted = serviceOrderService.deleteServiceOrder(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
