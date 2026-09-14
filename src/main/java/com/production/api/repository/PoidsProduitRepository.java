package com.production.api.repository;

import com.production.api.model.PoidsProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PoidsProduitRepository extends JpaRepository<PoidsProduit, Long> {

    Optional<PoidsProduit> findByCodePoids(String codePoids);

    List<PoidsProduit> findByCodePoidsContainingIgnoreCase(String codePoids);
}

