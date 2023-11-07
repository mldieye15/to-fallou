package sn.ucad.office.pjobac.modules.centre;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.config.AppConstants;

import sn.ucad.office.pjobac.modules.centre.dto.CentreAudit;
import sn.ucad.office.pjobac.modules.centre.dto.CentreRequest;
import sn.ucad.office.pjobac.modules.centre.dto.CentreResponse;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;
@Mapper(componentModel = "spring",uses = {CentreMapperUtil.class})
@Component
public interface CentreMapper {
    // transform the entity to PJO class
    CentreResponse toEntiteResponse(Centre Centre);

    // transform the entity to PJO class with audit information
    @Mapping(source = "auteurName", target = "auteur")
    @Mapping(source = "modifName", target = "modificateur")
    CentreAudit toEntiteAudit(Centre entre, Long auteurName, Long modifName);

    // request to entity anne
    @Mapping(source = "request.ville", target = "ville",qualifiedByName = "getVilleById")
    @Mapping(source = "request.typeCentre", target = "typeCentre",qualifiedByName = "getTypeCentreById")
    Centre requestToEntity(CentreRequest request);

    @Mapping(source = "centreRequest.ville", target = "ville",qualifiedByName = "getVilleById")
    @Mapping(source = "centreRequest.typeCentre", target = "typeCentre",qualifiedByName = "getTypeCentreById")
    Centre requestToEntiteAdd(CentreRequest centreRequest/*, Utilisateur user*/);   // ici on n'a pa la classe Utilisateur

    @Mapping(source = "request.ville", target = "ville",qualifiedByName = "getVilleById")
    @Mapping(source = "request.typeCentre", target = "typeCentre",qualifiedByName = "getTypeCentreById")
    Centre requestToEntiteUp(@MappingTarget Centre entity, CentreRequest request/*, Utilisateur user*/);

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
