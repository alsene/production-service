package com.production.api.model;

import com.production.api.util.TypeLot;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lot")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Lot extends AbstractEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_production", length = 25/*, nullable = false*/)
    private String numeroProduction;

    @Column(name = "annee_production", length = 5/*, nullable = false*/)
    private String anneeProduction;

    @Column(name = "type_lot", length = 25, nullable = true)
    @Enumerated(EnumType.STRING)
    private TypeLot typeLot;

}
