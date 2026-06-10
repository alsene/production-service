package com.production.api.model.mapper;


import com.production.api.model.Profil;
import com.production.api.model.dto.ProfilDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Optional;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfilMapper {

    Profil toEntity(ProfilDTO profilDTO);

    ProfilDTO toDto(Profil profil);

    void updateProfilFromDto(ProfilDTO source, @MappingTarget Profil destination);

}
