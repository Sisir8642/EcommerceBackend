package com.projectt.ecomProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())  // Apply CORS based on your CorsConfig bean
                .csrf(csrf -> csrf.disable()) // Disable CSRF (not recommended for production unless necessary)
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()  // Allow all requests (you can customize this)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(ALWAYS) // You can change this based on your requirements
                );
        return http.build();
    }
}
