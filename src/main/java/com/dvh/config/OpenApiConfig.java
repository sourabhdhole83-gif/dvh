package com.dvh.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // Spring ko batata hai: is class mein @Bean methods hain, startup pe padh lo
public class OpenApiConfig {

    @Bean  // yeh object Spring ke container mein register hoga, springdoc ise utha lega
    public OpenAPI dvhOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DVH - Dhole Vehicle Hub API")      // Swagger page ke top pe dikhega
                        .description("Self-drive vehicle rental platform APIs")
                        .version("v1"));                            // API ka version
    }
}