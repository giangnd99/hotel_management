
package com.poly.notification.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "notifications")
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
