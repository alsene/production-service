package com.production.api.model.mapper;


import com.production.api.model.Produit;
import com.production.api.model.dto.ProduitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProduitMapper {

    @Mapping(ignore = true, target = "id")
    Produit toEntity(ProduitDTO objDTO);

    Produit toEntityForUpdate(ProduitDTO objDTO);

    ProduitDTO toDto(Produit obj);

    @Mapping(ignore = true, target = "id")
    ProduitDTO toDtoForUpdate(Produit obj);

    @Mapping(target = "commentaires", ignore = true)
    void updateObjFromDto(ProduitDTO source, @MappingTarget Produit destination);

}
