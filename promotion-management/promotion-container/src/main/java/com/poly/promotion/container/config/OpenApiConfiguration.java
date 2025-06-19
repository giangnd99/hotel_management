package com.poly.promotion.container.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8088/promotion-management");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("LinhPete");
        myContact.setEmail("peterdamlinh1215@gmail.com");

        Info information = new Info()
                .title("Promotion Management System API")
                .version("1.0")
                .description("This API exposes endpoints to manage promotions.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }

}
