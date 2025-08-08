package com.poly.restaurant.domain.entity;

import java.time.LocalTime;

public class HotelRestaurantHours {
    private final LocalTime openTime;
    private final LocalTime closeTime;

    public HotelRestaurantHours(LocalTime openTime, LocalTime closeTime) {
        if (openTime == null || closeTime == null)
            throw new IllegalArgumentException("Open and close time must not be null");
        if (!closeTime.isAfter(openTime)) throw new IllegalArgumentException("Close time must be after open time");
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public boolean isOpen(LocalTime now) {
        return !now.isBefore(openTime) && !now.isAfter(closeTime);
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }
} 