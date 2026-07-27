package com.production.api.repository;

import com.production.api.model.ProfilUtilisateur;
import com.production.api.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfilUtilisateurRepository extends JpaRepository<ProfilUtilisateur, Long> {
    
	@Query("SELECT pu FROM ProfilUtilisateur pu WHERE pu.utilisateur.id = :utilisateurId")
	List<ProfilUtilisateur> findByUtilisateurId(Long utilisateurId);
}

