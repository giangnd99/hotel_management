package com.poly.promotion.application.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to log business operations with context information.
 * Applied to methods that represent significant business operations.
 *
 * @author System
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogBusinessOperation {
    
    /**
     * Description of the business operation.
     * This will be included in the log message.
     */
    String value();
    
    /**
     * Category of the business operation.
     * Used for grouping and filtering logs.
     */
    String category() default "GENERAL";
    
    /**
     * Whether to log the operation result.
     * Default is true.
     */
    boolean logResult() default true;
    
    /**
     * Whether to log the operation duration.
     * Default is true.
     */
    boolean logDuration() default true;
    
    /**
     * Whether to log the operation context (parameters).
     * Default is true.
     */
    boolean logContext() default true;
}
