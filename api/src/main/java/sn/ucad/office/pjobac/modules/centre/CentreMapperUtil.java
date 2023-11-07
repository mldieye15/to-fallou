package sn.ucad.office.pjobac.modules.centre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.typeCentre.TypeCentre;
import sn.ucad.office.pjobac.modules.typeCentre.TypeCentreDao;
import sn.ucad.office.pjobac.modules.ville.Ville;
import sn.ucad.office.pjobac.modules.ville.VilleDao;
import sn.ucad.office.pjobac.utils.AppDateFormatter;

import java.text.ParseException;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class CentreMapperUtil {
    private final VilleDao villeDao;
    private final TypeCentreDao typeCentreDao;

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
            log.warn("Paramétre id {} non autorisé. <CentreMapperUtil::getVilleById>.", villeId);
            throw new BusinessResourceException("not-valid-param", "Paramétre " + villeId+ " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }
    @Named("getTypeCentreById")
    TypeCentre getTypeCentreById(String typeCentreId) throws NumberFormatException{
        try {
            Long myId= Long.valueOf(typeCentreId.trim());
            TypeCentre response;
            response= typeCentreDao.findById(myId)
                    .orElseThrow( () ->  new BusinessResourceException("not-found", "Aucune type de centre avec " + typeCentreId + " trouvée.", HttpStatus.NOT_FOUND)

                    );
            return  response;
        }catch (NumberFormatException e){
            log.warn("Paramétre id {} non autorisé. <CentreMapperUtil::getTypeCentreById>.", typeCentreId);
            throw new BusinessResourceException("not-valid-param", "Paramétre " + typeCentreId + " non autorisé.", HttpStatus.BAD_REQUEST);

        }

    }

    @Named("encodeMdp")
    public static String encodeMdp(String mdpClair) throws ParseException {
        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(mdpClair);
    }

    @Named("formatStringToDate")
    public static Date formatStringToDate(String date) throws ParseException {
        final AppDateFormatter dateFormatter = new AppDateFormatter();
        return dateFormatter.dateFormat(date, AppConstants.DATE_FR_FORMAT_SALASH);
        //  entity.setDateNaissance(dateFormatter.dateFormat(req.getDateNaissance(), MesConstants.DATE_FR_FORMAT_SALASH))
    }
}
