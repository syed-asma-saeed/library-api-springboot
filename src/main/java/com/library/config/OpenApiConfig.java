package com.library.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI libraryOpenAPI() {
        return new OpenAPI()

                // Page header information
                .info(new Info()
                        .title("Library Management System API")
                        .description("""
                    REST API for managing books, members, and borrowing operations.
                    
                    Authentication:
                    1. Register via POST /api/auth/register
                    2. Login via POST /api/auth/login to get JWT token
                    3. Click 'Authorize' button and enter: Bearer {your_token}
                    4. All protected endpoints will now work
                    """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Asma")
                                .email("syedasmasaeed22@gmail.com")))

                // Tells Swagger: every endpoint requires this security scheme
                .addSecurityItem(
                        new SecurityRequirement().addList("Bearer Authentication"))

                // Defines what "Bearer Authentication" means
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .name("Bearer Authentication")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhc21hQHRlc3QuY29tIiwiaWF0IjoxNzg2NTYyMzMxLCJleHAiOjE3ODY2NDg3MzF9.h-zLxSC6bpShkRznu0opsHdG7YC8efSevG7U1T0n9is96d56ExwyngVwRcYo7meA")));
    }
}