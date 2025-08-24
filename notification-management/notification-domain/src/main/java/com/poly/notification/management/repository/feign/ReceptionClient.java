package com.poly.notification.management.repository.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(name = "room-service", url = "http://localhost:8087/api/rooms")
public interface ReceptionClient {
    @PostMapping("/checkin/{bookingId}")
    String performCheckIn(@PathVariable("bookingId") UUID bookingId);
}
