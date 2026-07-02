package com.production.api.model.mapper;


import com.production.api.model.CommentaireProduit;
import com.production.api.model.dto.CommentaireProduitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentaireProduitMapper {

    @Mapping(ignore = true, target = "id")
    CommentaireProduit toEntity(CommentaireProduitDTO objDTO);

    CommentaireProduit toEntityForUpdate(CommentaireProduitDTO objDTO);

    @Mapping(ignore = true, target = "id")
    CommentaireProduitDTO toDto(CommentaireProduit obj);

    CommentaireProduitDTO toDtoForUpdate(CommentaireProduit obj);

    void updateCommentaireProduitFromDto(CommentaireProduitDTO source, @MappingTarget CommentaireProduit destination);

}
