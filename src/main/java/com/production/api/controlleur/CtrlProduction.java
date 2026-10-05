package com.production.api.controlleur;

import com.production.api.model.*;
import com.production.api.model.dto.*;
import com.production.api.model.mapper.*;
import com.production.api.service.*;
import com.production.api.util.JourJulien;
import com.production.api.util.Qualite;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final SrvPoidsProduit srvPoidsProduit;
    private final SrvTypeProduit srvTypeProduit;
    private final SrvSilo srvSilo;
    private final SrvSiloTypeProduit srvSiloTypeProduit;
    private final SrvStation srvStation;
    private final SrvStationLot srvStationLot;
    private final SrvClient srvClient;
    private final SrvPdfProduction srvPdfProduction;
    private final FacadeProductionService facadeProductionService;
    private final ProduitMapper produitMapper;
    private final CommentaireProduitMapper commentaireProduitMapper;
    private final LotMapper lotMapper;
    private final PoidsProduitMapper poidsProduitMapper;
    private final TypeProduitMapper typeProduitMapper;
    private final SiloMapper siloMapper;
    private final SiloTypeProduitMapper siloTypeProduitMapper;
    private final StationMapper stationMapper;
    private final StationLotMapper stationLotMapper;
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
        try {
            Mono<Mono<ResponseProduction>> monoMono= facadeProductionService.obtenirPayloadProduction(encours);
            ResponseProduction responseFromFacade = monoMono.flatMap(mono -> mono).block(); // Blocking call to get the response from the facade
            List<String> qualites = List.of(Qualite.STANDARD.name(), Qualite.PREMIUM.name(), Qualite.EXCELLENCE.name());
            if(responseFromFacade == null) {
                responseFromFacade = new ResponseProduction(); // Create a new instance to avoid NullPointerException
            }
            responseFromFacade.setQualites(qualites);
            ResponseEntity<ResponseProduction> response= ResponseEntity.ok(responseFromFacade);
            return response;
        } catch (Exception e) {
            log.info("GET /api/production/endpoint/produit/v1/assurance-qualite called", e.getCause());
            throw new RuntimeException(e);
        }
    }
    private static void enrichirMapperProduit(ProduitDTO produitDTO) {
        produitDTO.setJourJulien(JourJulien.getJourJulien());
        produitDTO.setTypeProduit(produitDTO.getSiloTypeProduit().getTypeProduit());
        produitDTO.setNom(produitDTO.getSiloTypeProduit().getLibelle());
        produitDTO.setLot(produitDTO.getStationLot().getLot());
    }
    @PostMapping(value = "/ajouter")
    public ResponseEntity<Produit> ajouterProduit(@RequestBody ProduitDTO produitDTO){
        log.info("POST /api/production/endpoint/produit/v1/ajouter called");
        Utilisateur operateur= new Utilisateur();
        operateur.setId(1L);
        produitDTO.setOperateur(operateur);
        produitDTO.setQualite(Qualite.DEFAULT.name());
        produitDTO.setIdUserCreation(1L);
        produitDTO.setIdUserModification(1L);
        produitDTO.setEncours(Boolean.TRUE);
        produitDTO.setExpedier(Boolean.FALSE);
        produitDTO.setArecycler(Boolean.FALSE);
        produitDTO.setFulmine(Boolean.FALSE);
        produitDTO.setConforme(Boolean.FALSE);
        enrichirMapperProduit(produitDTO);
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
        enrichirMapperProduit(produitDTO);
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

    @GetMapping(value = "/impression/produits-encours/pdf")
    public ResponseEntity<byte[]> imprimerProduitsEncoursPdf() {
        log.info("GET /api/production/endpoint/produit/v1/impression/produits-encours/pdf called");

        List<Produit> produitsEncours = srvProduit.findAllProduitsByEncours("true").block();
        if (produitsEncours == null) {
            produitsEncours = List.of();
        }

        byte[] pdfBytes = srvPdfProduction.genererPdfProduitsEncours(produitsEncours);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String fileName = "produits-encours-" + timestamp + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(pdfBytes);
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
    @GetMapping(value = "/obtenirSiloTypeProduits")
    public ResponseEntity<List<SiloTypeProduit>> obtenirSiloTypeProduits(){
        log.info("GET /api/production/endpoint/produit/v1/obtenirSiloTypeProduits called");
        List<SiloTypeProduit> siloTypeProduits = srvSiloTypeProduit.getAllSiloTypeProduits().block();
        return ResponseEntity.ok(siloTypeProduits);
    }

    @GetMapping(value = "/findBySiloId")
    public ResponseEntity<List<SiloTypeProduit>> findBySiloId(@RequestParam String siloId ){
        log.info("GET /api/production/endpoint/produit/v1/findBySiloId called");
        List<SiloTypeProduit> siloTypeProduits = srvSiloTypeProduit.findBySiloId(siloId).block();
        return ResponseEntity.ok(siloTypeProduits);
    }

    @GetMapping(value = "/findByTypeProduitId/{typeProduitId}")
    public ResponseEntity<List<SiloTypeProduit>> findByTypeProduitId(@PathVariable String typeProduitId){
        log.info("GET /api/production/endpoint/produit/v1/findByTypeProduitId called");
        List<SiloTypeProduit> siloTypeProduits = srvSiloTypeProduit.findByTypeProduitId(typeProduitId).block();
        return ResponseEntity.ok(siloTypeProduits);
    }

    @PostMapping(value = "/ajouterSiloTypeProduit")
    public ResponseEntity<SiloTypeProduit> ajouterSiloTypeProduit(@RequestBody SiloTypeProduitDTO siloTypeProduitDTO){
        log.info("POST /api/production/endpoint/produit/v1/ajouterSiloTypeProduit called");
        SiloTypeProduit siloTypeProduit = siloTypeProduitMapper.toEntity(siloTypeProduitDTO);
        siloTypeProduit.setIdUserCreation(1L);
        siloTypeProduit.setIdUserModification(1L);
        SiloTypeProduit addedSiloTypeProduit = srvSiloTypeProduit.saveSiloTypeProduit(siloTypeProduit).block();
        return ResponseEntity.ok(addedSiloTypeProduit);
    }

    @PostMapping(value = "/modifierSiloTypeProduit")
    public ResponseEntity<SiloTypeProduit> modifierSiloTypeProduit(@RequestBody SiloTypeProduitDTO siloTypeProduitDTO){
        log.info("POST /api/production/endpoint/produit/v1/modifierSiloTypeProduit called");
        SiloTypeProduit siloTypeProduit = siloTypeProduitMapper.toEntityForUpdate(siloTypeProduitDTO);
        siloTypeProduit.setIdUserModification(1L);
        SiloTypeProduit updatedSiloTypeProduit = srvSiloTypeProduit.modifierSiloTypeProduit(siloTypeProduit).block();
        return ResponseEntity.ok(updatedSiloTypeProduit);
    }

    @PostMapping(value = "/supprimerSiloTypeProduit")
    public ResponseEntity<Void> supprimerSiloTypeProduit(@RequestBody SiloTypeProduitDTO siloTypeProduitDTO){
        log.info("POST /api/production/endpoint/produit/v1/supprimerSiloTypeProduit called");
        srvSiloTypeProduit.supprimerSiloTypeProduit(siloTypeProduitDTO.getId()).block();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/obtenirStations")
    public ResponseEntity<List<Station>> obtenirStations(){
        log.info("GET /api/production/endpoint/produit/v1/obtenirStations called");
        List<Station> stations = srvStation.getAllStations().block();
        return ResponseEntity.ok(stations);
    }

    @PostMapping(value = "/ajouterStation")
    public ResponseEntity<Station> ajouterStation(@RequestBody StationDTO stationDTO){
        log.info("POST /api/production/endpoint/produit/v1/ajouterStation called");
        Station station = stationMapper.toEntity(stationDTO);
        station.setIdUserCreation(1L);
        station.setIdUserModification(1L);
        Station addedStation = srvStation.saveStation(station).block();
        return ResponseEntity.ok(addedStation);
    }

    @PostMapping(value = "/modifierStation")
    public ResponseEntity<Station> modifierStation(@RequestBody StationDTO stationDTO){
        log.info("POST /api/production/endpoint/produit/v1/modifierStation called");
        Station station = stationMapper.toEntityForUpdate(stationDTO);
        station.setIdUserModification(1L);
        Station updatedStation = srvStation.modifierStation(station).block();
        return ResponseEntity.ok(updatedStation);
    }

    @PostMapping(value = "/supprimerStation")
    public ResponseEntity<Void> supprimerStation(@RequestBody StationDTO stationDTO){
        log.info("POST /api/production/endpoint/produit/v1/supprimerStation called");
        srvStation.supprimerStation(stationDTO.getId()).block();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/obtenirPoidsProduits")
    public ResponseEntity<List<PoidsProduit>> obtenirPoidsProduits(){
        log.info("GET /api/production/endpoint/produit/v1/obtenirPoidsProduits called");
        List<PoidsProduit> poidsProduits = srvPoidsProduit.getAllPoidsProduits().block();
        return ResponseEntity.ok(poidsProduits);
    }

    @PostMapping(value = "/ajouterPoidsProduit")
    public ResponseEntity<PoidsProduit> ajouterPoidsProduit(@RequestBody PoidsProduitDTO poidsProduitDTO){
        log.info("POST /api/production/endpoint/produit/v1/ajouterPoidsProduit called");
        PoidsProduit poidsProduit = poidsProduitMapper.toEntity(poidsProduitDTO);
        poidsProduit.setIdUserCreation(1L);
        poidsProduit.setIdUserModification(1L);
        PoidsProduit addedPoidsProduit = srvPoidsProduit.savePoidsProduit(poidsProduit).block();
        return ResponseEntity.ok(addedPoidsProduit);
    }

    @PostMapping(value = "/modifierPoidsProduit")
    public ResponseEntity<PoidsProduit> modifierPoidsProduit(@RequestBody PoidsProduitDTO poidsProduitDTO){
        log.info("POST /api/production/endpoint/produit/v1/modifierPoidsProduit called");
        PoidsProduit poidsProduit = poidsProduitMapper.toEntityForUpdate(poidsProduitDTO);
        poidsProduit.setIdUserModification(1L);
        PoidsProduit updatedPoidsProduit = srvPoidsProduit.modifierPoidsProduit(poidsProduit).block();
        return ResponseEntity.ok(updatedPoidsProduit);
    }

    @PostMapping(value = "/supprimerPoidsProduit")
    public ResponseEntity<Void> supprimerPoidsProduit(@RequestBody PoidsProduitDTO poidsProduitDTO){
        log.info("POST /api/production/endpoint/produit/v1/supprimerPoidsProduit called");
        srvPoidsProduit.supprimerPoidsProduit(poidsProduitDTO.getId()).block();
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/obtenirStationLots")
    public ResponseEntity<List<StationLot>> obtenirStationLots(){
        log.info("GET /api/production/endpoint/produit/v1/obtenirStationLots called");
        List<StationLot> stationLots = srvStationLot.getAllStationLots().block();
        return ResponseEntity.ok(stationLots);
    }

    @GetMapping(value = "/findByStationId")
    public ResponseEntity<List<StationLot>> findByStationId(@RequestParam String stationId ){
        log.info("GET /api/production/endpoint/produit/v1/findByStationId called");
        List<StationLot> stationLots = srvStationLot.findByStationId(stationId).block();
        return ResponseEntity.ok(stationLots);
    }

    @GetMapping(value = "/findByLotId/{lotId}")
    public ResponseEntity<List<StationLot>> findByLotId(@PathVariable String lotId){
        log.info("GET /api/production/endpoint/produit/v1/findByLotId called");
        List<StationLot> stationLots = srvStationLot.findByLotId(lotId).block();
        return ResponseEntity.ok(stationLots);
    }

    @PostMapping(value = "/ajouterStationLot")
    public ResponseEntity<StationLot> ajouterStationLot(@RequestBody StationLotDTO stationLotDTO){
        log.info("POST /api/production/endpoint/produit/v1/ajouterStationLot called");
        StationLot stationLot = stationLotMapper.toEntity(stationLotDTO);
        stationLot.setIdUserCreation(1L);
        stationLot.setIdUserModification(1L);
        StationLot addedStationLot = srvStationLot.saveStationLot(stationLot).block();
        return ResponseEntity.ok(addedStationLot);
    }

    @PostMapping(value = "/modifierStationLot")
    public ResponseEntity<StationLot> modifierStationLot(@RequestBody StationLotDTO stationLotDTO){
        log.info("POST /api/production/endpoint/produit/v1/modifierStationLot called");
        StationLot stationLot = stationLotMapper.toEntityForUpdate(stationLotDTO);
        stationLot.setIdUserModification(1L);
        StationLot updatedStationLot = srvStationLot.modifierStationLot(stationLot).block();
        return ResponseEntity.ok(updatedStationLot);
    }

    @PostMapping(value = "/supprimerStationLot")
    public ResponseEntity<Void> supprimerStationLot(@RequestBody StationLotDTO stationLotDTO){
        log.info("POST /api/production/endpoint/produit/v1/supprimerStationLot called");
        srvStationLot.supprimerStationLot(stationLotDTO.getId()).block();
        return ResponseEntity.ok().build();
    }
}
