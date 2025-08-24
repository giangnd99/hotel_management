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
public class RoomServiceResponse {
    private String id;
    private String roomNumber;
    private String guestName;
    private String serviceType;
    private String serviceName;
    private String description;
    private Integer quantity;
    private String unitPrice;
    private String totalPrice;
    private String status;
    private String requestedBy;
    private String requestedAt;
    private String deliveredAt;
    private String specialInstructions;
    private String dietaryNotes;
}
