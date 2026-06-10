package com.production.api.model;

import com.production.api.util.TypeProfil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profil")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Profil extends AbstractEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "libelle", length = 25, nullable = false)
    private String libelle;


    @Column(name = "type_profil", length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeProfil typeProfil;

    @Column(name = "description", length = 255, nullable = false)
    private String description;

}
