package com.poly.booking.management.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
