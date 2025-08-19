package com.poly.promotion.application.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to log method exit with return value and execution time.
 * Applied to methods that should have their exit logged.
 *
 * @author System
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogMethodExit {
    
    /**
     * Custom message to log when exiting the method.
     * If not specified, a default message will be generated.
     */
    String value() default "";
    
    /**
     * Whether to log the return value.
     * Default is true.
     */
    boolean logReturnValue() default true;
    
    /**
     * Whether to log execution time.
     * Default is true.
     */
    boolean logExecutionTime() default true;
    
    /**
     * Whether to log the method name.
     * Default is true.
     */
    boolean logMethodName() default true;
}
