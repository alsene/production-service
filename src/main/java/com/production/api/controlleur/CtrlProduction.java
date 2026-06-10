package com.production.api.controlleur;

import com.production.api.model.mapper.ProduitMapper;
import com.production.api.model.Produit;
import com.production.api.model.ResponseProduction;
import com.production.api.model.dto.ProduitDTO;
import com.production.api.service.FacadeProductionService;
import com.production.api.service.SrvProduit;
import com.production.api.util.Retour;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/production/endpoint/produit/v1")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CtrlProduction {
    private final SrvProduit srvProduit;
    private final FacadeProductionService facadeProductionService;
    private final ProduitMapper produitMapper;

    // Endpoint that returns data directly (used by ClientProduit via WebClient to avoid infinite loop)
    @GetMapping
    public Mono<ResponseEntity<Produit>> getProduit(){
        log.info("GET /api/production/endpoint/produit/v1 called");
        return Mono.defer(() -> {
            Produit produit = new Produit();
            produit.setNom("Produit Test");
            produit.setQuantite("100");
            log.info("Returning test product: {}", produit);
            return Mono.just(ResponseEntity.ok(produit));
        });
    }

    @GetMapping(value = "/assurance-qualite/{qualite}")
    public ResponseEntity<ResponseProduction> getProduitsForQualite(@PathVariable String  qualite){
        log.info("GET /api/production/endpoint/produit/v1/assurance-qualite called");
        Mono<Mono<ResponseProduction>> monoMono= facadeProductionService.obtenirPayloadProduction(qualite);
        ResponseProduction responseFromFacade = monoMono.flatMap(mono -> mono).block(); // Blocking call to get the response from the facade
        return ResponseEntity.ok(responseFromFacade);
    }
    // Endpoint pour la gestion des produits
   /* @GetMapping(value = "/obtenir/{id}")
    public ResponseEntity<ResponseProduction> getAllProduit(@PathVariable Long id){
        log.info("GET /api/production/endpoint/produit/v1/obtenir called");
        ResponseProduction response = new ResponseProduction();
        response.setProduit(srvProduit.getProduitById(id).block()); // Blocking call to get the product
        response.setRetour(Retour.builder().code("Succes").httpCode(200).build()); // Set to null for now, can be populated with actual return status if needed
        return ResponseEntity.ok(response);
    }*/
    @PostMapping(value = "/ajouter")
    public ResponseEntity<ResponseProduction> ajouterProduit(@RequestBody ProduitDTO produitDTO){
        log.info("POST /api/production/endpoint/produit/v1/ajouter called");
        Produit produit = produitMapper.toEntity(produitDTO);
        ResponseProduction response = new ResponseProduction();
        // Assuming there's a method to add a product
        srvProduit.ajouterProduit(produit).block(); // Blocking call to add the product
        response.setRetour(Retour.builder().code("Succes").httpCode(200).build()); // Set to null for now, can be populated with actual return status if needed
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/supprimer/{id}")
    public ResponseEntity<ResponseProduction> supprimerProduit(@PathVariable Long id){
        log.info("DELETE /api/production/endpoint/produit/v1/supprimer called");

        ResponseProduction response = new ResponseProduction();
        // Assuming there's a method to delete a product
        srvProduit.supprimerProduit(id).block(); // Blocking call to delete the product
        response.setRetour(Retour.builder().code("Succes").httpCode(200).build()); // Set to null for now, can be populated with actual return status if needed
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/modifier")
    public ResponseEntity<ResponseProduction> modifierProduit(@RequestBody ProduitDTO produitDTO){
        log.info("POST /api/production/endpoint/produit/v1/ajouter called");
        ResponseProduction response = new ResponseProduction();
        // Assuming there's a method to add a product
        srvProduit.modifierProduit(produitDTO).block(); // Blocking call to add the product
        response.setRetour(Retour.builder().code("Succes").httpCode(200).build()); // Set to null for now, can be populated with actual return status if needed
        return ResponseEntity.ok(response);
    }

}
