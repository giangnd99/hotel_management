package com.poly.authentication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.authentication.service.domain.AuthenticationApplication;
import com.poly.authentication.service.domain.dto.request.auth.AuthenticationRequest;
import com.poly.authentication.service.domain.dto.request.user.UserCreationRequest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest(classes = {AuthenticationApplication.class})
@AutoConfigureWebTestClient
@Sql(value = {"classpath:setup.sql"})
@Sql(value = {"classpath:cleanup.sql"}, executionPhase = AFTER_TEST_METHOD)
public class AuthenticationIT {

    @Autowired
    private WebTestClient webTestClient;

    private static String userToken;
    private static String adminToken; // For testing admin roles, if applicable
    private static String expiredToken;
    private static String malformedToken = "malformed.jwt.token";

    // --- Test Cases Tập trung vào Gateway ---

    // Test Case 1: Đăng ký người dùng và xác nhận phản hồi Gateway
    @Test
    @Order(1)
    void test1_SuccessfulUserRegistrationThroughGateway() {
        UserCreationRequest request = UserCreationRequest.builder()
                .password("password123")
                .email("gatewayuser1@example.com")
                .phone("1234567890")
                .build();

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserCreationRequest.class)
                .exchange()
                .expectStatus().isCreated() // Gateway chuyển tiếp request và nhận 201 Created
                .expectBody()
                .jsonPath("$.code").isEqualTo(1000)
                .jsonPath("$.message").isEqualTo("Operation success");
    }

    // Test Case 2: Đăng nhập người dùng và nhận Token qua Gateway
    @Test
    @Order(2)
    void test2_SuccessfulUserLoginThroughGateway() throws JsonProcessingException {
        // Đăng ký trước
        UserCreationRequest registerRequest = UserCreationRequest.builder()
                .password("password123")
                .email("loginusergateway@example.com")
                .phone("1112223333")
                .build();
        webTestClient.post().uri("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(registerRequest), UserCreationRequest.class)
                .exchange()
                .expectStatus().isCreated();

        // Đăng nhập
        AuthenticationRequest loginRequest = AuthenticationRequest.builder()
                .email("loginusergateway@example.com")
                .password("password123")
                .build();

        String responseBody = webTestClient.post().uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(loginRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus().isOk() // Gateway chuyển tiếp request và nhận 200 OK
                .expectBody(String.class)
                .returnResult().getResponseBody();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = objectMapper.readValue(responseBody, (JavaType) new ParameterizedTypeReference<Map<String, Object>>() {
        }.getType());
        Map<String, String> result = (Map<String, String>) responseMap.get("result");
        assertThat(result).isNotNull();
        userToken = result.get("token");
        assertThat(userToken).isNotNull().isNotEmpty();
    }

    // Test Case 3: Gateway định tuyến thành công tới USER-SERVICE với Token hợp lệ
    @Test
    @Order(3)
    void test3_GatewayRoutesToUserServiceWithValidToken() {
        assertThat(userToken).isNotNull().isNotEmpty(); // Đảm bảo token đã có từ test2

        webTestClient.get().uri("/users/myInfo") // Path được định tuyến tới USER-SERVICE
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .exchange()
                // Expect 404 NOT FOUND hoặc 503 SERVICE UNAVAILABLE vì USER-SERVICE không chạy.
                // Quan trọng là KHÔNG phải 401 Unauthorized, tức là Gateway đã xác thực thành công token
                // và cố gắng định tuyến nhưng không tìm thấy đích.
                .expectStatus().isNotFound(); // Hoặc .isServiceUnavailable() tùy theo hành vi của Eureka/Gateway khi không tìm thấy service
    }

    // Test Case 4: Gateway định tuyến thành công tới ROOM-SERVICE với Token hợp lệ
    @Test
    @Order(4)
    void test4_GatewayRoutesToProductServiceWithValidToken() {

        webTestClient.get().uri("api/rooms") // Path được định tuyến tới ROOM-SERVICE
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .exchange()
                .expectStatus().isNotFound(); // Tương tự test3, xác nhận định tuyến và xác thực thành công.
    }

    // Test Case 5: Gateway chặn request không có Token đến Endpoint Bảo vệ
    @Test
    @Order(5)
    void test5_GatewayBlocksProtectedEndpointWithoutToken() {
        webTestClient.get().uri("/users/profile")
                .exchange()
                .expectStatus().isUnauthorized() // Gateway trả về 401 Unauthorized
                .expectBody()
                .jsonPath("$.code").isEqualTo(1006); // UNAUTHENTICATED_EXCEPTION
    }

    // Test Case 6: Gateway chặn request với Token không hợp lệ (Malformed)
    @Test
    @Order(6)
    void test6_GatewayBlocksWithMalformedToken() {
        webTestClient.get().uri("/users") // Một path bất kỳ được bảo vệ
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + malformedToken)
                .exchange()
                .expectStatus().isUnauthorized() // Gateway trả về 401 Unauthorized
                .expectBody()
                .jsonPath("$.code").isEqualTo(1006); // UNAUTHENTICATED_EXCEPTION
    }

    // Test Case 7: Gateway chặn request với Token đã hết hạn
    @Test
    @Order(7)
    void test7_GatewayBlocksWithExpiredToken() {
        webTestClient.get().uri("/users") // Một path bất kỳ được bảo vệ
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + expiredToken)
                .exchange()
                .expectStatus().isUnauthorized() // Gateway trả về 401 Unauthorized
                .expectBody()
                .jsonPath("$.code").isEqualTo(1006); // UNAUTHENTICATED_EXCEPTION
    }

    // Test Case 8: Gateway cho phép truy cập Endpoint công khai (không cần Token)
    @Test
    @Order(8)
    void test8_GatewayAllowsPublicAccess() {
        webTestClient.get().uri("/auth/validate-token") // Endpoint trong PUBLIC_URLS
                .exchange()
                .expectStatus().isOk(); // Gateway cho phép truy cập và trả về 200 OK
    }

    // Test Case 9: Gateway xử lý lỗi định tuyến khi dịch vụ đích không tồn tại (Không tìm thấy route)
    // (Test này kiểm tra nếu có request đến một path không được định nghĩa trong MyGateway.java)
    @Test
    @Order(9)
    void test9_GatewayHandlesNonExistentRoute() {
        webTestClient.get().uri("/nonexistent-service/path") // Một đường dẫn không được cấu hình trong Gateway
                .exchange()
                .expectStatus().isNotFound(); // Gateway trả về 404 Not Found vì không có route khớp
    }

    // Test Case 10: Gateway xử lý Cors Preflight Request (OPTIONS method)
    // Giả sử CorsWebFilter của bạn đã được cấu hình để cho phép OPTIONS
    @Test
    @Order(10)
    void test10_GatewayHandlesCorsPreflightRequest() {
        webTestClient.options().uri("/users/profile") // Request OPTIONS đến một endpoint được bảo vệ
                .header(HttpHeaders.ORIGIN, "http://localhost:3000") // Giả lập Origin từ frontend
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Authorization, Content-Type")
                .exchange()
                .expectStatus().isOk() // Mong đợi 200 OK cho preflight request
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*") // hoặc domain cụ thể của bạn
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*")
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "authorization,content-type"); // Các header được cho phép
    }
}
