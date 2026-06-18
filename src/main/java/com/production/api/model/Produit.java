package com.production.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Produit extends AbstractEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom", length = 36, nullable = false)
    private String nom;
    
    @Column(name = "quantite", length = 50, nullable = false)
    private String quantite;

    @Column(name = "code", length = 20, nullable = true)
    private String code;

   /* @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "operateur_id", nullable = false)
    private Long operateurId;

    @Column(name = "lot_id", nullable = false)
    private Long lotId;

    @Column(name = "lotBag_id", nullable = false)
    private Long lotBagId;

    @Column(name = "silo_id", nullable = false)
    private Long siloId;*/


    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "operateur_id", nullable = false)
    private Utilisateur operateur;

    @ManyToOne
    @JoinColumn(name = "lot_id", nullable = false)
    private Lot lot;

    @ManyToOne
    @JoinColumn(name = "lotBag_id", nullable = false)
    private Lot lotBag;

    @ManyToOne
    @JoinColumn(name = "silo_id", nullable = false)
    private Silo silo;

    @ManyToOne
    @JoinColumn(name = "typeProduit_id", nullable = false)
    private TypeProduit typeProduit;

    @Column(name = "QUALITE", length=20,columnDefinition = "varchar(20) default 'BLANC'", nullable = false)
    private String qualite;

    @Column(name = "FULMINE")
    private Boolean fulmine;

}
