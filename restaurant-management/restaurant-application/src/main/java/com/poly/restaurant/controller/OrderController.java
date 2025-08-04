package com.poly.restaurant.controller;

import com.poly.restaurant.application.dto.OrderDTO;
import com.poly.restaurant.application.port.in.RestaurantUseCase;
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

    private final RestaurantUseCase restaurantUseCase;

    @PostMapping
    @Operation(summary = "Tạo đơn hàng mới")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid OrderDTO request) {
        log.info("Creating new order: {}", request);
        OrderDTO created = restaurantUseCase.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả đơn hàng")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        log.info("Getting all orders");
        return ResponseEntity.ok(restaurantUseCase.getAllOrders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy đơn hàng theo ID")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String id) {
        log.info("Getting order by id: {}", id);
        // TODO: Implement get order by id logic
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Cập nhật trạng thái đơn hàng")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable String id, @RequestParam String status) {
        log.info("Updating order status: {} to {}", id, status);
        // TODO: Implement update order status logic
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hủy đơn hàng")
    public ResponseEntity<Void> cancelOrder(@PathVariable String id) {
        log.info("Cancelling order: {}", id);
        // TODO: Implement cancel order logic
        return ResponseEntity.noContent().build();
    }
}
