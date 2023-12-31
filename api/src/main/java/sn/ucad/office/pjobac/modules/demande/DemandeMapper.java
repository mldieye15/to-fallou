package sn.ucad.office.pjobac.modules.demande;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeAccepter;
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
    @Mapping(source = "request.academie", target = "academie",qualifiedByName = "getAcademieById")
    @Mapping(target = "session", source = "request.session",qualifiedByName = "getSessionById")
    Demande requestToEntity(DemandeRequest request);

    // transform the PJO request to an entity
    //@Mapping(source = "user", target = "utiCree")
    @Mapping(source = "demandeRequest.ville", target = "ville",qualifiedByName = "getVilleById")
    @Mapping(source = "demandeRequest.academie", target = "academie",qualifiedByName = "getAcademieById")
    @Mapping(target = "session", source = "demandeRequest.session",qualifiedByName = "getSessionById")
    Demande requestToEntiteAdd(DemandeRequest demandeRequest/*, Utilisateur user*/);   // ici on n'a pa la classe Utilisateur

    // request to existing entity
    //@Mapping(source = "user", target = "utiModifie")
    @Mapping(source = "request.ville", target = "ville",qualifiedByName = "getVilleById")
    @Mapping(source = "request.academie", target = "academie",qualifiedByName = "getAcademieById")
    @Mapping(target = "session", source = "request.session",qualifiedByName = "getSessionById")
    Demande requestToEntiteUp(@MappingTarget Demande entity, DemandeRequest request/*, Utilisateur user*/);
    @Mapping(target = "ville",source = "accepteRequest.ville", qualifiedByName = "getVilleById")
    @Mapping(target = "academie",source = "accepteRequest.academie" ,qualifiedByName = "getAcademieById")
    @Mapping(target = "session", source = "accepteRequest.session",qualifiedByName = "getSessionById")
    @Mapping(target = "centre", source = "accepteRequest.centre",qualifiedByName = "getCentreById")
    Demande accepterToEntiteUp(@MappingTarget Demande entity, DemandeAccepter accepteRequest );

    //  Source: https://www.baeldung.com/mapstruct-custom-mapper

}
