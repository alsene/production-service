package com.production.api.service;

import com.production.api.model.CommentaireProduit;
import com.production.api.model.dto.CommentaireProduitDTO;
import com.production.api.model.mapper.CommentaireProduitMapper;
import com.production.api.repository.CommentaireProduitRepository;
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
public class SrvCommentaireProduit {
    private final CommentaireProduitRepository commentaireProduitRepository;
    private final CommentaireProduitMapper commentaireProduiMapper;
    public Mono<CommentaireProduit> getCommentaireProduit(){
        CommentaireProduit commentaireProduit = new CommentaireProduit();
        log.info("Returning CommentaireProduit: {}", commentaireProduit);
        return Mono.just(commentaireProduit);
    }

    /**
     * Save a product to the database
     */
    public Mono<CommentaireProduit> saveCommentaireProduit(CommentaireProduit commentaireProduit) {
        return Mono.fromCallable(() -> {
            log.info("Saving CommentaireProduit to database: {}", commentaireProduit);
            CommentaireProduit saved = commentaireProduitRepository.save(commentaireProduit);
            log.info("CommentaireProduit saved with id: {}", saved.getId());
            return saved;
        });
    }
    /** * Modify a product in the database */
    @Transactional
    public Mono<CommentaireProduit> modifierCommentaireProduit(CommentaireProduitDTO objDTO) {
        return Mono.fromCallable(() -> {
            log.info("Modifying produit: {}", objDTO);

            if (objDTO.getId() == null) {
                throw new IllegalArgumentException("Le code du produit est requis pour la modification");
            }

            // Vérifier que le produit existe
            Optional<CommentaireProduit> commentaireProduitExistant = commentaireProduitRepository.findById(objDTO.getId());
            CommentaireProduit comProduitExistant;
            if (commentaireProduitExistant.isPresent()) {
                comProduitExistant = commentaireProduitExistant.get();
            }else{
                throw new IllegalArgumentException("Commentaire Produit not found with code: " );
            }

            // Utiliser le mapper pour copier les champs non-null
            commentaireProduiMapper.updateCommentaireProduitFromDto(objDTO, comProduitExistant);
            comProduitExistant.setDateModification(new Date());

            commentaireProduitRepository.save(comProduitExistant);
            log.info("Commentaire Produit modifié avec id: {}", comProduitExistant.getId());
            return comProduitExistant;
        }).subscribeOn(Schedulers.boundedElastic());
    }
    /**
     * Get product by ID from database
     */
    public Mono<CommentaireProduit>  getCommentaireProduitById(Long id) {
        return Mono.fromCallable(() -> {
            log.info("Fetching CommentaireProduit with id: {}", id);
            return commentaireProduitRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("CommentaireProduit not found with id: " + id));
        });
    }
    /**
     * Save a product to the database
     */
    public Mono<Void> supprimerCommentaireProduit(Long id) {
        return Mono.fromCallable(() -> {
            Optional<CommentaireProduit> commentaireProduitExistant = commentaireProduitRepository.findById(id);
            CommentaireProduit comProduitExistant = null;
            if (commentaireProduitExistant.isPresent()) {
                comProduitExistant = commentaireProduitExistant.get();
            }else{
                throw new IllegalArgumentException("Commentaire Produit not found with code: ");
            }
            commentaireProduitRepository.deleteById(comProduitExistant.getId());
            log.info("Commentaire Produit deleted with id: {}", comProduitExistant.getId());
            return null; // Return null since the method is Mono<Void>
        });
    }
    /**
     * Get all products from database
     */
    public Mono<List<CommentaireProduit>> getAllCommentairesProduit(Long idProduit) {
        return Mono.fromCallable(() -> {
            log.info("Fetching all CommentaireProduits from database");
            return commentaireProduitRepository.getAllCommentairesProduit(idProduit);
        });
    }
}