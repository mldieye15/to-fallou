package sn.ucad.office.pjobac.modules.demande;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.modules.demande.dto.*;
import sn.ucad.office.pjobac.modules.detailsCandidat.DetailsCandidat;

@Mapper(componentModel = "spring", uses={DemandeMapperUtil.class})
@Component
public interface DemandeMapper {
    // transform the entity to PJO class
    DemandeResponse toEntiteResponse(Demande demande);
    @Mapping(source = "demande.id", target = "demandeId")
    @Mapping(source = "demande.user", target = "user")
    @Mapping(source = "demande.ville", target = "ville")
    @Mapping(source = "demande.session", target = "session")
    @Mapping(source = "demande.academie", target = "academie")
    @Mapping(source = "demande.etatDemande", target = "etatDemande")
    @Mapping(source = "demande.centre", target = "centre")
    @Mapping(source = "demande.jury", target = "jury")
    @Mapping(source = "demande.dateDemande", target = "dateDemande")
    @Mapping(source = "demande.dateRejetDemande", target = "dateRejetDemande")
    @Mapping(source = "demande.dateConfirmationDemande", target = "dateConfirmationDemande")
//    @Mapping(source = "demande.ordreArrivee", target = "ordreArrivee")
    @Mapping(source = "detailsCandidat.id", target = "detailsCandidatId")
    @Mapping(source = "detailsCandidat.affectable", target = "affectable")
    @Mapping(source = "detailsCandidat.note", target = "note")
    DemandeDetailsCandidatResponse mapToResponse(Demande demande, DetailsCandidat detailsCandidat);
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

    @Mapping(target = "ville",source = "affecterJuryRequest.ville", qualifiedByName = "getVilleById")
    @Mapping(target = "academie",source = "affecterJuryRequest.academie" ,qualifiedByName = "getAcademieById")
    @Mapping(target = "session", source = "affecterJuryRequest.session",qualifiedByName = "getSessionById")
    @Mapping(target = "centre", source = "affecterJuryRequest.centre",qualifiedByName = "getCentreById")
    @Mapping(target = "jury", source = "affecterJuryRequest.jury",qualifiedByName = "getJuryById")
    Demande affecterJuryToEntiteUp(@MappingTarget Demande entity, DemandeAffecterJury affecterJuryRequest );


    //  Source: https://www.baeldung.com/mapstruct-custom-mapper

}
