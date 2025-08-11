package com.poly.room.management.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoomRequest {
    @NotNull(message = "Room ID is required for update")
    private UUID roomId;

    @NotBlank(message = "New room number is required")
    private String roomNumber;

    @Min(value = 0, message = "New floor must be a non-negative number")
    private int floor;

    @NotNull(message = "New room type ID is required")
    private Integer roomTypeId;
}
