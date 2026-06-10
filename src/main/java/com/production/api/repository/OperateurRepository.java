package com.production.api.repository;

import com.production.api.model.Operateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperateurRepository extends JpaRepository<Operateur, Long> {
    
    /**
     * Find product by name
     * @param nom the product name
     * @return Optional containing the Operateur if found
     */
    Optional<Operateur> findByNom(String nom);
    
    /**
     * Find all products by name pattern
     * @param nom the product name pattern (using LIKE)
     * @return List of matching Operateur
     */
    List<Operateur> findByNomContainingIgnoreCase(String nom);
}

