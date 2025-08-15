package com.poly.kafka.config.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka-topics-config")
public class KafkaTopicsConfig {

    public static final String PAYMENT_REQUEST_TOPIC = "payment-request";
    public static final String PAYMENT_RESPONSE_TOPIC = "payment-response";

    public static final String ROOM_APPROVAL_REQUEST_TOPIC = "room-approval-request";
    public static final String ROOM_APPROVAL_RESPONSE_TOPIC = "room-approval-response";

    public static final String CUSTOMER_REQUEST_TOPIC = "customer-request";
    public static final String CUSTOMER_RESPONSE_TOPIC = "customer-response";

    public static final String BOOKING_REQUEST_TOPIC = "booking-request";
    public static final String BOOKING_RESPONSE_TOPIC = "booking-response";

    public static final String NOTIFICATION_REQUEST_TOPIC = "notification-request";
    public static final String NOTIFICATION_RESPONSE_TOPIC = "notification-response";

    public static final String RESTAURANT_REQUEST_TOPIC = "restaurant-request";
    public static final String RESTAURANT_RESPONSE_TOPIC = "restaurant-response";

    public static final String SERVICE_REQUEST_TOPIC = "service-request";
    public static final String SERVICE_RESPONSE_TOPIC = "service-response";

    public static final String AI_REQUEST_TOPIC = "ai-request";
    public static final String AI_RESPONSE_TOPIC = "ai-response";

    public static final String INVENTORY_REQUEST_TOPIC = "inventory-request";
    public static final String INVENTORY_RESPONSE_TOPIC = "inventory-response";
}
