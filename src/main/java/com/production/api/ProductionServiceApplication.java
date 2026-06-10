package com.production.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Hooks;

@EnableConfigurationProperties
@SpringBootApplication

public class ProductionServiceApplication {

	public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
        SpringApplication.run(ProductionServiceApplication.class, args);
	}

}
