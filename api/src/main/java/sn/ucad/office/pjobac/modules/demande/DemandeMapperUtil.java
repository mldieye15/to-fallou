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
import sn.ucad.office.pjobac.modules.centre.Centre;
import sn.ucad.office.pjobac.modules.centre.CentreDao;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeRequest;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemande;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemandeDao;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemandeMapper;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemandeService;
import sn.ucad.office.pjobac.modules.jury.Jury;
import sn.ucad.office.pjobac.modules.jury.JuryDao;
import sn.ucad.office.pjobac.modules.security.token.AuthService;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.security.user.UserDao;
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
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DemandeMapperUtil {
    private final VilleDao villeDao;
    private final AcademieDao academieDao;
    private final SessionDao sessionDao;
    private final JuryDao juryDao;
    private  final CentreDao centreDao;
    private  final AuthService authService;

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
//    @Named("getUserById")
//    AppUser getUserById(String userId) throws NumberFormatException {
//        try {
//            Long myId = Long.valueOf(userId.trim());
//            AppUser response;
//            response = userDao.findById(myId)
//                    .orElseThrow(
//                            () -> new BusinessResourceException("not-found", "Aucune user avec " + userId + " trouvée.", HttpStatus.NOT_FOUND)
//                    );
//            return response;
//        } catch (NumberFormatException e) {
//            log.warn("Paramétre id {} non autorisé. <UserMapperUtil::getUserById>.", userId );
//            throw new BusinessResourceException("not-valid-param", "Paramétre " + userId + " non autorisé.", HttpStatus.BAD_REQUEST);
//        }
//    }
    @Named("getCentreById")
    Centre getCentrerById(String centreId) throws NumberFormatException {
        try {
            Long myId = Long.valueOf(centreId.trim());
            Centre response;
            response = centreDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune user avec " + centreId + " trouvée.", HttpStatus.NOT_FOUND)
                    );
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id {} non autorisé. <UserMapperUtil::getUserById>.", centreId );
            throw new BusinessResourceException("not-valid-param", "Paramétre " + centreId + " non autorisé.", HttpStatus.BAD_REQUEST);
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
            log.warn("Paramétre id {} non autorisé. <DemandeMapperUtil::getSessionById>.", sessionId);
            throw new BusinessResourceException("not-valid-param", "Paramétre " + sessionId+ " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }
    @Named("getJuryById")
    Jury getJuryById(String juryId) throws NumberFormatException {
        try {
            Long myId = Long.valueOf(juryId.trim());
            Jury response;
            response = juryDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune jury avec " +juryId + " trouvée.", HttpStatus.NOT_FOUND)
                    );
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id {} non autorisé. <DemandeMapperUtil::getJuryById>.", juryId);
            throw new BusinessResourceException("not-valid-param", "Paramétre " + juryId+ " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }
}

