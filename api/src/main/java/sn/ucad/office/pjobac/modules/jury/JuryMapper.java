package sn.ucad.office.pjobac.modules.jury;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.config.AppConstants;

import sn.ucad.office.pjobac.modules.jury.dto.JuryAudit;
import sn.ucad.office.pjobac.modules.jury.dto.JuryRequest;
import sn.ucad.office.pjobac.modules.jury.dto.JuryResponse;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;
@Mapper(componentModel = "spring",uses = {JuryMapperUtil.class})
@Component
public interface JuryMapper {
    // transform the entity to PJO class
    JuryResponse toEntiteResponse(Jury jury);

    // transform the entity to PJO class with audit information
    @Mapping(source = "auteurName", target = "auteur")
    @Mapping(source = "modifName", target = "modificateur")
    JuryAudit toEntiteAudit(Jury jury, Long auteurName, Long modifName);

    // request to entity anne
    @Mapping(source = "request.centre", target = "centre",qualifiedByName = "getCentreById")
    Jury requestToEntity(JuryRequest request);

    // transform the PJO request to an entity
    //@Mapping(source = "user", target = "utiCree")
    @Mapping(source = "juryRequest.centre", target = "centre",qualifiedByName = "getCentreById")
    Jury requestToEntiteAdd(JuryRequest juryRequest/*, Utilisateur user*/);   // ici on n'a pa la classe Utilisateur

    // request to existing entity
    //@Mapping(source = "user", target = "utiModifie")
    @Mapping(source = "request.centre", target = "centre",qualifiedByName = "getCentreById")
    Jury requestToEntiteUp(@MappingTarget Jury entity, JuryRequest request/*, Utilisateur user*/);

    //  Source: https://www.baeldung.com/mapstruct-custom-mapper
    @Named("formatStringToDate")
    static Date formatStringToDate(String date) throws ParseException {
        final AppDateFormatter dateFormatter = new AppDateFormatter();
        String laDate = (date != null && date.trim().length() > 9 ) ? date : AppConstants.FIXED_DEFAULT_DATE;
        return dateFormatter.dateFormat(laDate, AppConstants.DATE_FR_FORMAT_SALASH);
        //  entity.setDateNaissance(dateFormatter.dateFormat(req.getDateNaissance(), MesConstants.DATE_FR_FORMAT_SALASH))
    }
    @Named("formatStringToLong")
    static Long formatStringToLong(String num) throws NumberFormatException {
        return  Long.valueOf(num.trim());
    }

}
