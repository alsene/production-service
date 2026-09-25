package com.production.api.model.dto;

import com.production.api.model.*;
import jakarta.persistence.Column;
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
    private String jourJulien;

    private Client client;

    private Utilisateur operateur;

    private Lot lot;

    private Lot lotBag;

    private Silo silo;

    private TypeProduit typeProduit;

    private PoidsProduit poidsProduit;
    private Station station;
    private SiloTypeProduit siloTypeProduit;

    private String qualite;

    private Boolean fulmine;

    private Boolean conforme;

    private Boolean expedier;

    private Boolean arecycler;

    private Boolean encours;

    private String numero;

    private List<CommentaireProduit> commentaires= new ArrayList<>();

}
