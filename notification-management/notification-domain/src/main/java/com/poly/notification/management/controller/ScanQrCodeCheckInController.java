package com.poly.notification.management.controller;

import com.poly.notification.management.command.BookingCheckedInCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
public class ScanQrCodeCheckInController {

    private final BookingCheckedInCommand bookingCheckedInCommand;

    @PostMapping("/check-in")
    public void checkIn(@RequestBody String bookingId) {
        bookingCheckedInCommand.processCheckInByQrCodeWithBookingId(bookingId);
    }

}
