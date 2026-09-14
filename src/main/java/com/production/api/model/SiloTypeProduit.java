package com.production.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "silo_typeproduit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SiloTypeProduit extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "code_produit", length = 25, nullable = false)
    private String codeProduit;

    @ManyToOne
    @JoinColumn(name = "Typeproduit_id", nullable = false)
    private TypeProduit typeProduit;

    @ManyToOne
    @JoinColumn(name = "silo_id", nullable = false)
    private Silo silo;
}
