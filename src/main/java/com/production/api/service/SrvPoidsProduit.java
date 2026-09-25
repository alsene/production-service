package com.production.api.service;

import com.production.api.model.PoidsProduit;
import com.production.api.repository.PoidsProduitRepository;
import com.production.api.util.EnrichirLibelle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SrvPoidsProduit {
    private final PoidsProduitRepository poidsProduitRepository;

    public Mono<PoidsProduit> getPoidsProduit() {
        PoidsProduit poidsProduit = new PoidsProduit();
        log.info("Returning PoidsProduit: {}", poidsProduit);
        return Mono.just(poidsProduit);
    }

    public Mono<PoidsProduit> savePoidsProduit(PoidsProduit poidsProduit) {
        return Mono.fromCallable(() -> {
            log.info("Saving PoidsProduit to database: {}", poidsProduit);
            EnrichirLibelle.libellePoidsProduit(poidsProduit);
            PoidsProduit saved = poidsProduitRepository.save(poidsProduit);
            log.info("PoidsProduit saved with id: {}", saved.getId());
            return saved;
        });
    }

    public Mono<PoidsProduit> modifierPoidsProduit(PoidsProduit poidsProduit) {
        return Mono.fromCallable(() -> {
            if (poidsProduit.getId() == null) {
                throw new IllegalArgumentException("L'id du poids produit est requis pour la modification");
            }

            PoidsProduit existingPoidsProduit = poidsProduitRepository.findById(poidsProduit.getId())
                    .orElseThrow(() -> new IllegalArgumentException("PoidsProduit not found with id: " + poidsProduit.getId()));

            existingPoidsProduit.setPoids(poidsProduit.getPoids());
            existingPoidsProduit.setUnitePoids(poidsProduit.getUnitePoids());
            existingPoidsProduit.setCodePoids(poidsProduit.getCodePoids());
            EnrichirLibelle.libellePoidsProduit(existingPoidsProduit);

            return poidsProduitRepository.save(existingPoidsProduit);
        });
    }

    public Mono<Void> supprimerPoidsProduit(Long id) {
        return Mono.fromCallable(() -> {
            PoidsProduit existingPoidsProduit = poidsProduitRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("PoidsProduit not found with id: " + id));
            poidsProduitRepository.deleteById(existingPoidsProduit.getId());
            return null;
        });
    }

    public Mono<PoidsProduit> getPoidsProduitById(Long id) {
        return Mono.fromCallable(() -> {
            log.info("Fetching PoidsProduit with id: {}", id);
            PoidsProduit poidsProduit = poidsProduitRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("PoidsProduit not found with id: " + id));
            EnrichirLibelle.libellePoidsProduit(poidsProduit);
            return poidsProduit;
        });
    }

    public Mono<List<PoidsProduit>> getAllPoidsProduits() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all PoidsProduits from database");
            List<PoidsProduit> result = poidsProduitRepository.findAll();
            result.forEach(EnrichirLibelle::libellePoidsProduit);
            return result;
        });
    }
}

