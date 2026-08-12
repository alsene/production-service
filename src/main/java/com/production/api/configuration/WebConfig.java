package com.production.api.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

//    private final String allowedOrigins="http://localhost:4200, https://sentechno-dev.com, https://www.sentechno-dev.com, https://www.sentechno-dev.com/login, https://api.sentechno-dev.com"; // Valeurs par défaut
    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    public WebConfig(String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .toArray(String[]::new);

        registry.addMapping("/api/**") // Applique à tous les endpoints
                .allowedOrigins(origins) // URL de votre app Angular //Access-Control-Allow-Origin: https://example.com, http://localhost:4200, Header set Access-Control-Allow-Origin 'origin-list'
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}