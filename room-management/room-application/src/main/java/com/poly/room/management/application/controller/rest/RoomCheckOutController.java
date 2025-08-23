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
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/room/")
public class RoomCheckOutController {

    private final ReceptionService receptionService;

    @PostMapping("/checkout/{bookingId}")
    @Operation(summary = "Thực hiện check-out")
    public ResponseEntity<UUID> performCheckOut(
            @PathVariable UUID bookingId) {
        log.info("Performing check-out with booking id : {}", bookingId);
        UUID bookingCheckedOut = receptionService.performCheckOut(bookingId);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingCheckedOut);
    }
}
