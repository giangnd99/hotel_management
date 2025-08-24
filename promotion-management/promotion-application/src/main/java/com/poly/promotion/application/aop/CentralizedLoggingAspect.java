package com.poly.promotion.application.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Centralized logging aspect that handles all logging operations across the application.
 * Replaces scattered Slf4j log calls with consistent, configurable logging behavior.
 *
 * <p>This aspect provides:</p>
 * <ul>
 *   <li><strong>Method Entry Logging:</strong> Logs when methods are entered with parameters</li>
 *   <li><strong>Method Exit Logging:</strong> Logs when methods exit with return values and execution time</li>
 *   <li><strong>Error Logging:</strong> Logs exceptions and errors with context</li>
 *   <li><strong>Business Operation Logging:</strong> Logs significant business operations</li>
 * </ul>
 *
 * @author System
 * @since 1.0.0
 */
// Temporarily disabled to fix StackOverflowError
// @Aspect
// @Component
@Slf4j
public class CentralizedLoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Logs method entry when @LogMethodEntry annotation is present.
     *
     * @param joinPoint the join point representing the method execution
     */
    @Before("@annotation(com.poly.promotion.application.annotation.LogMethodEntry)")
    public void logMethodEntry(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            com.poly.promotion.application.annotation.LogMethodEntry annotation = 
                method.getAnnotation(com.poly.promotion.application.annotation.LogMethodEntry.class);

            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = method.getName();
            
            StringBuilder logMessage = new StringBuilder();
            
            if (annotation.logMethodName()) {
                logMessage.append("🔍 Entering method: ").append(className).append(".").append(methodName);
            }
            
            if (annotation.logParameters()) {
                String parameters = formatParameters(joinPoint.getArgs());
                if (!parameters.isEmpty()) {
                    logMessage.append(" | Parameters: ").append(parameters);
                }
            }
            
            if (annotation.value().isEmpty()) {
                log.info(logMessage.toString());
            } else {
                log.info("{} | {}", annotation.value(), logMessage.toString());
            }
            
        } catch (Exception e) {
            log.warn("Failed to log method entry: {}", e.getMessage());
        }
    }

    /**
     * Logs method exit when @LogMethodExit annotation is present.
     * Measures execution time and logs return value.
     *
     * @param joinPoint the join point representing the method execution
     * @return the result of the method execution
     * @throws Throwable if the method execution fails
     */
    @Around("@annotation(com.poly.promotion.application.annotation.LogMethodExit)")
    public Object logMethodExit(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();
            
            logMethodExitDetails(joinPoint, result, stopWatch.getTotalTimeMillis(), null);
            return result;
            
        } catch (Throwable throwable) {
            stopWatch.stop();
            logMethodExitDetails(joinPoint, null, stopWatch.getTotalTimeMillis(), throwable);
            throw throwable;
        }
    }

    /**
     * Logs method errors when @LogMethodError annotation is present.
     *
     * @param joinPoint the join point representing the method execution
     * @param throwable the exception that occurred
     */
    @AfterThrowing(
        pointcut = "@annotation(com.poly.promotion.application.annotation.LogMethodError)",
        throwing = "throwable"
    )
    public void logMethodError(JoinPoint joinPoint, Throwable throwable) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            com.poly.promotion.application.annotation.LogMethodError annotation = 
                method.getAnnotation(com.poly.promotion.application.annotation.LogMethodError.class);

            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = method.getName();
            
            StringBuilder logMessage = new StringBuilder();
            
            if (annotation.logMethodName()) {
                logMessage.append("❌ Error in method: ").append(className).append(".").append(methodName);
            }
            
            if (annotation.logExceptionMessage()) {
                logMessage.append(" | Exception: ").append(throwable.getMessage());
            }
            
            if (annotation.logParameters()) {
                String parameters = formatParameters(joinPoint.getArgs());
                if (!parameters.isEmpty()) {
                    logMessage.append(" | Parameters: ").append(parameters);
                }
            }
            
            if (annotation.logStackTrace()) {
                log.error(logMessage.toString(), throwable);
            } else {
                log.error(logMessage.toString());
            }
            
            if (!annotation.value().isEmpty()) {
                log.error("{} | {}", annotation.value(), logMessage.toString());
            }
            
        } catch (Exception e) {
            log.warn("Failed to log method error: {}", e.getMessage());
        }
    }

    /**
     * Logs business operations when @LogBusinessOperation annotation is present.
     * Provides comprehensive logging for significant business operations.
     *
     * @param joinPoint the join point representing the method execution
     * @return the result of the method execution
     * @throws Throwable if the method execution fails
     */
    @Around("@annotation(com.poly.promotion.application.annotation.LogBusinessOperation)")
    public Object logBusinessOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            com.poly.promotion.application.annotation.LogBusinessOperation annotation = 
                method.getAnnotation(com.poly.promotion.application.annotation.LogBusinessOperation.class);

            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = method.getName();
            String operation = annotation.value();
            String category = annotation.category();
            
            // Log operation start
            if (annotation.logContext()) {
                String context = formatParameters(joinPoint.getArgs());
                log.info("🚀 Starting business operation: {} | Category: {} | Method: {}.{} | Context: {}", 
                    operation, category, className, methodName, context);
            } else {
                log.info("🚀 Starting business operation: {} | Category: {} | Method: {}.{}", 
                    operation, category, className, methodName);
            }
            
            Object result = joinPoint.proceed();
            stopWatch.stop();
            
            // Log operation completion
            if (annotation.logResult()) {
                String resultInfo = formatResult(result);
                log.info("✅ Business operation completed: {} | Category: {} | Method: {}.{} | Result: {} | Duration: {}ms", 
                    operation, category, className, methodName, resultInfo, stopWatch.getTotalTimeMillis());
            } else {
                log.info("✅ Business operation completed: {} | Category: {} | Method: {}.{} | Duration: {}ms", 
                    operation, category, className, methodName, stopWatch.getTotalTimeMillis());
            }
            
            return result;
            
        } catch (Throwable throwable) {
            stopWatch.stop();
            
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            com.poly.promotion.application.annotation.LogBusinessOperation annotation = 
                method.getAnnotation(com.poly.promotion.application.annotation.LogBusinessOperation.class);
            
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = method.getName();
            String operation = annotation.value();
            String category = annotation.category();
            
            log.error("❌ Business operation failed: {} | Category: {} | Method: {}.{} | Duration: {}ms | Error: {}", 
                operation, category, className, methodName, stopWatch.getTotalTimeMillis(), throwable.getMessage());
            
            throw throwable;
        }
    }

    /**
     * Helper method to log method exit details.
     *
     * @param joinPoint the join point
     * @param result the method result
     * @param executionTime the execution time in milliseconds
     * @param throwable the exception if any
     */
    private void logMethodExitDetails(ProceedingJoinPoint joinPoint, Object result, long executionTime, Throwable throwable) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            com.poly.promotion.application.annotation.LogMethodExit annotation = 
                method.getAnnotation(com.poly.promotion.application.annotation.LogMethodExit.class);

            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = method.getName();
            
            StringBuilder logMessage = new StringBuilder();
            
            if (annotation.logMethodName()) {
                logMessage.append("✅ Exiting method: ").append(className).append(".").append(methodName);
            }
            
            if (annotation.logExecutionTime()) {
                logMessage.append(" | Execution time: ").append(executionTime).append("ms");
            }
            
            if (annotation.logReturnValue() && result != null) {
                String resultInfo = formatResult(result);
                logMessage.append(" | Return value: ").append(resultInfo);
            }
            
            if (throwable != null) {
                logMessage.append(" | Exception: ").append(throwable.getMessage());
            }
            
            if (annotation.value().isEmpty()) {
                log.info(logMessage.toString());
            } else {
                log.info("{} | {}", annotation.value(), logMessage.toString());
            }
            
        } catch (Exception e) {
            log.warn("Failed to log method exit: {}", e.getMessage());
        }
    }

    /**
     * Formats method parameters for logging.
     *
     * @param args the method arguments
     * @return formatted string representation of parameters
     */
    private String formatParameters(Object[] args) {
        if (args == null || args.length == 0) {
            return "none";
        }
        
        return Arrays.stream(args)
                .map(this::formatParameter)
                .collect(Collectors.joining(", "));
    }

    /**
     * Formats a single parameter for logging.
     *
     * @param param the parameter to format
     * @return formatted string representation of the parameter
     */
    private String formatParameter(Object param) {
        if (param == null) {
            return "null";
        }
        
        // Detect AOP proxies to prevent recursive serialization
        if (AopUtils.isAopProxy(param)) {
            return "AOP_PROXY[" + param.getClass().getSimpleName() + "]";
        }
        
        try {
            if (param instanceof String || param instanceof Number || param instanceof Boolean) {
                return param.toString();
            }
            
            // For complex objects, try to serialize to JSON
            return objectMapper.writeValueAsString(param);
        } catch (JsonProcessingException e) {
            // Fallback to toString if JSON serialization fails
            return param.toString();
        }
    }

    /**
     * Formats method result for logging.
     *
     * @param result the method result
     * @return formatted string representation of the result
     */
    private String formatResult(Object result) {
        if (result == null) {
            return "null";
        }
        
        // Detect AOP proxies to prevent recursive serialization
        if (AopUtils.isAopProxy(result)) {
            return "AOP_PROXY[" + result.getClass().getSimpleName() + "]";
        }
        
        try {
            if (result instanceof String || result instanceof Number || result instanceof Boolean) {
                return result.toString();
            }
            
            // For collections, show size
            if (result instanceof java.util.Collection) {
                return String.format("Collection[%d items]", ((java.util.Collection<?>) result).size());
            }
            
            if (result.getClass().isArray()) {
                return String.format("Array[%d items]", java.lang.reflect.Array.getLength(result));
            }
            
            // For complex objects, try to serialize to JSON
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            // Fallback to toString if JSON serialization fails
            return result.toString();
        }
    }
}
