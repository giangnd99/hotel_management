package edu.poly.notificationapplicationservice.dto;

import lombok.Data;

@Data
public class ReceiverInfo {
    private Integer userId;
    private String receiverType; // TO, CC, BCC
    private String additionalEmail;
}