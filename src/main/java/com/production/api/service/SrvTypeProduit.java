package com.production.api.service;

import com.production.api.model.TypeProduit;
import com.production.api.repository.TypeProduitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SrvTypeProduit {
    private final TypeProduitRepository typeProduitRepository;
    
    public Mono<TypeProduit> getTypeProduit(){
        TypeProduit TypeProduit = new TypeProduit();
        log.info("Returning TypeProduit: {}", TypeProduit);
        return Mono.just(TypeProduit);
    }

    /**
     * Save a product to the database
     */
    public Mono<TypeProduit> saveTypeProduit(TypeProduit typeProduit) {
        return Mono.fromCallable(() -> {
            log.info("Saving TypeProduit to database: {}", typeProduit);
            TypeProduit saved = typeProduitRepository.save(typeProduit);
            log.info("TypeProduit saved with id: {}", saved.getId());
            return saved;
        });
    }

    public Mono<TypeProduit> modifierTypeProduit(TypeProduit typeProduit) {
        return Mono.fromCallable(() -> {
            if (typeProduit.getId() == null) {
                throw new IllegalArgumentException("L'id du type produit est requis pour la modification");
            }

            TypeProduit existingTypeProduit = typeProduitRepository.findById(typeProduit.getId())
                    .orElseThrow(() -> new IllegalArgumentException("TypeProduit not found with id: " + typeProduit.getId()));

            existingTypeProduit.setLibelle(typeProduit.getLibelle());
            existingTypeProduit.setDescription(typeProduit.getDescription());

            return typeProduitRepository.save(existingTypeProduit);
        });
    }

    public Mono<Void> supprimerTypeProduit(Long id) {
        return Mono.fromCallable(() -> {
            TypeProduit existingTypeProduit = typeProduitRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("TypeProduit not found with id: " + id));
            typeProduitRepository.deleteById(existingTypeProduit.getId());
            return null;
        });
    }

    /**
     * Get product by ID from database
     */
    public Mono<TypeProduit>  getTypeProduitById(Long id) {
        return Mono.fromCallable(() -> {
            log.info("Fetching TypeProduit with id: {}", id);
            return typeProduitRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("TypeProduit not found with id: " + id));
        });
    }

    /**
     * Get all products from database
     */
    public Mono<List<TypeProduit>> getAllTypeProduits() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all TypeProduits from database");
            return typeProduitRepository.findAll();
        });
    }
}