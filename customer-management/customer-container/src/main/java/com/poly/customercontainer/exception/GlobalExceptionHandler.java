package com.poly.customercontainer.exception;

import com.poly.customerapplicationservice.exception.BlankUserIdException;
import com.poly.customerapplicationservice.exception.UserExistException;
import com.poly.customercontainer.shared.request.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BlankUserIdException.class)
    public ResponseEntity<ApiResponse> handleBlankCustomerUserId(BlankUserIdException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(ex.getMessage(), ex.getErrorCode()));
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<ApiResponse> handleCustomerExist(UserExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.failure(ex.getMessage(), ex.getErrorCode()));
    }

    // (Optional) Cái này bắt tất cả exception chưa xác định để debug dễ
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGeneric(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong: " + ex.getMessage());
//    }
}
