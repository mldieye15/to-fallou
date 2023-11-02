package sn.ucad.office.pjobac.modules.typeSession;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.modules.annee.Annee;
import sn.ucad.office.pjobac.modules.typeSession.dto.TypeSessionAudit;
import sn.ucad.office.pjobac.modules.typeSession.dto.TypeSessionRequest;
import sn.ucad.office.pjobac.modules.typeSession.dto.TypeSessionResponse;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;
@Mapper(componentModel = "spring")
public interface TypeSessionMapper {
    // transform the entity to PJO class
    TypeSessionResponse toEntiteResponse(TypeSession typeSession);

    // transform the entity to PJO class with audit information
    @Mapping(source = "auteurName", target = "auteur")
    @Mapping(source = "modifName", target = "modificateur")
    TypeSessionAudit toEntiteAudit(TypeSession typeSession, Long auteurName, Long modifName);

    // request to entity anne
    TypeSession requestToEntity(TypeSessionRequest request);

    // transform the PJO request to an entity
    //@Mapping(source = "user", target = "utiCree")
    TypeSession requestToEntiteAdd(TypeSessionRequest typeSessionRequest/*, Utilisateur user*/);   // ici on n'a pa la classe Utilisateur

    // request to existing entity
    //@Mapping(source = "user", target = "utiModifie")
    TypeSession requestToEntiteUp(@MappingTarget TypeSession entity, TypeSessionRequest request/*, Utilisateur user*/);

    //  Source: https://www.baeldung.com/mapstruct-custom-mapper
    @Named("formatStringToDate")
    public static Date formatStringToDate(String date) throws ParseException {
        final AppDateFormatter dateFormatter = new AppDateFormatter();
        String laDate = (date != null && date.trim().length() > 9 ) ? date : AppConstants.FIXED_DEFAULT_DATE;
        return dateFormatter.dateFormat(laDate, AppConstants.DATE_FR_FORMAT_SALASH);
        //  entity.setDateNaissance(dateFormatter.dateFormat(req.getDateNaissance(), MesConstants.DATE_FR_FORMAT_SALASH))
    }
    @Named("formatStringToLong")
    public static Long formatStringToLong(String num) throws NumberFormatException, ParseException {
        return  Long.valueOf(num.trim());
    }

}
