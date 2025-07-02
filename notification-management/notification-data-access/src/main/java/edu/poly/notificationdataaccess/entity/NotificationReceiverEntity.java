package edu.poly.notificationdataaccess.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification_receivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationReceiverEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private NotificationEntity notification;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "receiver_cc_bcc", nullable = false)
    private String receiverType;

    @Column(name = "additional_email")
    private String additionalEmail;
}