package com.production.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Applique à tous les endpoints
                .allowedOrigins("http://localhost:4200") // URL de votre app Angular //Access-Control-Allow-Origin: https://example.com, http://localhost:4200, Header set Access-Control-Allow-Origin 'origin-list'
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}