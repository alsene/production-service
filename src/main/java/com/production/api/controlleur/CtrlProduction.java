package com.production.api.controlleur;

import com.production.api.model.CommentaireProduit;
import com.production.api.model.Client;
import com.production.api.model.Lot;
import com.production.api.model.Silo;
import com.production.api.model.TypeProduit;
import com.production.api.model.Utilisateur;
import com.production.api.model.dto.CommentaireProduitDTO;
import com.production.api.model.dto.ClientDTO;
import com.production.api.model.dto.LotDTO;
import com.production.api.model.dto.SiloDTO;
import com.production.api.model.dto.TypeProduitDTO;
import com.production.api.model.dto.UtilisateurDTO;
import com.production.api.model.mapper.CommentaireProduitMapper;
import com.production.api.model.mapper.ClientMapper;
import com.production.api.model.mapper.LotMapper;
import com.production.api.model.mapper.ProduitMapper;
import com.production.api.model.mapper.SiloMapper;
import com.production.api.model.mapper.TypeProduitMapper;
import com.production.api.model.Produit;
import com.production.api.model.ResponseProduction;
import com.production.api.model.dto.ProduitDTO;
import com.production.api.service.FacadeProductionService;
import com.production.api.service.SrvCommentaireProduit;
import com.production.api.service.SrvClient;
import com.production.api.service.SrvLot;
import com.production.api.service.SrvProduit;
import com.production.api.service.SrvSilo;
import com.production.api.service.SrvTypeProduit;
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
    private final SrvLot srvLot;
    private final SrvTypeProduit srvTypeProduit;
    private final SrvSilo srvSilo;
    private final SrvClient srvClient;
    private final FacadeProductionService facadeProductionService;
    private final ProduitMapper produitMapper;
    private final CommentaireProduitMapper commentaireProduitMapper;
    private final LotMapper lotMapper;
    private final TypeProduitMapper typeProduitMapper;
    private final SiloMapper siloMapper;
    private final ClientMapper clientMapper;


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

    @GetMapping(value = "/assurance-qualite/{encours}")
    public ResponseEntity<ResponseProduction> getProduitsForQualite(@PathVariable String  encours){
        log.info("GET /api/production/endpoint/produit/v1/assurance-qualite called");
        Mono<Mono<ResponseProduction>> monoMono= facadeProductionService.obtenirPayloadProduction(encours);
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
        produitDTO.setEncours(Boolean.TRUE);
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
        // Le commentaire doit toujours être rattaché à un Produit existant (FK non null)
        if (objDTO.getProduit() == null || objDTO.getProduit().getId() == null) {
            throw new IllegalArgumentException("L'id du produit est requis pour ajouter un commentaire");
        }

        objDTO.setIdUserCreation(1L);
        objDTO.setIdUserModification(1L);
        CommentaireProduit commentaireProduit = commentaireProduitMapper.toEntity(objDTO);

        Produit produit = srvProduit.getProduitById(objDTO.getProduit().getId()).block();
        if (produit == null) {
            throw new IllegalArgumentException("Produit introuvable avec id: " + objDTO.getProduit().getId());
        }

        commentaireProduit.setProduit(produit);
        CommentaireProduit addedCommentaireProduit = srvCommentaireProduit.saveCommentaireProduit(commentaireProduit).block();
        return ResponseEntity.ok(addedCommentaireProduit);
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

    @GetMapping(value = "/obtenirLots")
    public ResponseEntity<List<Lot>> obtenirLots(){
        log.info("GET /api/production/endpoint/produit/v1/obtenirLots called");
        List<Lot> lots = srvLot.getAllLots().block();
        return ResponseEntity.ok(lots);
    }

    @PostMapping(value = "/ajouterLot")
    public ResponseEntity<Lot> ajouterLot(@RequestBody LotDTO lotDTO){
        log.info("POST /api/production/endpoint/produit/v1/ajouterLot called");
        Lot lot = lotMapper.toEntity(lotDTO);
        lot.setIdUserCreation(1L);
        lot.setIdUserModification(1L);
        Lot addedLot = srvLot.saveLot(lot).block();
        return ResponseEntity.ok(addedLot);
    }

    @PostMapping(value = "/modifierLot")
    public ResponseEntity<Lot> modifierLot(@RequestBody LotDTO lotDTO){
        log.info("POST /api/production/endpoint/produit/v1/modifierLot called");
        Lot lot = lotMapper.toEntityForUpdate(lotDTO);
        lot.setIdUserModification(1L);
        Lot updatedLot = srvLot.modifierLot(lot).block();
        return ResponseEntity.ok(updatedLot);
    }

    @PostMapping(value = "/supprimerLot")
    public ResponseEntity<Void> supprimerLot(@RequestBody LotDTO lotDTO){
        log.info("POST /api/production/endpoint/produit/v1/supprimerLot called");
        srvLot.supprimerLot(lotDTO.getId()).block();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/obtenirTypeProduits")
    public ResponseEntity<List<TypeProduit>> obtenirTypeProduits(){
        log.info("GET /api/production/endpoint/produit/v1/obtenirTypeProduits called");
        List<TypeProduit> typeProduits = srvTypeProduit.getAllTypeProduits().block();
        return ResponseEntity.ok(typeProduits);
    }

    @PostMapping(value = "/ajouterTypeProduit")
    public ResponseEntity<TypeProduit> ajouterTypeProduit(@RequestBody TypeProduitDTO typeProduitDTO){
        log.info("POST /api/production/endpoint/produit/v1/ajouterTypeProduit called");
        TypeProduit typeProduit = typeProduitMapper.toEntity(typeProduitDTO);
        typeProduit.setIdUserCreation(1L);
        typeProduit.setIdUserModification(1L);
        TypeProduit addedTypeProduit = srvTypeProduit.saveTypeProduit(typeProduit).block();
        return ResponseEntity.ok(addedTypeProduit);
    }

    @PostMapping(value = "/modifierTypeProduit")
    public ResponseEntity<TypeProduit> modifierTypeProduit(@RequestBody TypeProduitDTO typeProduitDTO){
        log.info("POST /api/production/endpoint/produit/v1/modifierTypeProduit called");
        TypeProduit typeProduit = typeProduitMapper.toEntityForUpdate(typeProduitDTO);
        typeProduit.setIdUserModification(1L);
        TypeProduit updatedTypeProduit = srvTypeProduit.modifierTypeProduit(typeProduit).block();
        return ResponseEntity.ok(updatedTypeProduit);
    }

    @PostMapping(value = "/supprimerTypeProduit")
    public ResponseEntity<Void> supprimerTypeProduit(@RequestBody TypeProduitDTO typeProduitDTO){
        log.info("POST /api/production/endpoint/produit/v1/supprimerTypeProduit called");
        srvTypeProduit.supprimerTypeProduit(typeProduitDTO.getId()).block();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/obtenirSilos")
    public ResponseEntity<List<Silo>> obtenirSilos(){
        log.info("GET /api/production/endpoint/produit/v1/obtenirSilos called");
        List<Silo> silos = srvSilo.getAllSilos().block();
        return ResponseEntity.ok(silos);
    }

    @PostMapping(value = "/ajouterSilo")
    public ResponseEntity<Silo> ajouterSilo(@RequestBody SiloDTO siloDTO){
        log.info("POST /api/production/endpoint/produit/v1/ajouterSilo called");
        Silo silo = siloMapper.toEntity(siloDTO);
        silo.setIdUserCreation(1L);
        silo.setIdUserModification(1L);
        Silo addedSilo = srvSilo.saveSilo(silo).block();
        return ResponseEntity.ok(addedSilo);
    }

    @PostMapping(value = "/modifierSilo")
    public ResponseEntity<Silo> modifierSilo(@RequestBody SiloDTO siloDTO){
        log.info("POST /api/production/endpoint/produit/v1/modifierSilo called");
        Silo silo = siloMapper.toEntityForUpdate(siloDTO);
        silo.setIdUserModification(1L);
        Silo updatedSilo = srvSilo.modifierSilo(silo).block();
        return ResponseEntity.ok(updatedSilo);
    }

    @PostMapping(value = "/supprimerSilo")
    public ResponseEntity<Void> supprimerSilo(@RequestBody SiloDTO siloDTO){
        log.info("POST /api/production/endpoint/produit/v1/supprimerSilo called");
        srvSilo.supprimerSilo(siloDTO.getId()).block();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/obtenirClients")
    public ResponseEntity<List<Client>> obtenirClients(){
        log.info("GET /api/production/endpoint/produit/v1/obtenirClients called");
        List<Client> clients = srvClient.getAllClients().block();
        return ResponseEntity.ok(clients);
    }

    @PostMapping(value = "/ajouterClient")
    public ResponseEntity<Client> ajouterClient(@RequestBody ClientDTO clientDTO){
        log.info("POST /api/production/endpoint/produit/v1/ajouterClient called");
        Client client = clientMapper.toEntity(clientDTO);
        client.setIdUserCreation(1L);
        client.setIdUserModification(1L);
        Client addedClient = srvClient.saveClient(client).block();
        return ResponseEntity.ok(addedClient);
    }

    @PostMapping(value = "/modifierClient")
    public ResponseEntity<Client> modifierClient(@RequestBody ClientDTO clientDTO){
        log.info("POST /api/production/endpoint/produit/v1/modifierClient called");
        Client client = clientMapper.toEntityForUpdate(clientDTO);
        client.setIdUserModification(1L);
        Client updatedClient = srvClient.modifierClient(client).block();
        return ResponseEntity.ok(updatedClient);
    }

    @PostMapping(value = "/supprimerClient")
    public ResponseEntity<Void> supprimerClient(@RequestBody ClientDTO clientDTO){
        log.info("POST /api/production/endpoint/produit/v1/supprimerClient called");
        srvClient.supprimerClient(clientDTO.getId()).block();
        return ResponseEntity.ok().build();
    }
}
