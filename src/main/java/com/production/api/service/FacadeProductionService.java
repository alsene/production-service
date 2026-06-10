package com.production.api.service;

import com.production.api.model.*;
import com.production.api.util.Retour;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FacadeProductionService {
    private final SrvProduit srvProduit;
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
            Mono<List<Utilisateur>> utilisateursMono = srvUtilisateur.findOperateursAndAssuranceQualite();;
            return extractedProduction(produitsMono, clientsMono, silosMono, lotsMono, utilisateursMono);
        });
    }

    private Mono<ResponseProduction> extractedProduction( Mono<List<Produit>> produitsMono, Mono<List<Client>> clientsMono, Mono<List<Silo>> silosMono, Mono<List<Lot>> lotsMono, Mono<List<Utilisateur>> utilisateursMono) {
        return Mono.zip(produitsMono, clientsMono, silosMono, lotsMono, utilisateursMono)
                .map(tuple -> {
                    List<Produit> produits = tuple.getT1();
                    List<Client> clients = tuple.getT2();
                    List<Silo> silos = tuple.getT3();
                    List<Lot> lots = tuple.getT4();
                    List<Utilisateur> utilisateurs = tuple.getT5();

                    ResponseProduction response = new ResponseProduction();
                    response.setProduits(produits);
                    response.setClients(clients);
                    response.setLots(lots);
                    response.setSilos(silos);
                    response.setOperateursAndAssuranceQualites(utilisateurs);
                    response.setRetour(Retour.builder().code("Succes").httpCode(200).build()); // Set to null for now, can be populated with actual return status if needed
                    response.setSouvenirAppareil(true); // Set souvenirAppareil based on client existence

                    log.info("Returning ResponseProduction: {}", response);
                    return response;
                });
    }
}
