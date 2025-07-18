package com.poly.restaurant.controller;

import com.poly.restaurant.application.dto.OrderDTO;
import com.poly.restaurant.application.port.in.RestaurantUseCase;
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

    @PostMapping("/order")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid OrderDTO request) {
        OrderDTO created = restaurantUseCase.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);

    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders() {
        return ResponseEntity.ok(restaurantUseCase.getAllOrders());
    }
}
