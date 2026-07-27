package com.production.api.controlleur;

import com.production.api.model.Profil;
import com.production.api.model.Utilisateur;
import com.production.api.model.dto.ProfilDTO;
import com.production.api.model.dto.UtilisateurDTO;
import com.production.api.model.mapper.ProfilMapper;
import com.production.api.model.mapper.UtilisateurMapper;
import com.production.api.service.FacadeProductionService;
import com.production.api.service.SrvProduit;
import com.production.api.service.SrvProfil;
import com.production.api.service.SrvUtilisateur;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production/endpoint/administration/v1")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CtrlAdministration {
    private final SrvProduit srvProduit;
    private final FacadeProductionService facadeProductionService;
    private final SrvUtilisateur srvUtilisateur;
    private final SrvProfil srvProfil;
    private final UtilisateurMapper utilisateurMapper;
    private final ProfilMapper profilMapper;


    @PostMapping(value = "/ajouterUtilisateur")
    public ResponseEntity<Utilisateur> ajouterUtilisateur(@RequestBody UtilisateurDTO utilisateurDTO) {
        log.info("POST /api/production/endpoint/administration/v1/ajouterUtilisateur called");

        utilisateurDTO.setIdUserCreation(1L);
        utilisateurDTO.setIdUserModification(1L);
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);
        Utilisateur addedUtilisateur = srvUtilisateur.ajouterUtilisateur(utilisateur).block();

        return ResponseEntity.ok(addedUtilisateur);
    }

    @PostMapping(value = "/modifierUtilisateur")
    public ResponseEntity<Utilisateur> modifierUtilisateur(@RequestBody UtilisateurDTO utilisateurDTO) {
        log.info("POST /api/production/endpoint/administration/v1/modifierUtilisateur called");

        utilisateurDTO.setIdUserModification(1L);

        Utilisateur updatedUtilisateur = srvUtilisateur.modifierUtilisateur(utilisateurDTO).block();
        return ResponseEntity.ok(updatedUtilisateur);
    }

    @PostMapping(value = "/supprimerUtilisateur")
    public ResponseEntity<Void> supprimerUtilisateur(@RequestBody UtilisateurDTO utilisateurDTO) {
        log.info("POST /api/production/endpoint/administration/v1/supprimerUtilisateur called");

        srvUtilisateur.supprimerUtilisateur(utilisateurDTO.getId()).block();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/afficherUtilisateurs")
    public ResponseEntity<List<Utilisateur>> afficherUtilisateurs() {
        log.info("GET /api/production/endpoint/administration/v1/afficherUtilisateurs called");

        List<Utilisateur> utilisateurs = srvUtilisateur.getAllUtilisateursFromDB().block();
        return ResponseEntity.ok(utilisateurs);
    }

    @PostMapping(value = "/ajouterProfil")
    public ResponseEntity<Profil> ajouterProfil(@RequestBody ProfilDTO profilDTO) {
        log.info("POST /api/production/endpoint/administration/v1/ajouterProfil called");

        profilDTO.setIdUserCreation(1L);
        profilDTO.setIdUserModification(1L);
        Profil profil = profilMapper.toEntity(profilDTO);
        Profil addedProfil = srvProfil.ajouterProfil(profil).block();

        return ResponseEntity.ok(addedProfil);
    }

    @PostMapping(value = "/modifierProfil")
    public ResponseEntity<Profil> modifierProfil(@RequestBody ProfilDTO profilDTO) {
        log.info("POST /api/production/endpoint/administration/v1/modifierProfil called");

        profilDTO.setIdUserModification(1L);

        Profil updatedProfil = srvProfil.modifierProfil(profilDTO).block();
        return ResponseEntity.ok(updatedProfil);
    }

    @PostMapping(value = "/supprimerProfil")
    public ResponseEntity<Void> supprimerProfil(@RequestBody ProfilDTO profilDTO) {
        log.info("POST /api/production/endpoint/administration/v1/supprimerProfil called");

        srvProfil.supprimerProfil(profilDTO.getId()).block();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/afficherProfils")
    public ResponseEntity<List<Profil>> afficherProfils() {
        log.info("GET /api/production/endpoint/administration/v1/afficherProfils called");

        List<Profil> profils = srvProfil.getAllProfilsFromDB().block();
        return ResponseEntity.ok(profils);
    }
}
