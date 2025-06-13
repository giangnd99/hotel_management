package com.poly.report.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActivityLogModel {
    String userId;
    String actor;// SYSTEM, ADMIN, MANAGER, STAFF
    String action;// URL of the action performed
    String status;
    String message;
}
