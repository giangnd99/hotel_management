package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestDto {
    private UUID guestId;
    private String fullName;
    private String phone;
    private String email;
    private String idNumber;
    private String idType;
    private String nationality;
    private String address;
    private LocalDate dateOfBirth;
    private String gender;
    private String specialRequests;
    private String status;
}