package com.poly.room.management.application.controller.rest;

import com.poly.room.management.domain.service.CancelRoomByBookingIdService;
import com.poly.room.management.domain.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Slf4j
public class CancelRoomController {

    private final CancelRoomByBookingIdService roomService;

    @PostMapping("/cancel")
    public void cancelRoom(@RequestBody List<UUID> roomIds){
        log.info("Canceling room : {}", roomIds.size());
         roomService.cancelRoomByBookingId(roomIds);
    }
}
