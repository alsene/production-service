package com.production.api.client;

import com.production.api.model.Produit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
@Slf4j
public class ClientProduit {
    private  final WebClient webClient;

    @Value("${client.service.production.endpoint}")
    private  String endpoint;

    // Explicit constructor so Spring can inject the WebClient bean
    public ClientProduit(@Qualifier("webClient") WebClient webClient) {
        this.webClient = webClient;
    }


    public Mono<Produit> getAllProduit() {
        Produit produit = new Produit();
        produit.setNom("Produit Test");
        produit.setQuantite(BigDecimal.valueOf(100));
        log.info("ClientProduit.getAllProduit called, returning test product: {}", produit);
        Mono<Produit> testMono = Mono.just(produit);
        log.info("Test Mono created: {}", testMono);
        return webClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(Produit.class);
    }
}