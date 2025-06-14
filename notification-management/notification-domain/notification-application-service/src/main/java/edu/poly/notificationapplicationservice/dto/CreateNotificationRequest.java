package edu.poly.notificationapplicationservice.dto;


import edu.poly.notificationdomaincore.value_object.NotificationMethod;
import edu.poly.notificationdomaincore.value_object.NotificationPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Data
public class CreateNotificationRequest {

    @NotNull()
    @Positive(message = "User ID must be positive")
    private Integer userId;

    @NotBlank()
    private String message;

    @NotNull()
    private NotificationMethod method;

    @NotNull()
    private NotificationPriority priority;

    private List<ReceiverInfo> receivers;

    // Có thể thêm các trường khác tùy nghiệp vụ
    private String relatedEntityType; // "BOOKING", "PAYMENT", etc.
    private Integer relatedEntityId;
}