package com.production.api.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class UtilisateurDTO extends AbstractDTO {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String compagnie;
    private boolean actif;

    private String password ;

    private boolean changePassword;

    private String telephone;

    private String rue;

    private String ville;

    private String pays;

    private String codePostal;

}
