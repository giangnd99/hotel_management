package edu.poly.servicemanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "services", schema = "service_management")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service_ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Integer serviceId;

    @Column(name = "service_name", nullable = false, unique = true, length = 200)
    private String serviceName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "availability", nullable = false, length = 50)
    private String availability;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
