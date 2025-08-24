package com.poly.promotion.application.exception;

import com.poly.promotion.domain.core.exception.VoucherDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Global exception handler for the promotion management application.
 * Handles all exceptions thrown across the application and provides
 * consistent error responses.
 *
 * @author System
 * @since 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles domain-specific exceptions.
     *
     * @param ex the domain exception
     * @return error response with BAD_REQUEST status
     */
    @ExceptionHandler(VoucherDomainException.class)
    public ResponseEntity<ErrorResponse> handleVoucherDomainException(VoucherDomainException ex) {      
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Domain Validation Error")
                .message(ex.getMessage())
                .path(getCurrentRequestPath())
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles validation exceptions from @Valid annotations.
     *
     * @param ex the validation exception
     * @return error response with BAD_REQUEST status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {        
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing
                ));
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message("Request validation failed")
                .path(getCurrentRequestPath())
                .fieldErrors(fieldErrors)
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles constraint violation exceptions.
     *
     * @param ex the constraint violation exception
     * @return error response with BAD_REQUEST status
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {        
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        Map<String, String> fieldErrors = new HashMap<>();
        
        for (ConstraintViolation<?> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            fieldErrors.put(fieldName, message);
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Constraint Violation")
                .message("Request constraints violated")
                .path(getCurrentRequestPath())
                .fieldErrors(fieldErrors)
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles bind exceptions.
     *
     * @param ex the bind exception
     * @return error response with BAD_REQUEST status
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex) { 
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing
                ));
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Binding Error")
                .message("Request binding failed")
                .path(getCurrentRequestPath())
                .fieldErrors(fieldErrors)
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles missing request parameter exceptions.
     *
     * @param ex the missing parameter exception
     * @return error response with BAD_REQUEST status
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameterException(MissingServletRequestParameterException ex) {
              ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Missing Parameter")
                .message("Required parameter '" + ex.getParameterName() + "' is missing")
                .path(getCurrentRequestPath())
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles method argument type mismatch exceptions.
     *
     * @param ex the type mismatch exception
     * @return error response with BAD_REQUEST status
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Type Mismatch")
                .message("Parameter '" + ex.getName() + "' has invalid type. Expected: " + 
                        (ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"))
                .path(getCurrentRequestPath())
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles HTTP message not readable exceptions.
     *
     * @param ex the message not readable exception
     * @return error response with BAD_REQUEST status
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Request Body")
                .message("Request body is not readable or contains invalid JSON")
                .path(getCurrentRequestPath())
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles no handler found exceptions (404 errors).
     *
     * @param ex the no handler found exception
     * @return error response with NOT_FOUND status
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL())
                .path(getCurrentRequestPath())
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles all other unexpected exceptions.
     *
     * @param ex the unexpected exception
     * @return error response with INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred. Please try again later.")
                .path(getCurrentRequestPath())
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Gets the current request path for error reporting.
     * This is a simplified implementation - in a real application,
     * you might want to use RequestContextHolder or similar.
     *
     * @return the current request path or "unknown"
     */
    private String getCurrentRequestPath() {
        try {
            // In a real application, you would use RequestContextHolder
            // For now, return a placeholder
            return "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }
}
