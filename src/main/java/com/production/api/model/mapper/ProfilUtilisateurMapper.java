package com.production.api.model.mapper;


import com.production.api.model.Profil;
import com.production.api.model.ProfilUtilisateur;
import com.production.api.model.dto.ProfilDTO;
import com.production.api.model.dto.ProfilUtilisateurDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Optional;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfilUtilisateurMapper {

    ProfilUtilisateur toEntity(ProfilUtilisateurDTO profilUtilisateurDTO);

    ProfilUtilisateurDTO toDto(ProfilUtilisateur profilUtilisateur);

    void updateProfilUtilisateurFromDto(ProfilUtilisateurDTO source, @MappingTarget ProfilUtilisateur destination);;

}
