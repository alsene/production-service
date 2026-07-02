package com.production.api.controlleur;

import com.production.api.model.Produit;
import com.production.api.model.ResponseProduction;
import com.production.api.model.Utilisateur;
import com.production.api.model.dto.UtilisateurDTO;
import com.production.api.model.mapper.UtilisateurMapper;
import com.production.api.service.FacadeProductionService;
import com.production.api.service.SrvProduit;
import com.production.api.service.SrvUtilisateur;
import com.production.api.util.Retour;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    private final UtilisateurMapper utilisateurMapper;

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
}
