package com.dvh.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI dvhOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("DVH API")
                        .description("REST API for Dhole Vehicle Hub")
                        .version("v1"));
    }
}
