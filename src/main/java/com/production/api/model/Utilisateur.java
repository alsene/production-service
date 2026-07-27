package com.production.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Utilisateur extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "compagnie", nullable = false)
    private String compagnie;

    @Column(name = "is_actif", columnDefinition = "boolean default false", nullable = false)
    private boolean actif;

    @JsonIgnore
    @Column(name = "password", nullable = false, length = 200)
    private String password = "TO_CHANGE";

    @Column(name = "change-password", nullable = false)
    private boolean changePassword = Boolean.TRUE;

    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "rue", nullable = false)
    private String rue;

    @Column(name = "ville", nullable = false)
    private String ville;

    @Column(name = "pays", nullable = false)
    private String pays;

    @Column(name = "code_postal", length = 10, nullable = false)
    private String codePostal;
}
