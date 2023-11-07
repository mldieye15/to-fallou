package sn.ucad.office.pjobac.modules.demande;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeAudit;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeRequest;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeResponse;
@Mapper(componentModel = "spring", uses={DemandeMapperUtil.class})
@Component
public interface DemandeMapper {
    // transform the entity to PJO class
    DemandeResponse toEntiteResponse(Demande demande);

    // transform the entity to PJO class with audit information
    @Mapping(source = "auteurName", target = "auteur")
    @Mapping(source = "modifName", target = "modificateur")
    DemandeAudit toEntiteAudit(Demande demande, Long auteurName, Long modifName);

    // request to entity demande
    @Mapping(source = "request.ville", target = "ville",qualifiedByName = "getVilleById")
    Demande requestToEntity(DemandeRequest request);

    // transform the PJO request to an entity
    //@Mapping(source = "user", target = "utiCree")
    @Mapping(source = "demandeRequest.ville", target = "ville",qualifiedByName = "getVilleById")
    Demande requestToEntiteAdd(DemandeRequest demandeRequest/*, Utilisateur user*/);   // ici on n'a pa la classe Utilisateur

    // request to existing entity
    //@Mapping(source = "user", target = "utiModifie")
    @Mapping(source = "request.ville", target = "ville",qualifiedByName = "getVilleById")
    Demande requestToEntiteUp(@MappingTarget Demande entity, DemandeRequest request/*, Utilisateur user*/);

    //  Source: https://www.baeldung.com/mapstruct-custom-mapper

}
