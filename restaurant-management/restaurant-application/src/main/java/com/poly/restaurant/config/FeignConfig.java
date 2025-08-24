package com.poly.restaurant.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    /**
     * Custom error decoder for better error handling
     */
    public static class CustomErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultErrorDecoder = new Default();

        @Override
        public Exception decode(String methodKey, feign.Response response) {
            log.error("Feign client error - Method: {}, Status: {}, Reason: {}",
                methodKey, response.status(), response.reason());

            if (response.status() >= 400 && response.status() <= 499) {
                return new RuntimeException("Client error: " + response.status() + " " + response.reason());
            }
            if (response.status() >= 500 && response.status() <= 599) {
                return new RuntimeException("Server error: " + response.status() + " " + response.reason());
            }

            return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}
