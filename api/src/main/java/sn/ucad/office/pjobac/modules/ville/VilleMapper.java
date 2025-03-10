package sn.ucad.office.pjobac.modules.ville;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.config.AppConstants;

import sn.ucad.office.pjobac.modules.typeSession.dto.VilleAudit;
import sn.ucad.office.pjobac.modules.typeSession.dto.VilleRequest;
import sn.ucad.office.pjobac.modules.typeSession.dto.VilleResponse;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;
@Mapper(componentModel = "spring",uses = {VilleMapperUtil.class})
@Component
public interface VilleMapper {
    // transform the entity to PJO class
    VilleResponse toEntiteResponse(Ville ville);

    // transform the entity to PJO class with audit information
    @Mapping(source = "auteurName", target = "auteur")
    @Mapping(source = "modifName", target = "modificateur")
    VilleAudit toEntiteAudit(Ville ville, Long auteurName, Long modifName);

    // request to entity ville
    @Mapping(source = "request.academie",target = "academie",qualifiedByName = "getAcademieById")
    Ville requestToEntity(VilleRequest request);

    // transform the PJO request to an entity
    //@Mapping(source = "user", target = "utiCree")
    @Mapping(source = "villeRequest.academie",target = "academie",qualifiedByName = "getAcademieById")
    Ville requestToEntiteAdd(VilleRequest villeRequest/*, Utilisateur user*/);   // ici on n'a pa la classe Utilisateur

    // request to existing entity
    //@Mapping(source = "user", target = "utiModifie")
    @Mapping(source = "request.academie",target = "academie",qualifiedByName = "getAcademieById")
    Ville requestToEntiteUp(@MappingTarget Ville entity, VilleRequest request/*, Utilisateur user*/);

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
