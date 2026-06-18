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
    private List<Silo> silos;
    private List<Lot> lots;
    private List<Lot> lotBags;
    private List<Client> clients;
    private List<TypeProduit> typeProduits;
    private List<Utilisateur> operateurs;
    private List<Utilisateur> listQA;
    private Retour retour;
    private Boolean souvenirAppareil;
}
