package sn.ucad.office.pjobac.modules.etablissement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.typeEtablissement.TypeEtablissement;
import sn.ucad.office.pjobac.modules.typeEtablissement.TypeEtablissementDao;
import sn.ucad.office.pjobac.modules.ville.Ville;
import sn.ucad.office.pjobac.modules.ville.VilleDao;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class EtablissementMapperUtil {
    private final TypeEtablissementDao typeEtablissementDao;
    private final VilleDao villeDao;

    @Named("getTypeEtablissementById")
    TypeEtablissement getTypeEtablissementById(String typeEtablissementId) throws NumberFormatException {
        try {
            Long myId = Long.valueOf(typeEtablissementId.trim());
            TypeEtablissement response;
            response = typeEtablissementDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun typeEtablissement avec " + typeEtablissementId + " trouvée.", HttpStatus.NOT_FOUND)
                    );
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id {} non autorisé. <TypeEtablissementMapperUtil::getTypeEtablissementById>.", typeEtablissementId);
            throw new BusinessResourceException("not-valid-param", "Paramétre " + typeEtablissementId+ " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }
    @Named("getVilleById")
    Ville getVilleById(String villeId) throws NumberFormatException {
        try {
            Long myId = Long.valueOf(villeId.trim());
            Ville response;
            response = villeDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune ville avec " + villeId + " trouvée.", HttpStatus.NOT_FOUND)
                    );
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id {} non autorisé. <EtablissementMapperUtil::getVilleById>.", villeId);
            throw new BusinessResourceException("not-valid-param", "Paramétre " + villeId+ " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }
    @Named("formatStringToDate")
    public static Date formatStringToDate(String date) throws ParseException {
        final AppDateFormatter dateFormatter = new AppDateFormatter();
        return dateFormatter.dateFormat(date, AppConstants.DATE_FR_FORMAT_SALASH);
        //  entity.setDateNaissance(dateFormatter.dateFormat(req.getDateNaissance(), MesConstants.DATE_FR_FORMAT_SALASH))
    }
}
