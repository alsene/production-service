package com.production.api.repository;

import com.production.api.model.Silo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiloRepository extends JpaRepository<Silo, Long> {
    
    /**
     * Find product by name
     * @param libelle the product name
     * @return Optional containing the Silo if found
     */
    Optional<Silo> findBylibelle(String libelle);
    
    /**
     * Find all products by name pattern
     * @param libelle the product name pattern (using LIKE)
     * @return List of matching Silo
     */
    List<Silo> findBylibelleContainingIgnoreCase(String libelle);

}

