package com.poly.booking.management.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "booking-service")
public class BookingServiceConfigData {

    private String paymentRequestTopicName;
    private String paymentResponseTopicName;
    private String roomReserveRequestTopicName;
    private String roomReserveResponseTopicName;
    private String roomCheckInTopicName;
    private String bookingNotificationRequestTopicName;
}
