package com.ticketbooking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookMyTicketOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book My Ticket API")
                        .description("Movie & Show Ticket Booking System")
                        .version("1.0.0"));
    }
}
