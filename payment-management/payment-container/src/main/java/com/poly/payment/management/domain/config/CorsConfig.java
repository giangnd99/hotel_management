package com.poly.payment.management.domain.config;

//@Configuration
//public class CorsConfig {
//
////    @Value("${cors.allowed-origins}")
//    private String apiFrontend = "http://localhost:3000";
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins(apiFrontend)
//                        .allowedMethods("GET", "POST", "PUT", "DELETE")
//                        .allowedHeaders("*")
//                        .allowCredentials(true);
//            }
//        };
//    }
//}