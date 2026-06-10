package com.production.api.model.mapper;


import com.production.api.model.Produit;
import com.production.api.model.dto.ProduitDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProduitMapper {

    @Mapping(source = "clientId", target = "client.id")
    @Mapping(source = "lotId", target = "lot.id")
    @Mapping(source = "lotBagId", target = "lotBag.id")
    @Mapping(source = "siloId", target = "silo.id")
    @Mapping(source = "operateurId", target = "operateur.id")
    @Mapping(ignore = true, target = "id")
    Produit toEntity(ProduitDTO produitDTO);

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "lot.id", target = "lotId")
    @Mapping(source = "lotBag.id", target = "lotBagId")
    @Mapping(source = "silo.id", target = "siloId")
    @Mapping(source = "operateur.id", target = "operateurId")
    ProduitDTO toDto(Produit produit);

    void updateProduitFromDto(ProduitDTO source, @MappingTarget Produit destination);

}
