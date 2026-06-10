package com.production.api.service;

import com.production.api.model.Profil;
import com.production.api.model.dto.ProfilDTO;
import com.production.api.model.mapper.ProfilMapper;
import com.production.api.repository.ProfilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SrvProfil {
    private final ProfilRepository profilRepository;
    private final ProfilMapper profilMapper;

    
    public Mono<Profil> getProfil(){
        Profil profil = new Profil();
        profil.setLibelle("Profil Test2");
        log.info("Returning profil: {}", profil);
        return Mono.just(profil);
    }

    /**
     * Save a product to the database
     */
    public Mono<Profil> ajouterProfil(Profil profil) {
        return Mono.fromCallable(() -> {
            log.info("Saving profil to database: {}", profil);
            Profil saved = profilRepository.save(profil);
            log.info("Profil saved with id: {}", saved.getId());
            return saved;
        });
    }

    /**
     * Save a product to the database
     */
    public Mono<Void> supprimerProfil(Long id) {
        return Mono.fromCallable(() -> {
            Profil existingProfil =getProfilById(id).block(); // Blocking call to get the existing product
            if(existingProfil == null) {
                log.warn("Profil not found with id: {}", id);
                return null; // Return null if the product does not exist
            }
            profilRepository.deleteById(existingProfil.getId());
            log.info("Profil deleted with id: {}", existingProfil.getId());
            return null; // Return null since the method is Mono<Void>
        });
    }

    /** * Modify a product in the database */
   /* @Transactional
    public Mono<Void> modifierProfil(ProfilDTO profilDTO) {
        return Mono.fromCallable(() -> {
            log.info("Modifying profil: {}", profilDTO);

            if (profilDTO.getId() == null) {
                throw new IllegalArgumentException("Le code du profil est requis pour la modification");
            }

            // Vérifier que le profil existe
            Optional<Profil> profilExistant = profilRepository.findById(profilDTO.getId());

            if (profilExistant == null) {
                throw new IllegalArgumentException("Profil not found with code: " + profilDTO.getId());
            }

            // Utiliser le mapper pour copier les champs non-null
            profilMapper.updateProfilFromDto(profilDTO, profilExistant);
            profilExistant.setDateModification(new Date());

            profilRepository.save(profilExistant);
            log.info("Profil modifié avec id: {}", profilExistant.getId());
            return (Void) null;
        }).subscribeOn(Schedulers.boundedElastic());
    }*/

    /**
     * Get product by ID from database
     */
    public Mono<Profil>  getProfilById(Long id) {
        return Mono.fromCallable(() -> {
            log.info("Fetching profil with id: {}", id);
            return profilRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Profil not found with id: " + id));
        });
    }


    /**
     * Get all products from database
     */
    public Mono<List<Profil>> getAllProfilsFromDB() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all profils from database");
            return profilRepository.findAll();
        });
    }

}
