package com.poly.room.management.domain.entity;

import com.poly.room.management.domain.valueobject.GuestId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Guest {
    
    private GuestId id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phone;
    private String email;
    private String idNumber;
    private String idType; // PASSPORT, NATIONAL_ID, DRIVER_LICENSE
    private String nationality;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
    private String specialRequests;
    private String status; // ACTIVE, INACTIVE, BLACKLISTED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Business methods
    public String getFullName() {
        if (fullName != null && !fullName.trim().isEmpty()) {
            return fullName;
        }
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "").trim();
    }
    
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
    
    public boolean isBlacklisted() {
        return "BLACKLISTED".equals(status);
    }
    
    public void activate() {
        this.status = "ACTIVE";
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deactivate() {
        this.status = "INACTIVE";
        this.updatedAt = LocalDateTime.now();
    }
    
    public void blacklist() {
        this.status = "BLACKLISTED";
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasValidId() {
        return idNumber != null && !idNumber.trim().isEmpty() && 
               idType != null && !idType.trim().isEmpty();
    }
    
    public boolean hasValidContact() {
        return (phone != null && !phone.trim().isEmpty()) || 
               (email != null && !email.trim().isEmpty());
    }
}
