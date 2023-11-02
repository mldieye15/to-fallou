package sn.ucad.office.pjobac.modules.session;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import sn.ucad.office.pjobac.config.AppConstants;

import sn.ucad.office.pjobac.modules.session.dto.SessionAudit;
import sn.ucad.office.pjobac.modules.session.dto.SessionRequest;
import sn.ucad.office.pjobac.modules.session.dto.SessionResponse;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;
@Mapper(componentModel = "spring")
public interface
SessionMapper {
    // transform the entity to PJO class
    SessionResponse toEntiteResponse(Session Session);

    // transform the entity to PJO class with audit information
    @Mapping(source = "auteurName", target = "auteur")
    @Mapping(source = "modifName", target = "modificateur")
    SessionAudit toEntiteAudit(Session Session, Long auteurName, Long modifName);

    // request to entity anne
    @Mapping(source = "request.dateDeDebut", target = "dateDebut", qualifiedByName = "formatStringToDate")
    @Mapping(source = "request.dateDeFin", target = "dateFin", qualifiedByName = "formatStringToDate")
    @Mapping(source = "request.dateDeOuvertureDepotCandidature", target = "dateOuvertureDepotCandidature", qualifiedByName = "formatStringToDate")
    @Mapping(source = "request.dateDeClotureDepotCandidature", target = "dateClotureDepotCandidature", qualifiedByName = "formatStringToDate")
    Session requestToEntity(SessionRequest request);

    // transform the PJO request to an entity
    //@Mapping(source = "user", target = "utiCree")
    Session requestToEntiteAdd(SessionRequest SessionRequest/*, Utilisateur user*/);   // ici on n'a pa la classe Utilisateur

    // request to existing entity
    //@Mapping(source = "user", target = "utiModifie")
    Session requestToEntiteUp(@MappingTarget Session entity, SessionRequest request/*, Utilisateur user*/);

    //  Source: https://www.baeldung.com/mapstruct-custom-mapper
    @Named("formatStringToDate")
    public static Date formatStringToDate(String date) throws ParseException {
        final AppDateFormatter dateFormatter = new AppDateFormatter();
        return dateFormatter.dateFormat(date, AppConstants.DATE_FR_FORMAT_SALASH);
    }
    @Named("formatStringToLong")
    public static Long formatStringToLong(String num) throws NumberFormatException, ParseException {
        return  Long.valueOf(num.trim());
    }

}
