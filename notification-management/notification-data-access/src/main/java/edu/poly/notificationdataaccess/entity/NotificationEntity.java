package edu.poly.notificationdataaccess.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sent_notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "notification_method", nullable = false)
    private String notificationMethod;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "priority", nullable = false)
    private String priority;

    @Column(name = "sent_date", nullable = false)
    private LocalDateTime sentDate;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<NotificationReceiverEntity> receivers;
}