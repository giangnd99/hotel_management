
package com.poly.notification.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification {

    @Id
    private Integer notificationId;
    private String userId;
    private String notificationMethod;
    private String message;
    private String priority;
    private LocalDateTime sentDate;
    private String status;

}
