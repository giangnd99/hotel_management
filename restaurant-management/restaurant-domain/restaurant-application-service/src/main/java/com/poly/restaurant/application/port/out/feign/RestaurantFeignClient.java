package com.poly.restaurant.application.port.out.feign;

import com.poly.restaurant.application.dto.OrderDTO;
import com.poly.restaurant.application.dto.OrderStatusUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "restaurant-service", url = "${restaurant.service.url}")
public interface RestaurantFeignClient {

    @PutMapping("/api/restaurant/orders/{orderId}/payment-status")
    void updateOrderPaymentStatus(@PathVariable String orderId, @RequestBody OrderStatusUpdateDTO request);

    @GetMapping("/api/restaurant/orders/{orderId}")
    OrderDTO getOrderById(@PathVariable String orderId);

    @PutMapping("/api/restaurant/orders/{orderId}/status")
    void updateOrderStatus(@PathVariable String orderId, @RequestParam String status);
}
