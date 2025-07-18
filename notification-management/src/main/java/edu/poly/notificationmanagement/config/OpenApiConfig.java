package edu.poly.notificationmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Management Notification API")
                        .version("1.0")
                        .description("API documentation for the Notification Management service of the Hotel Management system.")
                        .termsOfService("http://swagger.io/terms/")
                        .contact(new Contact().name("Your Name/Team").email("tai30799@gmail.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
