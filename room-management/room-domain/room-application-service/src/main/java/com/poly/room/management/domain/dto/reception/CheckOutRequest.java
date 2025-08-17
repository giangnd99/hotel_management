package com.poly.room.management.domain.dto.reception;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckOutRequest {
    @NotNull(message = "Check-out time is required")
    private LocalDateTime checkOutTime;

    private String paymentMethod;
    private String notes;
    private String checkedOutBy;
    private Boolean isEarlyCheckOut;
    private String earlyCheckOutReason;
}