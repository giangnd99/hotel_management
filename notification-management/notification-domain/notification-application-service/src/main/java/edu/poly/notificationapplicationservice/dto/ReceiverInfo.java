package edu.poly.notificationapplicationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ReceiverInfo {
    @PositiveOrZero(message = "User ID must be positive or zero")
    private Integer userId;

    private String receiverType; // "TO", "CC", "BCC"

    @Email(message = "Additional email must be valid")
    private String additionalEmail;
}