package com.poly.promotion.application.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to log method entry with parameters.
 * Applied to methods that should have their entry logged.
 *
 * @author System
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogMethodEntry {
    
    /**
     * Custom message to log when entering the method.
     * If not specified, a default message will be generated.
     */
    String value() default "";
    
    /**
     * Whether to log method parameters.
     * Default is true.
     */
    boolean logParameters() default true;
    
    /**
     * Whether to log the method name.
     * Default is true.
     */
    boolean logMethodName() default true;
}
