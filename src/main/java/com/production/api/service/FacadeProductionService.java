package com.production.api.service;

import com.production.api.model.*;
import com.production.api.util.Retour;
import com.production.api.util.TypeLot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FacadeProductionService {
    private final SrvProduit srvProduit;
    private final SrvSilo srvSilo;
    private final SrvClient srvClient;
    private final SrvUtilisateur srvUtilisateur;
    private final SrvStation srvStation;
    private final SrvPoidsProduit srvPoidsProduit;

    public Mono<Mono<ResponseProduction>> obtenirPayloadProduction(String encours) {
        return Mono.fromCallable(() -> {
            log.info("Fetching all produits from database");

            Mono<List<Produit>> produitsMono =srvProduit.findAllProduitsByEncours(encours);
            
            Mono<List<Client>> clientsMono = srvClient.getAllClients();
            Mono<List<Silo>> silosMono = srvSilo.getAllSilos();
            Mono<List<Utilisateur>> listeQAMono = srvUtilisateur.findAssuranceQualite();
            Mono<List<Station>> stationsMono = srvStation.getAllStations();
            Mono<List<PoidsProduit>> poidsProduitMono = srvPoidsProduit.getAllPoidsProduits();
            return extractedProduction(produitsMono, clientsMono, silosMono, listeQAMono, stationsMono, poidsProduitMono);
        });
    }

    private Mono<ResponseProduction> extractedProduction( Mono<List<Produit>> produitsMono, Mono<List<Client>> clientsMono, Mono<List<Silo>> silosMono, Mono<List<Utilisateur>> listeQAMono, Mono<List<Station>> stationsMono, Mono<List<PoidsProduit>> poidsProduitMono) {
        return Mono.zip(produitsMono, clientsMono, silosMono, listeQAMono, stationsMono, poidsProduitMono)
                .map(tuple -> {
                    List<Produit> produits = tuple.getT1();
                    List<Client> clients = tuple.getT2();
                    List<Silo> silos = tuple.getT3();
                    List<Utilisateur> listeQA = tuple.getT4();
                    List<Station> listeStation = tuple.getT5();
                    List<PoidsProduit> listePoidsProduit= tuple.getT6();

                    ResponseProduction response = new ResponseProduction();
                    response.setProduits(produits);
                    response.setClients(clients);

                    response.setProduitsConforme(produits.stream().filter(produit -> Boolean.TRUE.equals(produit.getConforme()) && Boolean.TRUE.equals(produit.getEncours())).collect(Collectors.toList())); // Assuming lotBags is a field in ResponseProductiontion
                    response.setProduitsFulminer(produits.stream().filter(produit -> Boolean.TRUE.equals(produit.getFulmine()) && Boolean.TRUE.equals(produit.getEncours())).collect(Collectors.toList()));
                    response.setProduitsPourQualite(produits.stream().filter(produit -> Boolean.FALSE.equals(produit.getConforme()) && Boolean.TRUE.equals(produit.getEncours())).collect(Collectors.toList())); // Assuming lotBags is a field in ResponseProductiontion
                    response.setProduitsPourFulminer(produits.stream().filter(produit -> Boolean.FALSE.equals(produit.getFulmine()) && Boolean.TRUE.equals(produit.getEncours())).collect(Collectors.toList()));
                    response.setProduitsExpedier(produits.stream().filter(produit -> Boolean.TRUE.equals(produit.getExpedier())).collect(Collectors.toList()));
                    response.setProduitsArecycler(produits.stream().filter(produit -> Boolean.TRUE.equals(produit.getArecycler())).collect(Collectors.toList()));

                    response.setSilos(silos);
                    response.setListQA(listeQA);
                    response.setStations(listeStation);
                    response.setPoidsProduits(listePoidsProduit);
                    response.setRetour(Retour.builder().code("Succes").httpCode(200).build()); // Set to null for now, can be populated with actual return status if needed
                    response.setSouvenirAppareil(true); // Set souvenirAppareil based on client existence

                    log.info("Returning ResponseProduction: {}", response);
                    return response;
                });
    }
}
