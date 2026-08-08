package com.production.api.model.mapper;

import com.production.api.model.Client;
import com.production.api.model.dto.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClientMapper {

    @Mapping(ignore = true, target = "id")
    Client toEntity(ClientDTO objDTO);

    Client toEntityForUpdate(ClientDTO objDTO);

    @Mapping(ignore = true, target = "id")
    ClientDTO toDto(Client obj);

    ClientDTO toDtoForUpdate(Client obj);

    void updateObjFromDto(ClientDTO source, @MappingTarget Client destination);
}

