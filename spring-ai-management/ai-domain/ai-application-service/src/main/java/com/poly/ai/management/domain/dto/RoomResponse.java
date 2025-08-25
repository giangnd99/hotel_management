package com.poly.ai.management.domain.dto;

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
public class RoomResponse {
    private String id;
    private String roomNumber;
    private int floor;
    private RoomTypeResponse roomType;
    private String roomStatus;
    private String image_url;
}
