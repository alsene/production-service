package com.production.api.model.dto;

import com.production.api.util.TypeProfil;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class ProfilDTO extends AbstractDTO {
    private Long id;
    private String libelle;
    private TypeProfil typeProfil;
    private String description;

}
