package com.poly.room.management.domain.dto.request;

import jakarta.annotation.Nullable;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FurnitureRequirementRequest {
    private UUID furnitureId;

    @Nullable
    private UUID roomTypeId;

    private Integer quantity;
}
