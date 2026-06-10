package com.production.api.service;

import com.production.api.model.ProfilUtilisateur;
import com.production.api.model.mapper.ProfilUtilisateurMapper;
import com.production.api.repository.ProfilUtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SrvProfilUtilisateur {
    private final ProfilUtilisateurRepository profilUtilisateurRepository;
    private final ProfilUtilisateurMapper profilUtilisateurMapper;

    
    public Mono<ProfilUtilisateur> getProfilUtilisateur(){
        ProfilUtilisateur profilUtilisateur = new ProfilUtilisateur();
        log.info("Returning profilUtilisateur: {}", profilUtilisateur);
        return Mono.just(profilUtilisateur);
    }

    /**
     * Save a product to the database
     */
    public Mono<ProfilUtilisateur> ajouterProfilUtilisateur(ProfilUtilisateur profilUtilisateur) {
        return Mono.fromCallable(() -> {
            log.info("Saving profilUtilisateur to database: {}", profilUtilisateur);
            ProfilUtilisateur saved = profilUtilisateurRepository.save(profilUtilisateur);
            log.info("ProfilUtilisateur saved with id: {}", saved.getId());
            return saved;
        });
    }

    /**
     * Save a product to the database
     */
    public Mono<Void> supprimerProfilUtilisateur(Long id) {
        return Mono.fromCallable(() -> {
            ProfilUtilisateur existingProfilUtilisateur =getProfilUtilisateurById(id).block(); // Blocking call to get the existing product
            if(existingProfilUtilisateur == null) {
                log.warn("ProfilUtilisateur not found with id: {}", id);
                return null; // Return null if the product does not exist
            }
            profilUtilisateurRepository.deleteById(existingProfilUtilisateur.getId());
            log.info("ProfilUtilisateur deleted with id: {}", existingProfilUtilisateur.getId());
            return null; // Return null since the method is Mono<Void>
        });
    }

    /** * Modify a product in the database */
   /* @Transactional
    public Mono<Void> modifierProfilUtilisateur(ProfilUtilisateurDTO profilUtilisateurDTO) {
        return Mono.fromCallable(() -> {
            log.info("Modifying profilUtilisateur: {}", profilUtilisateurDTO);

            if (profilUtilisateurDTO.getUtilisateurId() == null) {
                throw new IllegalArgumentException("Le code du profilUtilisateur est requis pour la modification");
            }

            // Vérifier que le profilUtilisateur existe
            ProfilUtilisateur profilUtilisateurExistant = profilUtilisateurRepository.findProfilUtilisateurByCode(profilUtilisateurDTO.getCode());

            if (profilUtilisateurExistant == null) {
                throw new IllegalArgumentException("ProfilUtilisateur not found with code: " + profilUtilisateurDTO.getCode());
            }

            // Utiliser le mapper pour copier les champs non-null
            profilUtilisateurMapper.updateProfilUtilisateurFromDto(profilUtilisateurDTO, profilUtilisateurExistant);
            profilUtilisateurExistant.setDateModification(new Date());

            profilUtilisateurRepository.save(profilUtilisateurExistant);
            log.info("ProfilUtilisateur modifié avec id: {}", profilUtilisateurExistant.getId());
            return (Void) null;
        }).subscribeOn(Schedulers.boundedElastic());
    }*/

    /**
     * Get product by ID from database
     */
    public Mono<ProfilUtilisateur>  getProfilUtilisateurById(Long id) {
        return Mono.fromCallable(() -> {
            log.info("Fetching profilUtilisateur with id: {}", id);
            return profilUtilisateurRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("ProfilUtilisateur not found with id: " + id));
        });
    }

    /**
     * Get all products from database
     */
    public Mono<List<ProfilUtilisateur>> getAllProfilUtilisateursFromDB() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all profilUtilisateurs from database");
            return profilUtilisateurRepository.findAll();
        });
    }

}
