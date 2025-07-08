package com.poly.authentication.service.domain.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.authentication.service.domain.dto.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j; // Sử dụng SLF4J cho logging

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler; // Quan trọng cho WebFlux
import org.springframework.core.annotation.Order; // Để xác định thứ tự của handler
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException; // Vẫn có thể dùng cho lỗi validation
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException; // vẫn có thể sử dụng
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Order(-1) // Đảm bảo handler này được thực thi trước các handler mặc định của Spring
@Slf4j // Lombok for logging
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();

        ApiResponse<?> apiResponse = null;
        ErrorCode errorCode = null;

        // Xử lý các loại ngoại lệ cụ thể
        if (ex instanceof AppException) {
            errorCode = ((AppException) ex).getErrorCode();
        } else if (ex instanceof MethodArgumentNotValidException) {
            // Xử lý lỗi validation cho WebFlux
            MethodArgumentNotValidException validationEx = (MethodArgumentNotValidException) ex;
            String enumKey = validationEx.getFieldError() != null ? validationEx.getFieldError().getDefaultMessage() : null;
            errorCode = ErrorCode.INVALID_KEY; // Mặc định là INVALID_KEY
            try {
                if (enumKey != null) {
                    errorCode = ErrorCode.valueOf(enumKey);
                }
            } catch (IllegalArgumentException e) {
                log.warn("Invalid ErrorCode enum key: {}", enumKey, e);
                // Giữ nguyên errorCode = ErrorCode.INVALID_KEY nếu không tìm thấy enum
            }
            apiResponse = ApiResponse.builder()
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage())
                    .build();
            response.setStatusCode(HttpStatus.BAD_REQUEST); // 400 Bad Request cho lỗi validation
        } else if (ex instanceof ConstraintViolationException constraintEx) {
            // Xử lý ConstraintViolationException (từ @Valid trên RequestParam/PathVariable)
            Map<String, Object> details = constraintEx.getConstraintViolations().stream()
                    .collect(Collectors.toMap(violation ->
                            violation.getPropertyPath().toString(), ConstraintViolation::getMessage));
            errorCode = ErrorCode.INVALID_PARAMETER;
            apiResponse = ApiResponse.builder()
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage())
                    .result(details)
                    .build();
            response.setStatusCode(HttpStatus.BAD_REQUEST); // 400 Bad Request
        } else if (ex instanceof AccessDeniedException) {
            // Xử lý AccessDeniedException
            errorCode = ErrorCode.UNAUTHORIZED; // Hoặc ErrorCode.FORBIDDEN tùy theo ngữ cảnh của bạn
            response.setStatusCode(errorCode.getHttpStatus());
        } else {
            // Mặc định cho các ngoại lệ chưa được phân loại
            errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }

        // Nếu apiResponse chưa được tạo bởi các khối if/else trên, tạo nó từ errorCode
        if (apiResponse == null) {
            apiResponse = ApiResponse.builder()
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage())
                    .build();
        }

        // Đặt Content-Type là application/json
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // Chuyển ApiResponse thành JSON bytes và ghi vào response body
        ApiResponse<?> finalApiResponse = apiResponse;
        return response.writeWith(Mono.fromCallable(() -> {
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(finalApiResponse));
            } catch (JsonProcessingException e) {
                log.error("Error writing error response", e);
                return bufferFactory.wrap("{\"code\":9999,\"message\":\"Internal server error\"}".getBytes()); // Fallback error
            }
        }));
    }
}