package edu.poly.servicemanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import springfox.documentation.swagger2.annotations.EnableSwagger2; // Xóa dòng này

@Configuration
// @EnableSwagger2 // Xóa chú thích này
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Management API")
                        .version("1.0")
                        .description("API documentation.")
                        .termsOfService("http://swagger.io/terms/")
                        .contact(new Contact().name("Your Name/Team").email("tai30799@gmail.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
        