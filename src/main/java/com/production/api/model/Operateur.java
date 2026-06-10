package com.production.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "operateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Operateur extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom", length = 255, nullable = false)
    private String nom;

    @Column(name = "prenom", length = 255, nullable = false)
    private String prenom;

    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Column(name = "telephone", length = 255, nullable = false)
    private String telephone;

    @Column(name = "numero", length = 20, nullable = false)
    private String numero;

    @Column(name = "rue", length = 255, nullable = false)
    private String rue;

    @Column(name = "ville", length = 255, nullable = false)
    private String ville;

    @Column(name = "pays", length = 255, nullable = false)
    private String pays;

    @Column(name = "code_postal", length = 10, nullable = false)
    private String codePostal;
}
