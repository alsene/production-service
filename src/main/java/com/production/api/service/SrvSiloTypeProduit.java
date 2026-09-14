package com.production.api.service;

import com.production.api.model.SiloTypeProduit;
import com.production.api.repository.SiloTypeProduitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SrvSiloTypeProduit {
    private final SiloTypeProduitRepository siloTypeProduitRepository;

    public Mono<SiloTypeProduit> getSiloTypeProduit() {
        SiloTypeProduit siloTypeProduit = new SiloTypeProduit();
        log.info("Returning SiloTypeProduit: {}", siloTypeProduit);
        return Mono.just(siloTypeProduit);
    }

    public Mono<SiloTypeProduit> saveSiloTypeProduit(SiloTypeProduit siloTypeProduit) {
        return Mono.fromCallable(() -> {
            log.info("Saving SiloTypeProduit to database: {}", siloTypeProduit);
            SiloTypeProduit saved = siloTypeProduitRepository.save(siloTypeProduit);
            log.info("SiloTypeProduit saved with id: {}", saved.getId());
            return saved;
        });
    }

    public Mono<SiloTypeProduit> modifierSiloTypeProduit(SiloTypeProduit siloTypeProduit) {
        return Mono.fromCallable(() -> {
            if (siloTypeProduit.getId() == null) {
                throw new IllegalArgumentException("L'id du siloTypeProduit est requis pour la modification");
            }

            SiloTypeProduit existing = siloTypeProduitRepository.findById(siloTypeProduit.getId())
                    .orElseThrow(() -> new IllegalArgumentException("SiloTypeProduit not found with id: " + siloTypeProduit.getId()));

            existing.setCodeProduit(siloTypeProduit.getCodeProduit());
            existing.setTypeProduit(siloTypeProduit.getTypeProduit());
            existing.setSilo(siloTypeProduit.getSilo());

            return siloTypeProduitRepository.save(existing);
        });
    }

    public Mono<Void> supprimerSiloTypeProduit(Long id) {
        return Mono.fromCallable(() -> {
            SiloTypeProduit existing = siloTypeProduitRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("SiloTypeProduit not found with id: " + id));
            siloTypeProduitRepository.deleteById(existing.getId());
            return null;
        });
    }

    public Mono<SiloTypeProduit> getSiloTypeProduitById(Long id) {
        return Mono.fromCallable(() -> {
            log.info("Fetching SiloTypeProduit with id: {}", id);
            return siloTypeProduitRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("SiloTypeProduit not found with id: " + id));
        });
    }

    public Mono<List<SiloTypeProduit>> getAllSiloTypeProduits() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all SiloTypeProduits from database");
            return siloTypeProduitRepository.findAll();
        });
    }
}

