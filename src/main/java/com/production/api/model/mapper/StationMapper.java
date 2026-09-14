package com.production.api.model.mapper;

import com.production.api.model.Station;
import com.production.api.model.dto.StationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StationMapper {

    @Mapping(ignore = true, target = "id")
    Station toEntity(StationDTO objDTO);

    Station toEntityForUpdate(StationDTO objDTO);

    @Mapping(ignore = true, target = "id")
    StationDTO toDto(Station obj);

    StationDTO toDtoForUpdate(Station obj);

    void updateObjFromDto(StationDTO source, @MappingTarget Station destination);
}

