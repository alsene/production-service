package com.production.api.repository;

import com.production.api.model.Lot;
import com.production.api.model.TypeProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeProduitRepository extends JpaRepository<TypeProduit, Long> {
    
    /**
     * Find product by name
     * @param libelle the product name
     * @return Optional containing the Lot if found
     */
    Optional<Lot> findBylibelle(String libelle);
    
    /**
     * Find all products by name pattern
     * @param libelle the product name pattern (using LIKE)
     * @return List of matching Lot
     */
    List<Lot> findBylibelleContainingIgnoreCase(String libelle);
}

