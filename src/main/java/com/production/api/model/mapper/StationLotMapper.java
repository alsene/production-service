package com.production.api.model.mapper;

import com.production.api.model.StationLot;
import com.production.api.model.dto.StationLotDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StationLotMapper {

    @Mapping(ignore = true, target = "id")
    StationLot toEntity(StationLotDTO objDTO);

    StationLot toEntityForUpdate(StationLotDTO objDTO);

    @Mapping(ignore = true, target = "id")
    StationLotDTO toDto(StationLot obj);

    StationLotDTO toDtoForUpdate(StationLot obj);

    void updateObjFromDto(StationLotDTO source, @MappingTarget StationLot destination);
}

