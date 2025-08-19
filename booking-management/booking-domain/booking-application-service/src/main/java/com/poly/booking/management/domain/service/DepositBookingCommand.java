package com.poly.booking.management.domain.service;

import com.poly.booking.management.domain.dto.request.DepositBookingRequest;
import com.poly.booking.management.domain.dto.response.DepositBookingResponse;

public interface DepositBookingCommand {

    DepositBookingResponse depositBooking(DepositBookingRequest command);
}
