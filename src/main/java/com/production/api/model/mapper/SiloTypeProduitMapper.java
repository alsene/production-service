package com.production.api.model.mapper;

import com.production.api.model.SiloTypeProduit;
import com.production.api.model.dto.SiloTypeProduitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SiloTypeProduitMapper {

    @Mapping(ignore = true, target = "id")
    SiloTypeProduit toEntity(SiloTypeProduitDTO objDTO);

    SiloTypeProduit toEntityForUpdate(SiloTypeProduitDTO objDTO);

    @Mapping(ignore = true, target = "id")
    SiloTypeProduitDTO toDto(SiloTypeProduit obj);

    SiloTypeProduitDTO toDtoForUpdate(SiloTypeProduit obj);

    void updateObjFromDto(SiloTypeProduitDTO source, @MappingTarget SiloTypeProduit destination);
}

