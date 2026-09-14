package com.production.api.model.mapper;

import com.production.api.model.PoidsProduit;
import com.production.api.model.dto.PoidsProduitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PoidsProduitMapper {

    @Mapping(ignore = true, target = "id")
    PoidsProduit toEntity(PoidsProduitDTO objDTO);

    PoidsProduit toEntityForUpdate(PoidsProduitDTO objDTO);

    @Mapping(ignore = true, target = "id")
    PoidsProduitDTO toDto(PoidsProduit obj);

    PoidsProduitDTO toDtoForUpdate(PoidsProduit obj);

    void updateObjFromDto(PoidsProduitDTO source, @MappingTarget PoidsProduit destination);
}

