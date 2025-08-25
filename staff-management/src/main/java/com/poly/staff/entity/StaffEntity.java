package com.poly.staff.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "staffs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffEntity {
    
    @Id
    @Column(name = "staff_id", nullable = false, unique = true)
    private String staffId;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "department")
    private String department;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StaffStatus status;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public enum StaffStatus {
        ACTIVE, INACTIVE, ON_LEAVE, TERMINATED
    }
}
