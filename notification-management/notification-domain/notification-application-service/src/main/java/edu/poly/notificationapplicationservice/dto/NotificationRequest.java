
package edu.poly.notificationapplicationservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class NotificationRequest {
    private Integer userId;
    private String message;
    private String method; // EMAIL, SMS, etc.
    private String priority; // HIGH, MEDIUM, LOW
    private List<ReceiverInfo> receivers;
}

