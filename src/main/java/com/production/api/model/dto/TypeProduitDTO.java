package com.production.api.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class TypeProduitDTO extends AbstractDTO {
    private Long id;
    private String libelle;
    private String description;
}
