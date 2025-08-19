package com.poly.notification.management.repository.feign;

import com.poly.notification.management.dto.checkin.CheckInDto;
import com.poly.notification.management.dto.checkin.CheckInRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "room-service", url = "http://localhost:8087/api/reception/")
public interface RoomClient {
    /// checkin/{bookingId}
    @PostMapping("/checkin/{bookingId}")
    ResponseEntity<CheckInDto> performCheckIn(
            @PathVariable("bookingId") UUID bookingId,
            @RequestBody CheckInRequest request);
}
