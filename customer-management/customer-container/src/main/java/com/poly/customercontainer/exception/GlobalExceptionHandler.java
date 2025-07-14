package com.poly.customercontainer.exception;

import com.poly.customercontainer.shared.request.ApiResponse;
import com.poly.customerdomain.model.exception.BlankUserIdException;
import com.poly.customerdomain.model.exception.CustomerNotFoundException;
import com.poly.customerdomain.model.exception.DomainException;
import com.poly.customerdomain.model.exception.UserExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse> handleBlankCustomerUserId(DomainException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(ex.getMessage(), ex.getErrorCode()));
    }

//    @ExceptionHandler(UserExistException.class)
//    public ResponseEntity<ApiResponse> handleCustomerExist(UserExistException ex) {
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.failure(ex.getMessage(), ex.getErrorCode()));
//    }
//
//    @ExceptionHandler(CustomerNotFoundException.class)
//    public ResponseEntity<ApiResponse> handleCustomerNotFound(CustomerNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.failure(ex.getMessage(), ex.getErrorCode()));
//    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGeneric(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong: " + ex.getMessage());
//    }
}
