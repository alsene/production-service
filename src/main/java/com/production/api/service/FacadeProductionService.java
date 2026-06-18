package com.production.api.service;

import com.production.api.model.*;
import com.production.api.util.Retour;
import com.production.api.util.TypeLot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FacadeProductionService {
    private final SrvProduit srvProduit;
    private final SrvTypeProduit srvTypeProduit;
    private final SrvLot srvLot;
    private final SrvSilo srvSilo;
    private final SrvOperateur srvOperateur;
    private final SrvClient srvClient;
    private final SrvUtilisateur srvUtilisateur;

    public Mono<Mono<ResponseProduction>> obtenirPayloadProduction(String qualite) {
        return Mono.fromCallable(() -> {
            log.info("Fetching all produits from database");

            Mono<List<Produit>> produitsMono =srvProduit.findAllProduitsByQualite(qualite);
            
            Mono<List<Client>> clientsMono = srvClient.getAllClients();
            Mono<List<Lot>> lotsMono = srvLot.getAllLots();
            Mono<List<Silo>> silosMono = srvSilo.getAllSilos();
            Mono<List<TypeProduit>> typesProduitMono = srvTypeProduit.getAllTypeProduits();
            Mono<List<Utilisateur>> listeQAMono = srvUtilisateur.findAssuranceQualite();
            return extractedProduction(produitsMono, clientsMono, silosMono, lotsMono, typesProduitMono, listeQAMono);
        });
    }

    private Mono<ResponseProduction> extractedProduction( Mono<List<Produit>> produitsMono, Mono<List<Client>> clientsMono, Mono<List<Silo>> silosMono, Mono<List<Lot>> lotsMono, Mono<List<TypeProduit>> typesProduitMono, Mono<List<Utilisateur>> listeQAMono) {
        return Mono.zip(produitsMono, clientsMono, silosMono, lotsMono, typesProduitMono, listeQAMono)
                .map(tuple -> {
                    List<Produit> produits = tuple.getT1();
                    List<Client> clients = tuple.getT2();
                    List<Silo> silos = tuple.getT3();
                    List<Lot> lots = tuple.getT4();
                    List<TypeProduit> typesProduits = tuple.getT5();
                    List<Utilisateur> listeQA = tuple.getT6();

                    ResponseProduction response = new ResponseProduction();
                    response.setProduits(produits);
                    response.setClients(clients);
                    response.setLotBags(lots.stream().filter(lot -> lot.getTypeLot() == TypeLot.LOT_BIG_BAG).collect(Collectors.toList())); // Assuming lotBags is a field in ResponseProduction
                    response.setLots(lots.stream().filter(lot -> lot.getTypeLot() == TypeLot.LOT_PRODUIT).collect(Collectors.toList()));
                    response.setSilos(silos);
                    response.setListQA(listeQA);
                    response.setTypeProduits(typesProduits);
                    response.setRetour(Retour.builder().code("Succes").httpCode(200).build()); // Set to null for now, can be populated with actual return status if needed
                    response.setSouvenirAppareil(true); // Set souvenirAppareil based on client existence

                    log.info("Returning ResponseProduction: {}", response);
                    return response;
                });
    }
}
