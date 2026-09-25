package com.production.api.service;

import com.production.api.client.ClientProduit;
import com.production.api.model.Lot;
import com.production.api.model.PoidsProduit;
import com.production.api.model.Produit;
import com.production.api.model.SiloTypeProduit;
import com.production.api.model.dto.ProduitDTO;
import com.production.api.model.mapper.ProduitMapper;
import com.production.api.repository.ProduitRepository;
import com.production.api.util.EnrichirLibelle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SrvProduit {
    private final ClientProduit clientProduit;
    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;

    // Return the reactive Mono<Produit> instead of blocking here
    public Mono<Produit> getAllProduit(){
        return clientProduit.getAllProduit();
    }
    
    public Mono<Produit> getProduit(){
        Produit produit = new Produit();
        produit.setNom("Produit Test2");
        produit.setQuantite(BigDecimal.valueOf(200));
        log.info("Returning produit: {}", produit);
        return Mono.just(produit);
    }

    /**
     * Save a product to the database
     */
    public Mono<Produit> ajouterProduit(Produit produit) {
        return Mono.fromCallable(() -> {
            log.info("Saving produit to database: {}", produit);
            EnrichirLibelle.libelleProduit(produit);
            Produit saved = produitRepository.save(produit);
            log.info("Produit saved with id: {}", saved.getId());
            return saved;
        });
    }

    /**
     * Save a product to the database
     */
    public Mono<Void> supprimerProduit(Long id) {
        return Mono.fromCallable(() -> {
            Produit existingProduit =getProduitById(id).block(); // Blocking call to get the existing product
            if(existingProduit == null) {
                log.warn("Produit not found with id: {}", id);
                return null; // Return null if the product does not exist
            }
            produitRepository.deleteById(existingProduit.getId());
            log.info("Produit deleted with id: {}", existingProduit.getId());
            return null; // Return null since the method is Mono<Void>
        });
    }

    /** * Modify a product in the database */
    @Transactional
    public Mono<Produit> modifierProduit(ProduitDTO produitDTO) {
        return Mono.fromCallable(() -> {
            log.info("Modifying produit: {}", produitDTO);

            if (produitDTO.getCode() == null) {
                throw new IllegalArgumentException("Le code du produit est requis pour la modification");
            }

            // Vérifier que le produit existe
            Produit produitExistant = produitRepository.findProduitByCode(produitDTO.getCode());

            if (produitExistant == null) {
                throw new IllegalArgumentException("Produit not found with code: " + produitDTO.getCode());
            }

            // Utiliser le mapper pour copier les champs non-null
            produitMapper.updateObjFromDto(produitDTO, produitExistant);
            produitExistant.setDateModification(new Date());
            EnrichirLibelle.libelleProduit(produitExistant);
            produitRepository.save(produitExistant);
            log.info("Produit modifié avec id: {}", produitExistant.getId());
            return produitExistant;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get product by ID from database
     */
    public Mono<Produit>  getProduitById(Long id) {
        return Mono.fromCallable(() -> {
            log.info("Fetching produit with id: {}", id);
            Produit produit= produitRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Produit not found with id: " + id));
            EnrichirLibelle.libelleProduit(produit);
            return produit;
        });
    }

    /**
     * Get product by ID from database
     */
    public Mono<Produit>  getProduitByCode(String code) {
        return Mono.fromCallable(() -> {
            log.info("Fetching produit with id: {}", code);
            return produitRepository.findProduitByCode(code);
        });
    }

    /**
     * Get all products from database
     */
    public Mono<List<Produit>> getAllProduitsFromDB() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all produits from database");
            return produitRepository.findAll();
        });
    }

    /**
     * Get  product from database
     */
    public Mono<Produit> findByQualite(String qualite) {
        return Mono.fromCallable(() -> {
            log.info("Fetching all produits from database");
            Produit produit=  produitRepository.findByQualite(qualite);
            EnrichirLibelle.libelleProduit(produit);
            return produit;
        });
    }


    /**
     * Get all products from database
     */
    public Mono<List<Produit>> findAllProduitsByQualite(String qualite) {
        return Mono.fromCallable(() -> {
            log.info("Fetching all Lots from database");
            List<Produit> produits=  produitRepository.findAllProduitsByQualite(qualite);
            produits.forEach(EnrichirLibelle::libelleProduit);
            return produits;
        });
    }
    /**
     * Get all products from database
     */
    @Transactional(readOnly = true)
    public Mono<List<Produit>> findAllProduitsByEncours(String encours) {
        return Mono.fromCallable(() -> {
            log.info("Fetching all Lots from database");
            List<Produit> produits=  produitRepository.findAllProduitsByEncours(Boolean.valueOf(encours));
            produits.forEach(EnrichirLibelle::libelleProduit);
            return produits;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
