package com.poly.restaurant.controller;

import com.poly.restaurant.application.dto.OrderDTO;

import com.poly.restaurant.application.port.in.OrderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "Quản lý đơn đặt món")
@Slf4j(topic = "RESTAURANT ORDER CONTROLLER")
@Validated
public class OrderController {

    private final OrderUseCase orderUseCase;

    @PostMapping
    @Operation(summary = "Tạo đơn hàng mới")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid OrderDTO request) {
        log.info("Creating new order with payment: {}", request.id());
        OrderDTO created = orderUseCase.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/process")
    @Operation(summary = "Xử lý đơn hàng (chuyển sang IN_PROGRESS)")
    public ResponseEntity<OrderDTO> processOrder(@PathVariable String id) {
        log.info("Processing order: {}", id);
        OrderDTO processed = orderUseCase.processOrderWithNotification(id);
        return ResponseEntity.ok(processed);
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Hoàn thành đơn hàng")
    public ResponseEntity<OrderDTO> completeOrder(@PathVariable String id) {
        log.info("Completing order: {}", id);
        OrderDTO completed = orderUseCase.completeOrderWithNotification(id);
        return ResponseEntity.ok(completed);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Cập nhật trạng thái đơn hàng")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable String id,
            @RequestParam String status) {
        log.info("Updating order status: {} to {}", id, status);
        OrderDTO updated = orderUseCase.updateOrderStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Hủy đơn hàng")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable String id) {
        log.info("Cancelling order: {}", id);
        OrderDTO cancelled = orderUseCase.cancelOrder(id);
        return ResponseEntity.ok(cancelled);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hủy đơn hàng với refund")
    public ResponseEntity<OrderDTO> cancelOrderWithRefund(
            @PathVariable String id,
            @RequestParam(required = false, defaultValue = "Customer request") String reason) {
        log.info("Cancelling order with refund: {}", id);
        OrderDTO cancelled = orderUseCase.cancelOrderWithRefundAndNotification(id, reason);
        return ResponseEntity.ok(cancelled);
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả đơn hàng")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        log.info("Getting all orders");
        List<OrderDTO> orders = orderUseCase.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy đơn hàng theo ID")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String id) {
        log.info("Getting order by id: {}", id);
        OrderDTO order = orderUseCase.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Lấy đơn hàng theo khách hàng")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(@PathVariable String customerId) {
        log.info("Getting orders by customer: {}", customerId);
        List<OrderDTO> orders = orderUseCase.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/table/{tableId}")
    @Operation(summary = "Lấy đơn hàng theo bàn")
    public ResponseEntity<List<OrderDTO>> getOrdersByTable(@PathVariable String tableId) {
        log.info("Getting orders by table: {}", tableId);
        List<OrderDTO> orders = orderUseCase.getOrdersByTable(tableId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Lấy đơn hàng theo trạng thái")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable String status) {
        log.info("Getting orders by status: {}", status);
        List<OrderDTO> orders = orderUseCase.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }
}