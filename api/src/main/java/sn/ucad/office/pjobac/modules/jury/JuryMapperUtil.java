package sn.ucad.office.pjobac.modules.jury;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.centre.Centre;
import sn.ucad.office.pjobac.modules.centre.CentreDao;
import sn.ucad.office.pjobac.modules.jury.dto.JuryRequest;
import sn.ucad.office.pjobac.modules.session.SessionDao;
import sn.ucad.office.pjobac.modules.session.Session;
import sn.ucad.office.pjobac.modules.session.dto.SessionRequest;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JuryMapperUtil {
    private final CentreDao centreDao;
    private final SessionDao sessionDao;
    private final JuryDao juryDao;

    @Named("getCentreById")
    Centre getCentreById(String centreId) throws NumberFormatException {
        try {
            Long myId = Long.valueOf(centreId.trim());
            Centre response;
            response = centreDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune centre avec " + centreId + " trouvée.", HttpStatus.NOT_FOUND)
                    );
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id {} non autorisé. <centreMapperUtil::getCentreById>.", centreId);
            throw new BusinessResourceException("not-valid-param", "Paramétre " + centreId+ " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }
    @Named("getSessionById")
    Session getSessionById(String sessionId) throws NumberFormatException {
        try {
            Long myId = Long.valueOf(sessionId.trim());
            Session response;
            response = sessionDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune session avec " + sessionId + " trouvée.", HttpStatus.NOT_FOUND)
                    );
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id {} non autorisé. <sessionMapperUtil::getSessionById>.", sessionId);
            throw new BusinessResourceException("not-valid-param", "Paramétre " + sessionId+ " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }
    private final Map<String, Set<Long>> numerosAttribuesParCentre = new HashMap<>();
    @Named("juryNom")
    public String numeroJury(JuryRequest request) {
        if (request.getCentre() != null) {
            String centreId= request.getCentre();
            numerosAttribuesParCentre.putIfAbsent(centreId, new HashSet<>());
            Set<Long> numerosAttribues = numerosAttribuesParCentre.get(centreId);
            Centre centre =getCentreById(centreId);
            if (centre !=null){
             Long   nombreJury= Long.valueOf(centre.getNombreJury());
             String libelleCourt= centre.getLibelleCourt();
                Long newNumber = nombreJury + 1;
                while (numerosAttribues.contains(newNumber)){
                    newNumber++;
                }
                numerosAttribues.add(newNumber);
                return String.format("%s%03d", libelleCourt, newNumber);

            }
        }
        return null;
    }
    @Named("formatNom")
    public String formatNom(JuryRequest request) {
        if (request.getSession() != null && request.getNumero() != null) {
            String session=request.getSession();
            String numero=request.getNumero();
            String annee;
            annee = getSessionById(session).getAnnee().getLibelleLong();
            return "Jury N° " +numero+ " de " + annee;
        }
        return null;
    }
}

