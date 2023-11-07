package sn.ucad.office.pjobac.modules.etablissement;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.config.AppConstants;

import sn.ucad.office.pjobac.modules.etablissement.dto.EtablissementAudit;
import sn.ucad.office.pjobac.modules.etablissement.dto.EtablissementRequest;
import sn.ucad.office.pjobac.modules.etablissement.dto.EtablissementResponse;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;
@Mapper(componentModel = "spring", uses={EtablissementMapperUtil.class})
@Component
public interface EtablissementMapper {
    // transform the entity to PJO class
    EtablissementResponse toEntiteResponse(Etablissement etablissement);

    // transform the entity to PJO class with audit information
    @Mapping(source = "auteurName", target = "auteur")
    @Mapping(source = "modifName", target = "modificateur")
    EtablissementAudit toEntiteAudit(Etablissement etablissement, Long auteurName, Long modifName);

    // request to entity anne
    @Mapping(source = "request.typeEtablissement",target = "typeEtablissement",qualifiedByName = "getTypeEtablissementById")
    @Mapping(source = "request.ville", target = "ville",qualifiedByName = "getVilleById")
    Etablissement requestToEntity(EtablissementRequest request);

    // transform the PJO request to an entity
//    @Mapping(source = "user", target = "utiCree")
    @Mapping(source = "etablissementRequest.ville", target = "ville",qualifiedByName = "getVilleById")
    @Mapping(source = "etablissementRequest.typeEtablissement",target = "typeEtablissement",qualifiedByName = "getTypeEtablissementById")
    Etablissement requestToEntiteAdd(EtablissementRequest etablissementRequest/*, Utilisateur user*/);   // ici on n'a pa la classe Utilisateur

    // request to existing entity
    //@Mapping(source = "user", target = "utiModifie")
    @Mapping(source = "request.ville", target = "ville",qualifiedByName = "getVilleById")
    @Mapping(source = "request.typeEtablissement",target = "typeEtablissement",qualifiedByName = "getTypeEtablissementById")
    Etablissement requestToEntiteUp(@MappingTarget Etablissement entity, EtablissementRequest request/*, Utilisateur user*/);

    //  Source: https://www.baeldung.com/mapstruct-custom-mapper
//    @Named("formatStringToDate")
//    public static Date formatStringToDate(String date) throws ParseException {
//        final AppDateFormatter dateFormatter = new AppDateFormatter();
//        String laDate = (date != null && date.trim().length() > 9 ) ? date : AppConstants.FIXED_DEFAULT_DATE;
//        return dateFormatter.dateFormat(laDate, AppConstants.DATE_FR_FORMAT_SALASH);
//        //  entity.setDateNaissance(dateFormatter.dateFormat(req.getDateNaissance(), MesConstants.DATE_FR_FORMAT_SALASH))
//    }
//    @Named("formatStringToLong")
//    public static Long formatStringToLong(String num) throws NumberFormatException, ParseException {
//        return  Long.valueOf(num.trim());
//    }

}
