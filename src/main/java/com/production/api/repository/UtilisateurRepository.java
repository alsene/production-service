package com.production.api.repository;

import com.production.api.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {


    @Query("SELECT a FROM Utilisateur a WHERE a.email =:email")
    Utilisateur findUtilisateurByEmail(String email);

    @Query("SELECT a FROM Utilisateur a, Profil p, ProfilUtilisateur pu WHERE a.id = pu.utilisateur.id AND p.id = pu.profil.id AND p.typeProfil =:typeProfil")
    List<Utilisateur> findAllUtilisateursByTypeProfil(String typeProfil);

    /**
     * Find all users who are either operators or quality assurance specialists
     */
    @Query("SELECT a FROM Utilisateur a, Profil p, ProfilUtilisateur pu WHERE a.id = pu.utilisateur.id AND p.id = pu.profil.id AND (p.typeProfil = 'OPERATEUR' OR p.typeProfil = 'ASSURANCE_QUALITE')")
    List<Utilisateur> findOperateursAndAssuranceQualite();
}

