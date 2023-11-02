package sn.ucad.office.pjobac.modules.security.token;

import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.security.user.AppUser;

import java.util.Optional;

public interface VerificationTokenService {
    public String genVerifToken(AppUser user) throws BusinessResourceException;

    Optional<VerificationToken> findByToken(String token) throws BusinessResourceException;
}
