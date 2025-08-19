package com.poly.room.management.dao.guest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "guests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestEntity {
    
    @Id
    private UUID id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Column(unique = true)
    private String phone;
    
    @Column(unique = true)
    private String email;
    
    @Column(name = "id_number")
    private String idNumber;
    
    @Column(name = "id_type")
    private String idType;
    
    private String nationality;
    
    @Column(columnDefinition = "TEXT")
    private String address;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    private String gender;
    
    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;
    
    @Enumerated(EnumType.STRING)
    private GuestStatus status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum GuestStatus {
        ACTIVE,
        INACTIVE,
        BLACKLISTED
    }
}
