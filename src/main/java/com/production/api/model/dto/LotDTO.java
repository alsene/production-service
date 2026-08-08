package com.production.api.model.dto;

import com.production.api.util.TypeLot;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class LotDTO extends AbstractDTO {
    private Long id;
    private String libelle;
    private String description;
    private TypeLot typeLot;
}
