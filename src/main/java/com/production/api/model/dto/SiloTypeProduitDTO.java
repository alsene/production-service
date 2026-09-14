package com.production.api.model.dto;

import com.production.api.model.Silo;
import com.production.api.model.TypeProduit;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SiloTypeProduitDTO extends AbstractDTO {
    private Long id;
    private String codeProduit;
    private TypeProduit typeProduit;
    private Silo silo;
}

