package com.poly.ai.management.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GuestResponse {
    private String firstName;
    private String lastName;
    private String fullName;
    private String phone;
    private String email;
    private String nationality;
    private String address;
    private String dateOfBirth;
    private String gender;
    private String specialRequests;
    private String loyaltyLevel;
    private String preferredLanguage;
    private String dietaryRestrictions;
}
