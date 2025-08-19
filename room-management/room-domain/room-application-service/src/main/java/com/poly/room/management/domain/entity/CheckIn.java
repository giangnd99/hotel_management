package com.poly.room.management.domain.entity;

import com.poly.room.management.domain.valueobject.CheckInId;
import com.poly.room.management.domain.valueobject.GuestId;
import com.poly.room.management.domain.valueobject.RoomId;
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
public class CheckIn {
    
    private CheckInId id;
    private UUID bookingId;
    private GuestId guestId;
    private RoomId roomId;
    private String roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Integer numberOfGuests;
    private String specialRequests;
    private String status; // CHECKED_IN, EXTENDED, CHANGED_ROOM, CHECKED_OUT
    private String checkedInBy;
    private String checkedOutBy;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Business methods
    public boolean isActive() {
        return "CHECKED_IN".equals(status) || "EXTENDED".equals(status);
    }
    
    public boolean isCheckedOut() {
        return "CHECKED_OUT".equals(status);
    }
    
    public boolean canExtend() {
        return isActive() && checkOutDate != null;
    }
    
    public boolean canChangeRoom() {
        return isActive();
    }
    
    public void extendStay(LocalDate newCheckOutDate) {
        if (canExtend()) {
            this.checkOutDate = newCheckOutDate;
            this.status = "EXTENDED";
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void changeRoom(String newRoomNumber) {
        if (canChangeRoom()) {
            this.roomNumber = newRoomNumber;
            this.status = "CHANGED_ROOM";
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void checkOut() {
        this.status = "CHECKED_OUT";
        this.checkOutTime = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
