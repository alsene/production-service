package com.production.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.production.api.util.Qualite;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Column(name = "quantite", nullable = false)
    private BigDecimal quantite;

    @Column(name = "code", length = 20, nullable = true)
    private String code;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "qualite", length = 20, nullable = false)
    private Qualite qualite;

    @Column(name = "FULMINE")
    private Boolean fulmine;

    @Column(name = "CONFORME")
    private Boolean conforme;

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<CommentaireProduit>  commentaires= new ArrayList<>();

}
