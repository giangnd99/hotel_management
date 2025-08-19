package com.poly.room.management.dao.checkin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "check_ins")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInEntity {
    
    @Id
    private UUID id;
    
    private UUID bookingId;
    
    @Column(name = "guest_id", nullable = false)
    private UUID guestId;
    
    @Column(name = "room_id", nullable = false)
    private UUID roomId;
    
    @Column(name = "room_number", nullable = false)
    private String roomNumber;
    
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;
    
    @Column(name = "check_out_date")
    private LocalDate checkOutDate;
    
    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;
    
    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;
    
    @Column(name = "number_of_guests")
    private Integer numberOfGuests;
    
    @Column(name = "special_requests")
    private String specialRequests;
    
    @Enumerated(EnumType.STRING)
    private CheckInStatus status;
    
    @Column(name = "checked_in_by")
    private String checkedInBy;
    
    @Column(name = "checked_out_by")
    private String checkedOutBy;
    
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum CheckInStatus {
        PENDING,
        CHECKED_IN,
        EXTENDED,
        CHANGED_ROOM,
        CHECKED_OUT,
        CANCELLED
    }
}
