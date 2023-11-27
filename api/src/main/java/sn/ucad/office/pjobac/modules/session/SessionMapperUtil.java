package sn.ucad.office.pjobac.modules.session;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.centre.annee.Annee;
import sn.ucad.office.pjobac.modules.annee.AnneeDao;
import sn.ucad.office.pjobac.modules.session.dto.SessionRequest;
import sn.ucad.office.pjobac.modules.typeSession.TypeSession;
import sn.ucad.office.pjobac.modules.typeSession.TypeSessionDao;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessionMapperUtil {
    private final TypeSessionDao typeSessionDao;
    private  final AnneeDao anneeDao;


    @Named("getTypeSessionById")
    TypeSession getTypeSessionById(String typeSessionId) throws NumberFormatException {
        try {
            Long myId = Long.valueOf(typeSessionId.trim());
            TypeSession response;
            response = typeSessionDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune TypeSession avec " + typeSessionId + " trouvée.", HttpStatus.NOT_FOUND)
                    );
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id {} non autorisé. <SessionMapperUtil::getTypeSessionById>.", typeSessionId);
            throw new BusinessResourceException("not-valid-param", "Paramétre " + typeSessionId+ " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }
    @Named("getAnneeById")
    Annee getAnneeById(String anneeId) throws NumberFormatException {
        try {
            Long myId = Long.valueOf(anneeId.trim());
            Annee response;
            response = anneeDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Annee avec " + anneeId + " trouvée.", HttpStatus.NOT_FOUND)
                    );
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id {} non autorisé. <SessionMapperUtil::getAnneeById>.", anneeId);
            throw new BusinessResourceException("not-valid-param", "Paramétre " + anneeId+ " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }
    @Named("formatStringToDate")
    public static Date formatStringToDate(String date) throws ParseException {
        final AppDateFormatter dateFormatter = new AppDateFormatter();
        return dateFormatter.dateFormat(date, AppConstants.DATE_FR_FORMAT_TIRET);
    }
    @Named("formatStringToLong")
    public static Long formatStringToLong(String num) throws NumberFormatException {
        return  Long.valueOf(num.trim());
    }
    @Named("formatLibelle")
    public String formatLibelle(SessionRequest request) {
        if (request.getTypeSession() != null && request.getAnnee() != null) {
            String typeSession=request.getTypeSession();
            String annee=request.getAnnee();
            String libelleLongTypeSession;
            libelleLongTypeSession = getTypeSessionById(typeSession).getLibelleLong();
            String libelleLongAnnee;
            libelleLongAnnee=getAnneeById(annee).getLibelleLong();
            return "Session " + libelleLongTypeSession+ " " + libelleLongAnnee;
        }
        return null;
    }
}


