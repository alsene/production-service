package com.production.api.service;

import com.production.api.model.Utilisateur;
import com.production.api.model.dto.UtilisateurDTO;
import com.production.api.model.mapper.UtilisateurMapper;
import com.production.api.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SrvUtilisateur {
    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;

    public Mono<Utilisateur> getUtilisateur(){
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom("Utilisateur Test2");
        log.info("Returning Utilisateur: {}", utilisateur);
        return Mono.just(utilisateur);
    }

    /**
     * Save a Utilisateur to the database
     */
    public Mono<Utilisateur> ajouterUtilisateur(Utilisateur utilisateur) {
        return Mono.fromCallable(() -> {
            log.info("Saving Utilisateur to database: {}", utilisateur);
            Utilisateur saved = utilisateurRepository.save(utilisateur);
            log.info("Utilisateur saved with id: {}", saved.getId());
            return saved;
        });
    }

    /**
     * Save a product to the database
     */
    public Mono<Void> supprimerUtilisateur(Long id) {
        return Mono.fromCallable(() -> {
            Utilisateur existingUtilisateur =getUtilisateurById(id).block(); // Blocking call to get the existing product
            if(existingUtilisateur == null) {
                log.warn("Utilisateur not found with id: {}", id);
                return null; // Return null if the product does not exist
            }
            utilisateurRepository.deleteById(existingUtilisateur.getId());
            log.info("Utilisateur deleted with id: {}", existingUtilisateur.getId());
            return null; // Return null since the method is Mono<Void>
        });
    }

    /** * Modify a product in the database */
    @Transactional
    public Mono<Void> modifierUtilisateur(UtilisateurDTO utilisateurDTO) {
        return Mono.fromCallable(() -> {
            log.info("Modifying Utilisateur: {}", utilisateurDTO);

            if (utilisateurDTO.getEmail() == null) {
                throw new IllegalArgumentException("L'email du Utilisateur est requis pour la modification");
            }

            // Vérifier que le Utilisateur existe
            Utilisateur utilisateurExistant = utilisateurRepository.findUtilisateurByEmail(utilisateurDTO.getEmail());

            if (utilisateurExistant == null) {
                throw new IllegalArgumentException("Utilisateur not found with email: " + utilisateurDTO.getEmail());
            }

            // Utiliser le mapper pour copier les champs non-null
            utilisateurMapper.updateUtilisateurFromDto(utilisateurDTO, utilisateurExistant);
            utilisateurExistant.setDateModification(new Date());

            utilisateurRepository.save(utilisateurExistant);
            log.info("Utilisateur modifié avec id: {}", utilisateurExistant.getId());
            return (Void) null;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get product by ID from database
     */
    public Mono<Utilisateur>  getUtilisateurById(Long id) {
        return Mono.fromCallable(() -> {
            log.info("Fetching Utilisateur with id: {}", id);
            return utilisateurRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur not found with id: " + id));
        });
    }

    /**
     * Get product by ID from database
     */
    public Mono<Utilisateur>  getUtilisateurByEmail(String email) {
        return Mono.fromCallable(() -> {
            log.info("Fetching Utilisateur with email: {}", email);
            return utilisateurRepository.findUtilisateurByEmail(email);
        });
    }

    /**
     * Get all products from database
     */
    public Mono<List<Utilisateur>> getAllUtilisateursFromDB() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all Utilisateurs from database");
            return utilisateurRepository.findAll();
        });
    }

    /**
     * Get all products from database
     */
    public Mono<List<Utilisateur>> getAllUtilisateursTypeProfil(String typeProfil) {
        return Mono.fromCallable(() -> {
            log.info("Fetching all Utilisateurs from database");
            return utilisateurRepository.findAllUtilisateursByTypeProfil(typeProfil);
        });
    }


    /**
     * Get all products from database
     */
    public Mono<List<Utilisateur>> findOperateursAndAssuranceQualite() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all Utilisateurs from database");
            return utilisateurRepository.findOperateursAndAssuranceQualite();
        });
    }

    /**
     * Get all products from database
     */
    public Mono<List<Utilisateur>> findAssuranceQualite() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all Utilisateurs from database");
            return utilisateurRepository.findAssuranceQualite();
        });
    }
}
