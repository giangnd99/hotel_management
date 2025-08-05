package com.poly.paymentdataaccess.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Slf4j
@Configuration
public class PayOSConfig {

    @Value("${payos.clientId}")
    protected String clientId;

    @Value("${payos.apiKey}")
    protected String apiKey;

    @Value("${payos.checksumKey}")
    protected String checksumKey;

    @Bean
    public PayOS payOS() {
        log.info("PayOS config bean has been initialized" + apiKey);
        log.info("PayOS config bean has been initialized" + checksumKey);
        log.info("PayOS config bean has been initialized" + clientId);
        return new PayOS(clientId, apiKey, checksumKey);
    }
}
