package com.production.api.model.mapper;


import com.production.api.model.Profil;
import com.production.api.model.ProfilUtilisateur;
import com.production.api.model.dto.ProfilDTO;
import com.production.api.model.dto.ProfilUtilisateurDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Optional;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfilUtilisateurMapper {

    @Mapping(ignore = true, target = "id")
    ProfilUtilisateur toEntity(ProfilUtilisateurDTO profilUtilisateurDTO);

    ProfilUtilisateur toEntityForUpdate(ProfilUtilisateurDTO profilUtilisateurDTO);

    @Mapping(ignore = true, target = "id")
    ProfilUtilisateurDTO toDto(ProfilUtilisateur profilUtilisateur);

    ProfilUtilisateurDTO toDtoForUpdate(ProfilUtilisateur profilUtilisateur);

    void updateProfilUtilisateurFromDto(ProfilUtilisateurDTO source, @MappingTarget ProfilUtilisateur destination);;

}
