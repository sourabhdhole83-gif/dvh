package com.dvh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){

        httpSecurity.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/users",
                                "/api/v1/users/{id}",
                                "/swagger-ui.html",      // Swagger ka main page
                                "/swagger-ui/**",        // uski CSS/JS files
                                "/v3/api-docs/**"        // raw OpenAPI JSON
                        ).permitAll()
                        .anyRequest().authenticated()    // Baaki sabhi endpoints ke liye auth required (ya dev mein .permitAll())
                );
        return httpSecurity.build();
    }
}
