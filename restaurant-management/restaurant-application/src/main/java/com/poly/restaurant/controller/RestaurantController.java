package com.poly.restaurant.controller;

import com.poly.restaurant.application.dto.*;
import com.poly.restaurant.application.port.in.RestaurantUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
@Tag(name = "Restaurant Controller", description = "Các API của nhà hàng")
@Slf4j(topic = "RESTAURANT CONTROLLER")
@Validated
public class RestaurantController {
    private final RestaurantUseCase restaurantUseCase;

    @GetMapping("/tables")
    public ResponseEntity<List<TableDTO>> getTables() {
        return ResponseEntity.ok(restaurantUseCase.getAllTables());
    }

    @GetMapping("/menu")
    public ResponseEntity<List<MenuItemDTO>> getMenu() {
        return ResponseEntity.ok(restaurantUseCase.getMenu());
    }

    @PostMapping("/order")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(restaurantUseCase.createOrder(request));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getOrders() {
        return ResponseEntity.ok(restaurantUseCase.getAllOrders());
    }

    @GetMapping("/staff")
    public ResponseEntity<List<StaffDTO>> getStaff() {
        return ResponseEntity.ok(restaurantUseCase.getAllStaff());
    }

}
