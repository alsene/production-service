package com.production.api.repository;

import com.production.api.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    
    /**
     * Find product by name
     * @param nom the product name
     * @return Optional containing the Produit if found
     */
    Optional<Produit> findByNom(String nom);
    
    /**
     * Find all products by name pattern
     * @param nom the product name pattern (using LIKE)
     * @return List of matching Produit
     */
    List<Produit> findByNomContainingIgnoreCase(String nom);

    /**
     * Find all products by name pattern
     * @param qualite the product name pattern (using LIKE)
     * @return List of matching Produit
     */
    @Query("SELECT a FROM Produit a WHERE a.qualite =:qualite")
    Produit findByQualite(String qualite);

    @Query("SELECT a FROM Produit a WHERE a.qualite =:qualite")
    List<Produit> findAllProduitsByQualite(String qualite);

    @Query("SELECT a FROM Produit a WHERE a.code =:code")
    Produit findProduitByCode(String code);
}

