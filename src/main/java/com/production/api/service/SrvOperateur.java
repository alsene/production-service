package com.production.api.service;

import com.production.api.model.Operateur;
import com.production.api.repository.OperateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SrvOperateur {
    private final OperateurRepository OperateurRepository;
    
    public Mono<Operateur> getOperateur(){
        Operateur Operateur = new Operateur();
        Operateur.setNom("Operateur Test2");
        log.info("Returning Operateur: {}", Operateur);
        return Mono.just(Operateur);
    }

    /**
     * Save a product to the database
     */
    public Mono<Operateur> saveOperateur(Operateur Operateur) {
        return Mono.fromCallable(() -> {
            log.info("Saving Operateur to database: {}", Operateur);
            Operateur saved = OperateurRepository.save(Operateur);
            log.info("Operateur saved with id: {}", saved.getId());
            return saved;
        });
    }

    /**
     * Get product by ID from database
     */
    public Mono<Operateur>  getOperateurById(Long id) {
        return Mono.fromCallable(() -> {
            log.info("Fetching Operateur with id: {}", id);
            return OperateurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Operateur not found with id: " + id));
        });
    }

    /**
     * Get all products from database
     */
    public Mono<List<Operateur>> getAllOperateurs() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all Operateurs from database");
            return OperateurRepository.findAll();
        });
    }
}