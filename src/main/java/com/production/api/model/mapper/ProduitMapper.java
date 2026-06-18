package com.production.api.model.mapper;


import com.production.api.model.Produit;
import com.production.api.model.dto.ProduitDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProduitMapper {

    @Mapping(ignore = true, target = "id")
    Produit toEntity(ProduitDTO produitDTO);

    Produit toEntityForUpdate(ProduitDTO produitDTO);

    ProduitDTO toDto(Produit produit);

    @Mapping(ignore = true, target = "id")
    ProduitDTO toDtoForUpdate(Produit produit);

    void updateProduitFromDto(ProduitDTO source, @MappingTarget Produit destination);

}
