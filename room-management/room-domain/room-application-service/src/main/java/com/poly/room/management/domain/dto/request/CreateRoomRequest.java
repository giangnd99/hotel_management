package com.poly.room.management.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {
    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @Min(value = 0, message = "Floor must be a non-negative number")
    private int floor;

    @NotNull(message = "Room type ID is required")
    private Integer roomTypeId;
}
