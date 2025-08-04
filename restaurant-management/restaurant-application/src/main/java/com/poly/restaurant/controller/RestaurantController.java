package com.poly.restaurant.controller;

import com.poly.restaurant.application.dto.StaffDTO;
import com.poly.restaurant.application.dto.TableDTO;
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

    @GetMapping("/staff")
    public ResponseEntity<List<StaffDTO>> getStaff() {
        return ResponseEntity.ok(restaurantUseCase.getAllStaff());
    }
}
