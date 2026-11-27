package com.dvh.auth.config;

import com.dvh.auth.security.CustomUserDetailsService;
import com.dvh.auth.security.JwtAuthenticationFilter;
import com.dvh.auth.security.RestAccessDeniedHandler;
import com.dvh.auth.security.RestAuthenticationEntryPoint;
import com.dvh.auth.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   JwtService jwtService,
                                                   CustomUserDetailsService userDetailsService,
                                                   RestAccessDeniedHandler accessDeniedhandler,
                                                   RestAuthenticationEntryPoint entryPoint){


        httpSecurity.csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // server keeps no login session, the token is the proof
                .exceptionHandling(e -> e.authenticationEntryPoint(entryPoint)                     // plug in our JSON 401
                        .accessDeniedHandler(accessDeniedhandler))                // plug in our JSON 403
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login",
                                "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .anyRequest().authenticated())                                             // everything else needs a valid token
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, userDetailsService),      // our filter runs before Spring's own login filter
                        UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();     }
}
