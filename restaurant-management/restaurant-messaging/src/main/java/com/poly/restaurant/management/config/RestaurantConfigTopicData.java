package com.poly.restaurant.management.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "restaurant-service")
public class RestaurantConfigTopicData {

    private String restaurantPaymentRequestTopicName;
    private String restaurantPaymentResponseTopicName;
    private String bookedRoomRequestTopicName;
    private String bookedRoomResponseTopicName;
}
