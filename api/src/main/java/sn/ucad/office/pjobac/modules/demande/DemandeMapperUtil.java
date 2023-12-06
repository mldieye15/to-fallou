package sn.ucad.office.pjobac.modules.demande;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.academie.Academie;
import sn.ucad.office.pjobac.modules.academie.AcademieDao;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeRequest;
import sn.ucad.office.pjobac.modules.session.Session;
import sn.ucad.office.pjobac.modules.session.SessionDao;
import sn.ucad.office.pjobac.modules.ville.Ville;
import sn.ucad.office.pjobac.modules.ville.VilleDao;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DemandeMapperUtil {
    private final VilleDao villeDao;
    private final AcademieDao academieDao;
    private final SessionDao sessionDao;

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
    @Named("getAcademieById")
    Academie getAcademieById(String academieId) throws NumberFormatException {
        try {
            Long myId = Long.valueOf(academieId.trim());
            Academie response;
            response = academieDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune academie avec " + academieId + " trouvée.", HttpStatus.NOT_FOUND)
                    );
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id {} non autorisé. <VilleMapperUtil::getAcademieById>.", academieId);
            throw new BusinessResourceException("not-valid-param", "Paramétre " + academieId+ " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }
    @Named("formatStringToDate")
    public static Date formatStringToDate(String date) throws ParseException {
        final AppDateFormatter dateFormatter = new AppDateFormatter();
        return dateFormatter.dateFormat(date, AppConstants.DATE_FR_FORMAT_SALASH);
        //  entity.setDateNaissance(dateFormatter.dateFormat(req.getDateNaissance(), MesConstants.DATE_FR_FORMAT_SALASH))
    }
    @Named("getSessionById")
    Session getSessionById(String sessionId) throws NumberFormatException {
        try {
            Long myId = Long.valueOf(sessionId.trim());
            Session response;
            response = sessionDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune ville avec " +sessionId + " trouvée.", HttpStatus.NOT_FOUND)
                    );
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id {} non autorisé. <DemandeMapperUtil::getVilleById>.", sessionId);
            throw new BusinessResourceException("not-valid-param", "Paramétre " + sessionId+ " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

}

