package com.production.api.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class UtilisateurDTO extends AbstractDTO {

    private Long id;
    private String nom;

    private String email;

    private String codeActivation;

    private String password ;

    private boolean changePassword;

    private String telephone;

    private String numero;

    private String rue;

    private String ville;

    private String pays;

    private String codePostal;

}
