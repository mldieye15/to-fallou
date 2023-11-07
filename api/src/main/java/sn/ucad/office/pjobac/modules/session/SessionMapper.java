package sn.ucad.office.pjobac.modules.session;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.modules.session.dto.SessionAudit;
import sn.ucad.office.pjobac.modules.session.dto.SessionRequest;
import sn.ucad.office.pjobac.modules.session.dto.SessionResponse;

@Mapper(componentModel = "spring", uses={SessionMapperUtil.class})
@Component
public interface
SessionMapper {
    // transform the entity to PJO class
    SessionResponse toEntiteResponse(Session session);

    // transform the entity to PJO class with audit information
    @Mapping(source = "auteurName", target = "auteur")
    @Mapping(source = "modifName", target = "modificateur")
    SessionAudit toEntiteAudit(Session session, Long auteurName, Long modifName);

    // request to entity anne
    @Mapping(source = "request.dateDeDebut", target = "dateDebut", qualifiedByName = "formatStringToDate")
    @Mapping(source = "request.dateDeFin", target = "dateFin", qualifiedByName = "formatStringToDate")
    @Mapping(source = "request.dateDeOuvertureDepotCandidature", target = "dateOuvertureDepotCandidature", qualifiedByName = "formatStringToDate")
    @Mapping(source = "request.dateDeClotureDepotCandidature", target = "dateClotureDepotCandidature", qualifiedByName = "formatStringToDate")
    @Mapping(source = "request.typeSession",target = "typeSession",qualifiedByName = "getTypeSessionById")
    @Mapping(source = "request.annee",target = "annee",qualifiedByName = "getAnneeById")
    Session requestToEntity(SessionRequest request);

    // transform the PJO request to an entity
    //@Mapping(source = "user", target = "utiCree")
    @Mapping(source = "sessionRequest.typeSession",target = "typeSession",qualifiedByName = "getTypeSessionById")
    @Mapping(source = "sessionRequest.annee",target = "annee",qualifiedByName = "getAnneeById")
    Session requestToEntiteAdd(SessionRequest sessionRequest/*, Utilisateur user*/);   // ici on n'a pa la classe Utilisateur

    // request to existing entity
    //@Mapping(source = "user", target = "utiModifie")
    @Mapping(source = "request.annee",target = "annee",qualifiedByName = "getAnneeById")
    @Mapping(source = "request.typeSession",target = "typeSession",qualifiedByName = "getTypeSessionById")
    Session requestToEntiteUp(@MappingTarget Session entity, SessionRequest request/*, Utilisateur user*/);

    //  Source: https://www.baeldung.com/mapstruct-custom-mapper

}
