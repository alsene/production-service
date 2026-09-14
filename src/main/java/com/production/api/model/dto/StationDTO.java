package com.production.api.model.dto;

import com.production.api.util.TypeLot;
import com.production.api.util.TypeStation;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class StationDTO extends AbstractDTO {
    private Long id;
    private String libelle;
    private TypeStation typeStation;
}
