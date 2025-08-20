package com.poly.promotion.application.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response structure for the promotion management application.
 * Provides consistent error information across all API endpoints.
 *
 * @author System
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Timestamp when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * HTTP status code.
     */
    private int status;

    /**
     * Error type/category.
     */
    private String error;

    /**
     * Human-readable error message.
     */
    private String message;

    /**
     * Request path where the error occurred.
     */
    private String path;

    /**
     * Field-specific validation errors (if applicable).
     */
    private Map<String, String> fieldErrors;

    /**
     * Additional error details (if applicable).
     */
    private String details;
}
