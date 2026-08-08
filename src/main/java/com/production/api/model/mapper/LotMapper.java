package com.production.api.model.mapper;


import com.production.api.model.Lot;
import com.production.api.model.dto.LotDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LotMapper {

    @Mapping(ignore = true, target = "id")
    Lot toEntity(LotDTO objDTO);

    Lot toEntityForUpdate(LotDTO objDTO);

    @Mapping(ignore = true, target = "id")
    LotDTO toDto(Lot obj);

    LotDTO toDtoForUpdate(Lot obj);

    void updateObjFromDto(LotDTO source, @MappingTarget Lot destination);

}
