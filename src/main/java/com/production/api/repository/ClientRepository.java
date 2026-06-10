package com.production.api.repository;

import com.production.api.model.Client;
import com.production.api.model.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    /**
     * Find product by name
     * @param nom the product name
     * @return Optional containing the Client if found
     */
    Optional<Client> findByNom(String nom);
    
    /**
     * Find all products by name pattern
     * @param nom the product name pattern (using LIKE)
     * @return List of matching Client
     */
    List<Client> findByNomContainingIgnoreCase(String nom);
}

