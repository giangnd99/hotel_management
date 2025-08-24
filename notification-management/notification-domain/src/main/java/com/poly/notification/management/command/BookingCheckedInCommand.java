package com.poly.notification.management.command;

public interface BookingCheckedInCommand {

    void processCheckInByQrCodeWithBookingId(String bookingId);
}
