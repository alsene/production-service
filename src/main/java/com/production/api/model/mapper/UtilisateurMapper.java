package com.production.api.model.mapper;


import com.production.api.model.Utilisateur;
import com.production.api.model.dto.UtilisateurDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UtilisateurMapper {

    Utilisateur toEntity(UtilisateurDTO UtilisateurDTO);


    UtilisateurDTO toDto(Utilisateur Utilisateur);

    void updateUtilisateurFromDto(UtilisateurDTO source, @MappingTarget Utilisateur destination);

}
