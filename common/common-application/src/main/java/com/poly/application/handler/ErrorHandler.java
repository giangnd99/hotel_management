package com.poly.application.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Builder
@Data
public class ErrorHandler {

    private String code;
    private String message;
    private HttpStatus status;
}
