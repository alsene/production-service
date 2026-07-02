package com.production.api.controlleur;

import com.production.api.model.CommentaireProduit;
import com.production.api.model.Utilisateur;
import com.production.api.model.dto.CommentaireProduitDTO;
import com.production.api.model.dto.UtilisateurDTO;
import com.production.api.model.mapper.CommentaireProduitMapper;
import com.production.api.model.mapper.ProduitMapper;
import com.production.api.model.Produit;
import com.production.api.model.ResponseProduction;
import com.production.api.model.dto.ProduitDTO;
import com.production.api.service.FacadeProductionService;
import com.production.api.service.SrvCommentaireProduit;
import com.production.api.service.SrvProduit;
import com.production.api.util.Qualite;
import com.production.api.util.Retour;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/production/endpoint/produit/v1")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CtrlProduction {
    private final SrvProduit srvProduit;
    private final SrvCommentaireProduit srvCommentaireProduit;
    private final FacadeProductionService facadeProductionService;
    private final ProduitMapper produitMapper;
    private final CommentaireProduitMapper commentaireProduitMapper;

    // Endpoint that returns data directly (used by ClientProduit via WebClient to avoid infinite loop)
    @GetMapping
    public Mono<ResponseEntity<Produit>> getProduit(){
        log.info("GET /api/production/endpoint/produit/v1 called");
        return Mono.defer(() -> {
            Produit produit = new Produit();
            produit.setNom("Produit Test");
            produit.setQuantite(BigDecimal.valueOf(100));
            log.info("Returning test product: {}", produit);
            return Mono.just(ResponseEntity.ok(produit));
        });
    }

    @GetMapping(value = "/assurance-qualite/{conforme}")
    public ResponseEntity<ResponseProduction> getProduitsForQualite(@PathVariable String  conforme){
        log.info("GET /api/production/endpoint/produit/v1/assurance-qualite called");
        Mono<Mono<ResponseProduction>> monoMono= facadeProductionService.obtenirPayloadProduction(conforme);
        ResponseProduction responseFromFacade = monoMono.flatMap(mono -> mono).block(); // Blocking call to get the response from the facade
        List<String> qualites = List.of(Qualite.STANDARD.name(), Qualite.PREMIUM.name(), Qualite.EXCELLENCE.name());
        if(responseFromFacade == null) {
            responseFromFacade = new ResponseProduction(); // Create a new instance to avoid NullPointerException
        }
        responseFromFacade.setQualites(qualites);
        return ResponseEntity.ok(responseFromFacade);
    }

    @PostMapping(value = "/ajouter")
    public ResponseEntity<Produit> ajouterProduit(@RequestBody ProduitDTO produitDTO){
        log.info("POST /api/production/endpoint/produit/v1/ajouter called");
        Utilisateur operateur= new Utilisateur();
        operateur.setId(1L);
        produitDTO.setOperateur(operateur);
        produitDTO.setNom("couscous");
        produitDTO.setQualite(Qualite.DEFAULT.name());
        produitDTO.setIdUserCreation(1L);
        produitDTO.setIdUserModification(1L);
        Produit produit = produitMapper.toEntity(produitDTO);
        // Assuming there's a method to add a product
        Produit addedProduit= srvProduit.ajouterProduit(produit).block(); // Blocking call to add the product
        return ResponseEntity.ok(addedProduit);
    }

    @PostMapping(value = "/supprimer")
    public ResponseEntity<Void> supprimerProduit(@RequestBody ProduitDTO produitDTO){
        log.info("POST /api/production/endpoint/produit/v1/supprimer called");
        // Assuming there's a method to delete a product
        srvProduit.supprimerProduit(produitDTO.getId()).block(); // Blocking call to delete the product
      // Set to null for now, can be populated with actual return status if needed
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/modifier")
    public ResponseEntity<Produit> modifierProduit(@RequestBody ProduitDTO produitDTO){
        log.info("POST /api/production/endpoint/produit/v1/modifier called");
        // Assuming there's a method to add a product
        Produit updatedProduit= srvProduit.modifierProduit(produitDTO).block(); // Blocking call to add the product// Set to null for now, can be populated with actual return status if needed
        return ResponseEntity.ok(updatedProduit);
    }

    @PostMapping(value = "/ajouterCommentaire")
    public ResponseEntity<CommentaireProduit> ajouterCommentaire(@RequestBody CommentaireProduitDTO objDTO){
        log.info("POST /api/production/endpoint/produit/v1/ajouterCommentaire called");
        // Assuming there's a method to add a product
        objDTO.setIdUserCreation(1L);
        objDTO.setIdUserModification(1L);
        CommentaireProduit commentaireProduit = commentaireProduitMapper.toEntity(objDTO);

        Produit produit=commentaireProduit.getProduit();
        produit.getCommentaires().add(commentaireProduit);
        Produit addedProduit= srvProduit.ajouterProduit(produit).block();
        CommentaireProduit addedCommentaireProduit= new CommentaireProduit(); // Blocking call to add the product// Set to null for now, can be populated with actual return status if needed
        return ResponseEntity.ok(commentaireProduit);
    }

    @PostMapping(value = "/modifierCommentaire")
    public ResponseEntity<CommentaireProduit> modifierCommentaire(@RequestBody CommentaireProduitDTO objDTO){
        log.info("POST /api/production/endpoint/produit/v1/modifierCommentaire called");
        // Assuming there's a method to add a product
        CommentaireProduit updatedCommentaireProduit= srvCommentaireProduit.modifierCommentaireProduit(objDTO).block(); // Blocking call to add the product// Set to null for now, can be populated with actual return status if needed
        return ResponseEntity.ok(updatedCommentaireProduit);
    }

    @PostMapping(value = "/supprimerCommentaire")
    public ResponseEntity<Void> supprimerCommentaire(@RequestBody CommentaireProduitDTO objDTO){
        log.info("DELETE /api/production/endpoint/produit/v1/supprimerCommentaire called");
        // Assuming there's a method to delete a product
        srvCommentaireProduit.supprimerCommentaireProduit(objDTO.getId()).block(); // Blocking call to delete the product
        // Set to null for now, can be populated with actual return status if needed
        return ResponseEntity.ok().build();
    }
}
