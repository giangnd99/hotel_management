package com.poly.booking.management.domain.service;

import com.poly.booking.management.domain.dto.response.DepositBookingResponse;

import java.util.UUID;

public interface DepositBookingCommand {

    DepositBookingResponse depositBooking(UUID command);
}
