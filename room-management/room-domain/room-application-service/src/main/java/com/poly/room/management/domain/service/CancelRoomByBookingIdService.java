package com.poly.room.management.domain.service;

import java.util.List;
import java.util.UUID;

public interface CancelRoomByBookingIdService {

    void cancelRoomByBookingId(List<UUID> bookingId);
}
