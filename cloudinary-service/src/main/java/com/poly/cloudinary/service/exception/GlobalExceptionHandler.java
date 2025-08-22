package com.poly.cloudinary.service.exception;

import com.poly.cloudinary.service.dto.UploadResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<UploadResponseDto> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
        log.error("File quá lớn: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(UploadResponseDto.builder()
                        .success(false)
                        .message("File quá lớn. Kích thước tối đa là 10MB")
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UploadResponseDto> handleGenericException(Exception e) {
        log.error("Lỗi không xác định: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(UploadResponseDto.builder()
                        .success(false)
                        .message("Lỗi server: " + e.getMessage())
                        .build());
    }
}
