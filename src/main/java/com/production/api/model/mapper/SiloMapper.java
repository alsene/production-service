package com.production.api.model.mapper;


import com.production.api.model.Silo;
import com.production.api.model.dto.SiloDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SiloMapper {

    @Mapping(ignore = true, target = "id")
    Silo toEntity(SiloDTO objDTO);

    Silo toEntityForUpdate(SiloDTO objDTO);

    @Mapping(ignore = true, target = "id")
    SiloDTO toDto(Silo obj);

    SiloDTO toDtoForUpdate(Silo obj);

    void updateObjFromDto(SiloDTO source, @MappingTarget Silo destination);

}
