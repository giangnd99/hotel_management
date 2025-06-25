package com.poly.authentication.service.domain.config;

import com.poly.authentication.service.domain.service.AuthDomainService;
import com.poly.authentication.service.domain.service.impl.AuthDomainServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomJwtDecoder jwtDecoder;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomJwtConverter jwtConverter;


    // Danh sách các đường dẫn công khai (không cần xác thực)
    private final String[] PUBLIC_URLS = {
            "/auth/login",
            "/auth/register",
            "/auth/validate-token", // Endpoint validation được gọi nội bộ
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/eureka/**",
            "/actuator/**",
            // Các đường dẫn public từ các service khác
            "/products", "/products/**",
            "/categories", "/categories/**",
            "/reviews", "/reviews/**",
            "/payment/vn-pay-callback", "/payment/vn-pay-callback/**",
            "/reset-password", "/reset-password/**",
            // "/users", // Nếu bạn muốn /users là public (cẩn thận với bảo mật)
            // Nếu có các phương thức HTTP cụ thể cho public URLs, bạn có thể thêm:
            // HttpMethod.GET + "/some-public-read-only-endpoint",
            // "/some-other-public-path"
    };

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Vô hiệu hóa CSRF cho API (phổ biến trong REST API)
                .cors(Customizer.withDefaults()) // Kích hoạt CORS với cấu hình CorsWebFilter
                .exceptionHandling(exceptionHandlingSpec ->
                        exceptionHandlingSpec.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // Xử lý lỗi xác thực
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec
                                .pathMatchers(PUBLIC_URLS).permitAll() // Cho phép truy cập công khai các URL này
                                .anyExchange().authenticated() // Tất cả các request khác đều yêu cầu xác thực
                )
                .oauth2ResourceServer(oauth2ResourceServerSpec ->
                        oauth2ResourceServerSpec
                                .jwt(jwtSpec -> jwtSpec.jwtDecoder(jwtDecoder) // Sử dụng CustomJwtDecoder của bạn
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter())) // Chuyển đổi JWT thành Authentication
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Xử lý lỗi xác thực liên quan đến JWT
                )
        ;

        return http.build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() { // Thay đổi kiểu trả về
        // ReactiveJwtAuthenticationConverter là một Converter<Jwt, Mono<AbstractAuthenticationToken>>
        // mà Spring Security WebFlux mong đợi.
        // Nó có thể được khởi tạo với một JwtGrantedAuthoritiesConverter (như CustomJwtConverter của bạn).
        ReactiveJwtGrantedAuthoritiesConverterAdapter grantedAuthoritiesConverter = new ReactiveJwtGrantedAuthoritiesConverterAdapter(jwtConverter);

        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        // Gán CustomJwtConverter của bạn vào ReactiveJwtAuthenticationConverter
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return converter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*"); // PRODUCTION: Thay đổi thành domain thực tế của Frontend
        corsConfig.addAllowedMethod("*"); // Cho phép tất cả các phương thức HTTP (GET, POST, PUT, DELETE, etc.)
        corsConfig.addAllowedHeader("*"); // Cho phép tất cả các header, bao gồm Authorization
        corsConfig.setAllowCredentials(true); // Cho phép gửi cookie, header xác thực
        corsConfig.setMaxAge(3600L); // Thời gian pre-flight request được cache (giây)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Áp dụng cấu hình CORS cho tất cả các đường dẫn
        return new CorsWebFilter(source);
    }

}
