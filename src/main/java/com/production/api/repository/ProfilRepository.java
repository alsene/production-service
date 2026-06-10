package com.production.api.repository;

import com.production.api.model.Client;
import com.production.api.model.Profil;
import com.production.api.util.TypeProfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfilRepository extends JpaRepository<Profil, Long> {
    
    /**
     * Find product by name
     * @param libelle the product name
     * @return Optional containing the Client if found
     */
    Optional<Profil> findByLibelle(String libelle);
    
    /**
     * Find all products by name pattern
     * @param typeProfil the product name pattern (using LIKE)
     * @return List of matching Client
     */
   /* @Query("SELECT a FROM Profil a WHERE a.type_profil =:typeProfil")
    List<Profil> findByTypeProfil(TypeProfil typeProfil);*/
}

