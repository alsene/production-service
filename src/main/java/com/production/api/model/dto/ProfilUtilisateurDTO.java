package com.production.api.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class ProfilUtilisateurDTO extends AbstractDTO {

    private Long utilisateurId;

    private Long profilId;

}
