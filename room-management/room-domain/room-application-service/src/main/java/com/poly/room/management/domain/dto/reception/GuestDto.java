package com.poly.room.management.domain.dto.reception;

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
public class GuestDto {
    private UUID guestId;
    private String name;
    private String phone;
    private String email;
    private String address;
    private LocalDate dateOfBirth;
    private String nationality;
    private String idNumber;
    private String idType; // PASSPORT, ID_CARD, DRIVER_LICENSE
    private String guestType; // INDIVIDUAL, GROUP, CORPORATE
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}