package sn.ucad.office.pjobac.modules.security.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.security.user.AppUser;

import java.util.Optional;
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class VerificationTokenServiceImp implements VerificationTokenService {
    private final VerificationTokenDao dao;
    private final VerificationTokenMapper mapper;
    @Override
    public String genVerifToken(AppUser user) throws BusinessResourceException {
        try {
            log.info("Debug 001-generation verifcation token for:  "+user.getUsername());
            VerificationToken verificationToken = dao.save( mapper.getVerifToken(user) ); //mapper.getVerifToken(user);
            log.info("Debug 002: token {} generated for user {}:  ", verificationToken.getToken(), user.getUsername());
            return verificationToken.getToken();
        } catch (DataIntegrityViolationException e) {
            log.error("Génération token de vérification: donnée en doublon ou contrainte non respectée");
            throw new BusinessResourceException("data-error", "Donnée existante ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch(Exception ex){
            log.error("Generation token de verification: Une erreur inatteandue est rencontrée.");
            throw new BusinessResourceException("technical-error", "Erreur technique de génération token: "+user.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) throws BusinessResourceException {
        try {
            Optional<VerificationToken> response = dao.findByToken(token);
            log.info("Token: "+token+" trouvé. <findByToken>");
            return response;
        } catch(Exception ex){
            log.error("User by username: Une erreur inattandue est rencontrée."+ex.toString());
            throw new BusinessResourceException("not-found", "Token: "+token+" non trouvé(e).", HttpStatus.NOT_FOUND);
        }
    }
}

