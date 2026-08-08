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
    Utilisateur toEntity(UtilisateurDTO objDTO);

    Utilisateur toEntityForUpdate(UtilisateurDTO objDTO);

    @Mapping(ignore = true, target = "id")
    UtilisateurDTO toDto(Utilisateur obj);

    UtilisateurDTO toDtoForUpdate(Utilisateur obj);
    void updateObjFromDto(UtilisateurDTO source, @MappingTarget Utilisateur destination);

}
