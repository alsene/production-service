package com.production.api.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientDTO extends AbstractDTO {
    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private String numero;
    private String rue;
    private String ville;
    private String pays;
    private String codePostal;
}

