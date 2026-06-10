package com.production.api.service;

import com.production.api.model.Silo;
import com.production.api.repository.SiloRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SrvSilo {
    private final SiloRepository SiloRepository;
    
    public Mono<Silo> getSilo(){
        Silo Silo = new Silo();
        log.info("Returning Silo: {}", Silo);
        return Mono.just(Silo);
    }

    /**
     * Save a product to the database
     */
    public Mono<Silo> saveSilo(Silo Silo) {
        return Mono.fromCallable(() -> {
            log.info("Saving Silo to database: {}", Silo);
            Silo saved = SiloRepository.save(Silo);
            log.info("Silo saved with id: {}", saved.getId());
            return saved;
        });
    }

    /**
     * Get product by ID from database
     */
    public Mono<Silo>  getSiloById(Long id) {
        return Mono.fromCallable(() -> {
            log.info("Fetching Silo with id: {}", id);
            return SiloRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Silo not found with id: " + id));
        });
    }

    /**
     * Get all products from database
     */
    public Mono<List<Silo>> getAllSilos() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all Silos from database");
            return SiloRepository.findAll();
        });
    }
}