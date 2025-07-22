package com.poly.authentication.service.domain.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.RouteMatcher;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class MyGateway {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Định tuyến đến các dịch vụ bên ngoài.
                // SecurityConfig đã xử lý xác thực cho mọi request đến gateway.
                // Nếu request được xác thực thành công bởi SecurityConfig, nó sẽ được định tuyến.
                // Nếu không, SecurityConfig sẽ từ chối request trước khi nó đến được RouteLocator.

                // Route cho user-service
                .route("user-service", r -> r.path("/users/**")
                        .uri("lb://AUTHENTICATION-SERVICE"))

                // Route cho product-service
                .route("room-management", r -> r.path("/api/rooms/**")
                        .uri("lb://ROOM-MANAGEMENT"))

                // Route cho hotel-service
                .route("hotel-service", r -> r.path("/hotels/**", "/rooms/**", "/bookings/**")
                        .uri("lb://HOTEL-SERVICE"))

                // Thêm các route khác của bạn tại đây
                // Ví dụ: .route("payment-service", r -> r.path("/payment/**").uri("lb://PAYMENT-SERVICE"))

                .build();
    }
}
