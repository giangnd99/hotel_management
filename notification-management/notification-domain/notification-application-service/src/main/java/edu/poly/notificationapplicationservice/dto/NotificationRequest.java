// NotificationRequest.java
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

// NotificationResponse.java
package edu_poly_notificationapplicationservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private Integer notificationId;
    private String message;
    private LocalDateTime sentDate;
    private String status;
}

// ReceiverInfo.java
package edu_poly_notificationapplicationservice.dto;

import lombok.Data;

@Data
public class ReceiverInfo {
    private Integer userId;
    private String receiverType; // TO, CC, BCC
    private String additionalEmail;
}