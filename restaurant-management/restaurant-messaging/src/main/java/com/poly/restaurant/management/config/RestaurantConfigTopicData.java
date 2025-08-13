package com.poly.restaurant.management.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class cho Kafka topics của restaurant service
 * 
 * NGHIỆP VỤ:
 * - Quản lý tên các Kafka topics
 * - Cấu hình từ application properties
 * 
 * PATTERNS ÁP DỤNG:
 * - Configuration Pattern: Tập trung cấu hình
 * - Properties Pattern: External configuration
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "restaurant-service")
public class RestaurantConfigTopicData {

    // Payment topics
    private String restaurantPaymentRequestTopicName;
    private String restaurantPaymentResponseTopicName;
    
    // Room order topics
    private String restaurantRoomOrderRequestTopicName;
    private String restaurantRoomOrderResponseTopicName;
    
    // Legacy room topics (có thể deprecated)
    private String bookedRoomRequestTopicName;
    private String bookedRoomResponseTopicName;
}
