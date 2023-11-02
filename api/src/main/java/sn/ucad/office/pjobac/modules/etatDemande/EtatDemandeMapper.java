package sn.ucad.office.pjobac.modules.etatDemande;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import sn.ucad.office.pjobac.config.AppConstants;

import sn.ucad.office.pjobac.modules.etatDemande.dto.EtatDemandeAudit;
import sn.ucad.office.pjobac.modules.etatDemande.dto.EtatDemandeRequest;
import sn.ucad.office.pjobac.modules.etatDemande.dto.EtatDemandeResponse;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;
@Mapper(componentModel = "spring")
public interface EtatDemandeMapper {
    // transform the entity to PJO class
    EtatDemandeResponse toEntiteResponse(EtatDemande EtatDemande);

    // transform the entity to PJO class with audit information
    @Mapping(source = "auteurName", target = "auteur")
    @Mapping(source = "modifName", target = "modificateur")
    EtatDemandeAudit toEntiteAudit(EtatDemande EtatDemande, Long auteurName, Long modifName);

    // request to entity anne
    EtatDemande requestToEntity(EtatDemandeRequest request);

    // transform the PJO request to an entity
    //@Mapping(source = "user", target = "utiCree")
    EtatDemande requestToEntiteAdd(EtatDemandeRequest EtatDemandeRequest/*, Utilisateur user*/);   // ici on n'a pa la classe Utilisateur

    // request to existing entity
    //@Mapping(source = "user", target = "utiModifie")
    EtatDemande requestToEntiteUp(@MappingTarget EtatDemande entity, EtatDemandeRequest request/*, Utilisateur user*/);

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
