package com.production.api.model.dto;

import com.production.api.model.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
public class ProduitDTO extends AbstractDTO {
    private Long id;
    private String nom;

    private String quantite;

    private String code;

    private Client client;

    private Utilisateur operateur;

    private Lot lot;

    private Lot lotBag;

    private Silo silo;

    private TypeProduit typeProduit;

    private String qualite;

    private Boolean fulmine;

    private Boolean conforme;

    private Boolean expedier;

    private Boolean arecycler;

    private Boolean encours;

    private List<CommentaireProduit> commentaires= new ArrayList<>();

}
