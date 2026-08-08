package com.production.api.model.mapper;


import com.production.api.model.TypeProduit;
import com.production.api.model.dto.TypeProduitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TypeProduitMapper {

    @Mapping(ignore = true, target = "id")
    TypeProduit toEntity(TypeProduitDTO objDTO);

    TypeProduit toEntityForUpdate(TypeProduitDTO objDTO);

    @Mapping(ignore = true, target = "id")
    TypeProduitDTO toDto(TypeProduit obj);

    TypeProduitDTO toDtoForUpdate(TypeProduit obj);

    void updateObjFromDto(TypeProduitDTO source, @MappingTarget TypeProduit destination);

}
