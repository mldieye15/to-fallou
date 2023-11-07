package sn.ucad.office.pjobac.modules.ville;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.academie.Academie;
import sn.ucad.office.pjobac.modules.academie.AcademieDao;
import sn.ucad.office.pjobac.modules.typeCentre.TypeCentre;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class VilleMapperUtil {
    private final AcademieDao academieDao;


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
}


