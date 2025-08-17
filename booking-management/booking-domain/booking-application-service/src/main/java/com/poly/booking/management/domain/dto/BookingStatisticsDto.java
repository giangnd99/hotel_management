package com.poly.booking.management.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatisticsDto {
    private Long totalBookings;
    private Long successfulBookings;
    private Long pendingBookings;
    private Long cancelledBookings;
    private Long checkInBookings;
    private Long checkOutBookings;
    private Double totalRevenue;
    private Double averageBookingValue;
}
