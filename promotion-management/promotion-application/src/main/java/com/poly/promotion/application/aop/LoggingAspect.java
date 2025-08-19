package com.poly.promotion.application.aop;

import com.poly.promotion.application.dto.monitoring.MethodExecutionMetrics;
import com.poly.promotion.application.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

/**
 * AOP aspect for comprehensive logging and monitoring of application activities.
 * Provides method entry/exit logging, performance monitoring, and exception tracking.
 *
 * @author System
 * @since 1.0.0
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingAspect {

    private final MonitoringService monitoringService;

    /**
     * Logs method entry with parameters for all public methods in the application layer.
     *
     * @param joinPoint the join point representing the method execution
     */
    @Before("execution(public * com.poly.promotion.application..*.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        log.info("=== ENTERING METHOD: {}.{}() ===", className, methodName);
        if (args.length > 0) {
            log.info("Parameters: {}", Arrays.toString(args));
        } else {
            log.info("No parameters");
        }
    }

    /**
     * Logs method execution time and exit for all public methods in the application layer.
     * Provides performance monitoring capabilities.
     *
     * @param joinPoint the join point representing the method execution
     * @return the result of the method execution
     * @throws Throwable if the method execution throws an exception
     */
    @Around("execution(public * com.poly.promotion.application..*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodSignature = className + "." + methodName + "()";
        
        long startTime = System.currentTimeMillis();
        LocalDateTime startDateTime = LocalDateTime.now();
        
        log.info("=== EXECUTING METHOD: {}.{}() ===", className, methodName);
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            LocalDateTime endDateTime = LocalDateTime.now();
            
            log.info("=== EXITING METHOD: {}.{}() - Execution Time: {}ms ===", 
                    className, methodName, executionTime);
            
            if (result != null) {
                log.debug("Return value: {}", result);
            }
            
            // Send metrics to monitoring service
            sendMethodExecutionMetrics(methodName, className, methodSignature, executionTime, 
                    startDateTime, endDateTime, true, null, null);
            
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            LocalDateTime endDateTime = LocalDateTime.now();
            
            log.error("=== EXCEPTION IN METHOD: {}.{}() - Execution Time: {}ms ===", 
                    className, methodName, executionTime);
            
            // Send error metrics to monitoring service
            sendMethodExecutionMetrics(methodName, className, methodSignature, executionTime, 
                    startDateTime, endDateTime, false, e.getMessage(), e.getClass().getSimpleName());
            
            throw e;
        }
    }

    /**
     * Logs exceptions thrown by methods in the application layer.
     * Provides comprehensive exception tracking and logging.
     *
     * @param joinPoint the join point representing the method execution
     * @param ex the exception that was thrown
     */
    @AfterThrowing(
            pointcut = "execution(public * com.poly.promotion.application..*.*(..))",
            throwing = "ex"
    )
    public void logMethodException(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        
        log.error("=== EXCEPTION IN METHOD: {}.{}() ===", className, methodName);
        log.error("Exception type: {}", ex.getClass().getSimpleName());
        log.error("Exception message: {}", ex.getMessage());
        log.error("Method parameters: {}", Arrays.toString(args));
        log.error("Stack trace:", ex);
        
        // Send error info to monitoring service (data will be stored locally first)
        String errorInfo = String.format("Method: %s.%s(), Exception: %s - %s, Parameters: %s", 
                className, methodName, ex.getClass().getSimpleName(), ex.getMessage(), Arrays.toString(args));
        monitoringService.sendErrorInfo(errorInfo);
    }

    /**
     * Sends method execution metrics to the monitoring service.
     *
     * @param methodName the name of the method
     * @param className the name of the class
     * @param methodSignature the full method signature
     * @param executionTime the execution time in milliseconds
     * @param startTime the start time
     * @param endTime the end time
     * @param success whether the execution was successful
     * @param errorMessage the error message if failed
     * @param exceptionClassName the exception class name if failed
     */
    private void sendMethodExecutionMetrics(String methodName, String className, String methodSignature,
                                         long executionTime, LocalDateTime startTime, LocalDateTime endTime,
                                         boolean success, String errorMessage, String exceptionClassName) {
        try {
            MethodExecutionMetrics metrics = MethodExecutionMetrics.builder()
                    .id(UUID.randomUUID().toString())
                    .methodName(methodName)
                    .className(className)
                    .methodSignature(methodSignature)
                    .executionTimeMs(executionTime)
                    .startTime(startTime)
                    .endTime(endTime)
                    .success(success)
                    .errorMessage(errorMessage)
                    .exceptionClassName(exceptionClassName)
                    .build();
            
            monitoringService.sendMethodExecutionMetrics(metrics);
        } catch (Exception e) {
            log.warn("Failed to send method execution metrics to monitoring service: {}", e.getMessage());
            // Metrics are still stored locally by the monitoring service
        }
    }
}
