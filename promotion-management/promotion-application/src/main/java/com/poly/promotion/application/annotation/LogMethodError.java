package com.poly.promotion.application.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to log method errors and exceptions.
 * Applied to methods that should have their errors logged.
 *
 * @author System
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogMethodError {
    
    /**
     * Custom message to log when an error occurs.
     * If not specified, a default message will be generated.
     */
    String value() default "";
    
    /**
     * Whether to log the exception message.
     * Default is true.
     */
    boolean logExceptionMessage() default true;
    
    /**
     * Whether to log the exception stack trace.
     * Default is false (to avoid log flooding).
     */
    boolean logStackTrace() default false;
    
    /**
     * Whether to log the method name.
     * Default is true.
     */
    boolean logMethodName() default true;
    
    /**
     * Whether to log method parameters when error occurs.
     * Default is true.
     */
    boolean logParameters() default true;
}
