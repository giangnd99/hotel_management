package com.poly.ai.management.application.dto;

import lombok.Data;

@Data
public class FindAvailableRoomsInput {
    private String checkIn;
    private String checkOut;
    private String roomType;
}
