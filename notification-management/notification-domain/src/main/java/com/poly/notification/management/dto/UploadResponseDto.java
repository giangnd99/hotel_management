package com.poly.notification.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponseDto {
    private String imageUrl;
    private String message;
    private boolean success;
}
