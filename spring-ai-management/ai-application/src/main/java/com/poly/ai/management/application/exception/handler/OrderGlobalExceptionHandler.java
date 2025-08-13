package com.poly.ai.management.application.exception.handler;


import com.poly.ai.management.domain.exception.AiDomainException;
import com.poly.domain.dto.ApiResponse;
import com.poly.domain.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class OrderGlobalExceptionHandler extends GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {AiDomainException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<String> handleException(AiDomainException orderDomainException) {
        log.error(orderDomainException.getMessage(), orderDomainException);
        return ApiResponse.<String>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(orderDomainException.getMessage())
                .build();
    }
//
//    @ResponseBody
//    @ExceptionHandler(value = {OrderNotFoundException.class})
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorDTO handleException(OrderNotFoundException orderNotFoundException) {
//        log.error(orderNotFoundException.getMessage(), orderNotFoundException);
//        return ErrorDTO.builder()
//                .code(HttpStatus.NOT_FOUND.getReasonPhrase())
//                .message(orderNotFoundException.getMessage())
//                .build();
//    }
}
