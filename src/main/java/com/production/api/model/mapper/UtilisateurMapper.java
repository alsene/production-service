package com.production.api.model.mapper;


import com.production.api.model.Utilisateur;
import com.production.api.model.dto.UtilisateurDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UtilisateurMapper {
    @Mapping(ignore = true, target = "id")
    Utilisateur toEntity(UtilisateurDTO UtilisateurDTO);

    Utilisateur toEntityForUpdate(UtilisateurDTO UtilisateurDTO);

    @Mapping(ignore = true, target = "id")
    UtilisateurDTO toDto(Utilisateur Utilisateur);

    UtilisateurDTO toDtoForUpdate(Utilisateur Utilisateur);
    void updateUtilisateurFromDto(UtilisateurDTO source, @MappingTarget Utilisateur destination);

}
