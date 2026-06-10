package com.production.api.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class ProduitDTO extends AbstractDTO {

    private String nom;

    private String quantite;

    private String code;

    private Long clientId;

    private Long operateurId;

    private Long lotId;

    private Long lotBagId;

    private Long siloId;

    private String qualite;

    private Boolean fulmine;

}
