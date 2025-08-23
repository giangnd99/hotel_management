package com.poly.room.management.application.controller.rest;

import com.poly.room.management.domain.service.ReceptionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/room/")
@Slf4j
@RequiredArgsConstructor
public class RoomCheckInController {
    private final ReceptionService receptionService;

    @PostMapping("/checkin/{bookingId}")
    @Operation(summary = "Thực hiện check-in")
    public ResponseEntity<String> performCheckIn(
            @PathVariable UUID bookingId) {
        log.info("Performing check-in for booking: {}", bookingId);
        String checkIn = receptionService.performCheckIn(bookingId);
        return ResponseEntity.status(HttpStatus.CREATED).body(checkIn);
    }

}
