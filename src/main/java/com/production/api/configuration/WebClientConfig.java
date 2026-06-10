package com.production.api.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class WebClientConfig {

	@Value("${client.service.url}")
	private String url;

	@Bean(name = "webClient")
	public WebClient webClient(WebClient.Builder builder) {
		log.info("Creating WebClient with baseUrl={}", url);
		return builder
				.baseUrl(url)
				.defaultCookie("cookie-name", "cookie-value")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	// Provide a WebClient.Builder bean in case auto-configuration does not register one
	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}
}