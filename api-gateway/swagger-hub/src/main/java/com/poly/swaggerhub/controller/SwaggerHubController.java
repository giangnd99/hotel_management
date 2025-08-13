package com.poly.swaggerhub.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api-docs")
@CrossOrigin(origins = "*")
public class SwaggerHubController {

    @Autowired
    private WebClient webClient;

    @Autowired
    private Environment environment;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/swagger-config")
    public ResponseEntity<Map<String, Object>> getSwaggerConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("urls", getServiceUrls());
        return ResponseEntity.ok(config);
    }

    @GetMapping("/inventory")
    public Mono<ResponseEntity<String>> getInventoryDocs() {
        return getServiceDocs("services.swagger.inventory.url", "services.swagger.inventory.docs-path");
    }

    @GetMapping("/restaurant")
    public Mono<ResponseEntity<String>> getRestaurantDocs() {
        return getServiceDocs("services.swagger.restaurant.url", "services.swagger.restaurant.docs-path");
    }

    @GetMapping("/staff")
    public Mono<ResponseEntity<String>> getStaffDocs() {
        return getServiceDocs("services.swagger.staff.url", "services.swagger.staff.docs-path");
    }

    @GetMapping("/booking")
    public Mono<ResponseEntity<String>> getBookingDocs() {
        return getServiceDocs("services.swagger.booking.url", "services.swagger.booking.docs-path");
    }

    @GetMapping("/room")
    public Mono<ResponseEntity<String>> getRoomDocs() {
        return getServiceDocs("services.swagger.room.url", "services.swagger.room.docs-path");
    }

    @GetMapping("/spring-ai")
    public Mono<ResponseEntity<String>> getSpringAiDocs() {
        return getServiceDocs("services.swagger.spring-ai.url", "services.swagger.spring-ai.docs-path");
    }

    @GetMapping("/service-management")
    public Mono<ResponseEntity<String>> getServiceManagementDocs() {
        return getServiceDocs("services.swagger.service-management.url", "services.swagger.service-management.docs-path");
    }

    @GetMapping("/notification")
    public Mono<ResponseEntity<String>> getNotificationDocs() {
        return getServiceDocs("services.swagger.notification.url", "services.swagger.notification.docs-path");
    }

    @GetMapping("/payment")
    public Mono<ResponseEntity<String>> getPaymentDocs() {
        return getServiceDocs("services.swagger.payment.url", "services.swagger.payment.docs-path");
    }

    @GetMapping("/customer")
    public Mono<ResponseEntity<String>> getCustomerDocs() {
        return getServiceDocs("services.swagger.customer.url", "services.swagger.customer.docs-path");
    }

    @GetMapping("/reporting")
    public Mono<ResponseEntity<String>> getReportingDocs() {
        return getServiceDocs("services.swagger.reporting.url", "services.swagger.reporting.docs-path");
    }

    private Mono<ResponseEntity<String>> getServiceDocs(String urlProperty, String pathProperty) {
        String baseUrl = environment.getProperty(urlProperty);
        String docsPath = environment.getProperty(pathProperty);
        
        if (baseUrl == null || docsPath == null) {
            return Mono.just(ResponseEntity.notFound().build());
        }

        String fullUrl = baseUrl + docsPath;
        
        return webClient.get()
                .uri(fullUrl)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok(response))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    private Map<String, Object>[] getServiceUrls() {
        Map<String, Object>[] urls = new Map[11];
        
        urls[0] = createUrlMap("/api-docs/inventory", "Inventory Service");
        urls[1] = createUrlMap("/api-docs/restaurant", "Restaurant Service");
        urls[2] = createUrlMap("/api-docs/booking", "Booking Management");
        urls[3] = createUrlMap("/api-docs/room", "Room Management");
        urls[4] = createUrlMap("/api-docs/spring-ai", "Spring AI Management");
        urls[5] = createUrlMap("/api-docs/staff", "Staff Management");
        urls[6] = createUrlMap("/api-docs/service-management", "Service Management");
        urls[7] = createUrlMap("/api-docs/notification", "Notification Management");
        urls[8] = createUrlMap("/api-docs/payment", "Payment Management");
        urls[9] = createUrlMap("/api-docs/customer", "Customer Management");
        urls[10] = createUrlMap("/api-docs/reporting", "Reporting Management");
        
        return urls;
    }

    private Map<String, Object> createUrlMap(String url, String name) {
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("url", url);
        urlMap.put("name", name);
        return urlMap;
    }
} 