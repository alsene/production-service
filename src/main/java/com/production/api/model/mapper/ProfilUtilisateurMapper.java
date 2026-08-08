package com.production.api.model.mapper;


import com.production.api.model.ProfilUtilisateur;
import com.production.api.model.dto.ProfilUtilisateurDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfilUtilisateurMapper {

    @Mapping(ignore = true, target = "id")
    ProfilUtilisateur toEntity(ProfilUtilisateurDTO objDTO);

    ProfilUtilisateur toEntityForUpdate(ProfilUtilisateurDTO objDTO);

    @Mapping(ignore = true, target = "id")
    ProfilUtilisateurDTO toDto(ProfilUtilisateur obj);

    ProfilUtilisateurDTO toDtoForUpdate(ProfilUtilisateur obj);

    void updateObjFromDto(ProfilUtilisateurDTO source, @MappingTarget ProfilUtilisateur destination);;

}
