package com.production.api.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PoidsProduitDTO extends AbstractDTO {
    private Long id;
    private String poids;
    private String unitePoids;
    private String codePoids;
    private String libelle;
}

