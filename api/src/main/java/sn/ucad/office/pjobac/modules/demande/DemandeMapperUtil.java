package sn.ucad.office.pjobac.modules.demande;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.ville.Ville;
import sn.ucad.office.pjobac.modules.ville.VilleDao;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class DemandeMapperUtil {
    private final VilleDao villeDao;

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
            log.warn("Paramétre id {} non autorisé. <DemandeMapperUtil::getVilleById>.", villeId);
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
