package com.production.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.production.api.util.Retour;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseProduction {
    private List<Produit> produits;
    private List<Produit> produitsPourFulminer;
    private List<Produit> produitsPourQualite;
    private List<Produit> produitsConforme;
    private List<Produit> produitsFulminer;
    private List<Produit> produitsExpedier;
    private List<Produit> produitsArecycler;
    private List<Silo> silos;
    private List<Client> clients;
    private List<Utilisateur> operateurs;
    private List<Utilisateur> listQA;
    private List<String> qualites;
    private List<Station> stations;
    private List<PoidsProduit> poidsProduits;
    private Retour retour;
    private Boolean souvenirAppareil;
}
