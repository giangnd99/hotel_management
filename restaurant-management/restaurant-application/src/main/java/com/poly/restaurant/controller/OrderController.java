package com.poly.restaurant.controller;

import com.poly.restaurant.application.dto.OrderDTO;
import com.poly.restaurant.application.handler.conmand.CreateOrderDirectlyCommand;
import com.poly.restaurant.application.handler.conmand.CreateOrderWithRoomDetailCommand;
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
    private final CreateOrderDirectlyCommand createOrderDirectlyCommand;
    private final CreateOrderWithRoomDetailCommand createOrderWithRoomDetailCommand;

    @PostMapping
    @Operation(summary = "Tạo đơn hàng mới với payment")
    public ResponseEntity<OrderDTO> createOrderWithPayment(@RequestBody @Valid OrderDTO request) {
        log.info("Creating new order with payment: {}", request.id());
        OrderDTO created = orderUseCase.createOrderWithPayment(request);
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

    // ========== NEW ORDER TYPES ==========

    @PostMapping("/direct")
    @Operation(summary = "Tạo đơn hàng với thanh toán trực tiếp")
    public ResponseEntity<OrderDTO> createDirectOrder(@RequestBody @Valid OrderDTO request) {
        log.info("Creating direct order: {}", request.id());
        OrderDTO created = orderUseCase.createDirectOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/room-attached")
    @Operation(summary = "Tạo đơn hàng đính kèm vào room")
    public ResponseEntity<OrderDTO> createRoomAttachedOrder(@RequestBody @Valid OrderDTO request) {
        log.info("Creating room attached order: {}", request.id());
        OrderDTO created = orderUseCase.createRoomAttachedOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Temporarily disabled payment triggering to decouple from Kafka
    // @PostMapping("/{id}/trigger-direct-payment")
    // @Operation(summary = "Kích hoạt payment request cho đơn hàng trực tiếp")
    // public ResponseEntity<Void> triggerDirectPaymentRequest(@PathVariable String id) {
    //     log.info("Triggering direct payment request for order: {}", id);
    //     OrderDTO order = orderUseCase.getOrderById(id);
    //     orderUseCase.triggerDirectPaymentRequest(order);
    //     return ResponseEntity.ok().build();
    // }

    // Temporarily disabled room payment triggering to decouple from Kafka
    // @PostMapping("/{id}/trigger-room-payment")
    // @Operation(summary = "Kích hoạt payment request cho đơn hàng đính kèm room (khi checkout)")
    // public ResponseEntity<Void> triggerRoomOrderPaymentRequest(@PathVariable String id) {
    //     log.info("Triggering room order payment request for order: {}", id);
    //     OrderDTO order = orderUseCase.getOrderById(id);
    //     orderUseCase.triggerRoomOrderPaymentRequest(order);
    //     return ResponseEntity.ok().build();
    // }

    @PostMapping("/direct/merge")
    @Operation(summary = "Kích hoạt payment request cho đơn hàng trực tiếp")
    public ResponseEntity<Void> triggerDirectPaymentRequest(@RequestBody @Valid OrderDTO request) {
        log.info("Triggering direct payment request for order: {}", request.id());
        createOrderDirectlyCommand.createOrder(request);
        return ResponseEntity.ok().build();
    }
}