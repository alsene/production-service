package com.production.api.repository;

import com.production.api.model.CommentaireProduit;
import com.production.api.model.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentaireProduitRepository extends JpaRepository<CommentaireProduit, Long> {
    
    /**
     * Find product by name
     * @param commentaire the product name
     * @return Optional containing the CommentaireProduit if found
     */
    Optional<CommentaireProduit> findByCommentaire(String commentaire);
    
    /**
     * Find all products by name pattern
     * @param commentaire the product name pattern (using LIKE)
     * @return List of matching CommentaireProduit
     */
    List<CommentaireProduit> findByCommentaireContainingIgnoreCase(String commentaire);

    @Query("SELECT a FROM CommentaireProduit a WHERE a.produit.id = : idProduit")
    List<CommentaireProduit> getAllCommentairesProduit(Long idProduit);
}

