package com.poly.staff.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffModel {
    String staffId;
    String userId;
    Integer departmentId;
    Set<String> permissions;
    String name;
    String email;
    String phone;
    String address;
    String bankName;
    String bankAccount;
    String avatar;
    LocalDate hireDate;
    Double baseSalary;
    Integer status;
}
