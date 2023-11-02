package sn.ucad.office.pjobac.modules.security.refresh;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImp implements RefreshTokenService {
    private final RefreshTokenDao dao;
    private final RefreshTokenMapper mapper;
    @Override
    public RefreshToken generateRefreshToken() throws BusinessResourceException {
        try {
            log.info("Debug 001-generation refresh token");
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setCreatedDate(Instant.now());
            dao.save(refreshToken);

            log.info("Debug 002: token {} generated at {}:  ", refreshToken.getToken(), refreshToken.getCreatedDate());
            return refreshToken;
        } catch (DataIntegrityViolationException e) {
            log.error("Génération refresh token: donnée en doublon ou contrainte non respectée");
            throw new BusinessResourceException("data-error", "Donnée existante ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch(Exception ex){
            log.error("Generation refresh token: Une erreur inatteandue est rencontrée.");
            throw new BusinessResourceException("technical-error", "Erreur technique de génération refresh token. ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void validateRefreshToken(String token) throws BusinessResourceException {
        log.info("Debug 001-vérification refresh token");
        dao.findByToken(token)
                .orElseThrow(
                        () -> new BusinessResourceException("InvalidTokenRefresh","Token invdalide.", HttpStatus.INTERNAL_SERVER_ERROR)
                );
    }

    @Override
    public void deleteRefreshToken(String token) throws BusinessResourceException {
        try {
            log.info("Debug 001-suppression token: {}", token);
            dao.deleteByToken(token);
        } catch (Exception e) {
            log.warn("Token expire.");
            throw new BusinessResourceException("DeltingTokenRefresh","Suppression refresh token impossible.");
        }
    }
}

