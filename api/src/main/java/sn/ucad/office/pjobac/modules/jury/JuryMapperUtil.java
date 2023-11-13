package sn.ucad.office.pjobac.modules.jury;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.centre.Centre;
import sn.ucad.office.pjobac.modules.centre.CentreDao;
@Component
@RequiredArgsConstructor
@Slf4j
public class JuryMapperUtil {
    private final CentreDao centreDao;

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
    }
