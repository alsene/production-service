package com.production.api.model.dto;

import com.production.api.model.Produit;
import com.production.api.util.TypeProfil;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class CommentaireProduitDTO extends AbstractDTO {
    private Long id;
    private String commentaire;
    private Produit produit;
}
