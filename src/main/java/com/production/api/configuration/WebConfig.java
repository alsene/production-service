package com.production.api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String allowedOrigins="https://sentechno-dev.com, https://www.sentechno-dev.com, https://www.sentechno-dev.com/login, https://api.sentechno-dev.com"; // Valeurs par défaut

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .toArray(String[]::new);

        registry.addMapping("/api/**") // Applique à tous les endpoints
                .allowedOrigins(allowedOrigins) // URL de votre app Angular //Access-Control-Allow-Origin: https://example.com, http://localhost:4200, Header set Access-Control-Allow-Origin 'origin-list'
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}