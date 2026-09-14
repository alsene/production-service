package com.production.api.repository;

import com.production.api.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    /**
     * Find station by libelle.
     */
    Optional<Station> findByLibelle(String libelle);

    /**
     * Find stations by libelle pattern.
     */
    List<Station> findByLibelleContainingIgnoreCase(String libelle);
}

