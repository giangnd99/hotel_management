
package edu.poly.notificationapplicationservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private Integer notificationId;
    private String message;
    private LocalDateTime sentDate;
    private String status;
}

