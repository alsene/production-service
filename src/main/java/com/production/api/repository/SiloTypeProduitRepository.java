package com.production.api.repository;

import com.production.api.model.SiloTypeProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiloTypeProduitRepository extends JpaRepository<SiloTypeProduit, Long> {

    Optional<SiloTypeProduit> findByCodeProduit(String codeProduit);

    List<SiloTypeProduit> findByCodeProduitContainingIgnoreCase(String codeProduit);
}

