package com.production.api.model.mapper;


import com.production.api.model.Profil;
import com.production.api.model.dto.ProfilDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Optional;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfilMapper {

    @Mapping(ignore = true, target = "id")
    Profil toEntity(ProfilDTO profilDTO);

    Profil toEntityForUpdate(ProfilDTO profilDTO);

    @Mapping(ignore = true, target = "id")
    ProfilDTO toDto(Profil profil);

    ProfilDTO toDtoForUpdate(Profil profil);

    void updateProfilFromDto(ProfilDTO source, @MappingTarget Profil destination);

}
