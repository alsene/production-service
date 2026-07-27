package com.production.api.repository;

import com.production.api.model.ActivationCode;
import com.production.api.model.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Long> {
    
    /**
     * Find product by name
     * @param code the product name
     * @return Optional containing the ActivationCode if found
     */
    Optional<ActivationCode> findByCode(String code);
    
    /**
     * Find all products by name pattern
     * @param code the product name pattern (using LIKE)
     * @return List of matching ActivationCode
     */
    List<ActivationCode> findByCodeContainingIgnoreCase(String code);
}

