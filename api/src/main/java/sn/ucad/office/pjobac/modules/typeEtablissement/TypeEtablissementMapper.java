package sn.ucad.office.pjobac.modules.typeEtablissement;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.modules.typeEtablissement.dto.TypeEtablissementAudit;
import sn.ucad.office.pjobac.modules.typeEtablissement.dto.TypeEtablissementRequest;
import sn.ucad.office.pjobac.modules.typeEtablissement.dto.TypeEtablissementResponse;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;
@Mapper(componentModel = "spring")
public interface TypeEtablissementMapper {
    // transform the entity to PJO class
    TypeEtablissementResponse toEntiteResponse(TypeEtablissement TypeEtablissement);

    // transform the entity to PJO class with audit information
    @Mapping(source = "auteurName", target = "auteur")
    @Mapping(source = "modifName", target = "modificateur")
    TypeEtablissementAudit toEntiteAudit(TypeEtablissement TypeEtablissement, Long auteurName, Long modifName);

    // request to entity typeEtablissement
    TypeEtablissement requestToEntity(TypeEtablissementRequest request);

    // transform the PJO request to an entity
    //@Mapping(source = "user", target = "utiCree")
    TypeEtablissement requestToEntiteAdd(TypeEtablissementRequest TypeEtablissementRequest/*, Utilisateur user*/);   // ici on n'a pa la classe Utilisateur

    // request to existing entity
    //@Mapping(source = "user", target = "utiModifie")
    TypeEtablissement requestToEntiteUp(@MappingTarget TypeEtablissement entity, TypeEtablissementRequest request/*, Utilisateur user*/);

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
