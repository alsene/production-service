package com.production.api.model.dto;

import com.production.api.model.Profil;
import com.production.api.model.Utilisateur;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class ProfilUtilisateurDTO extends AbstractDTO {
    private Long id;

    private Profil profil;

    private Utilisateur utilisateur;

}
