package sn.ucad.office.pjobac.modules.typeEtablissement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.etablissement.Etablissement;
import sn.ucad.office.pjobac.modules.etablissement.EtablissementDao;
import sn.ucad.office.pjobac.modules.fonction.Fonction;
import sn.ucad.office.pjobac.modules.fonction.FonctionDao;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class TypeEtablissementMapperUtil {
    private final FonctionDao fonctionDao;
    private final EtablissementDao etablissementDao;

    @Named("getFonctionById")
    Fonction getFonctionById(String fonctionId) throws NumberFormatException {
        try {
            Long myId = Long.valueOf(fonctionId.trim());
            Fonction response;
            response = fonctionDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune fonction avec " + fonctionId + " trouvée.", HttpStatus.NOT_FOUND)
                    );
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id {} non autorisé. <EtablissementMapperUtil::getFonctionById>.", fonctionId);
            throw new BusinessResourceException("not-valid-param", "Paramétre " + fonctionId + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }
}
