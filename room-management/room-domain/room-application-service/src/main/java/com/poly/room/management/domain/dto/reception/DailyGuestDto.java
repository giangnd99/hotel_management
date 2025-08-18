package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyGuestDto {
    private LocalDate date;
    private Long totalGuests;
    private Long newGuests;
    private Long returningGuests;
    private Long checkInGuests;
    private Long checkOutGuests;
    private Long currentGuests;
    private Long walkInGuests;
    private Long groupGuests;
    private Long individualGuests;
}
