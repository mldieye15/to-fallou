package sn.ucad.office.pjobac.modules.security.refresh;

import sn.ucad.office.pjobac.exception.BusinessResourceException;

public interface RefreshTokenService {
    public RefreshToken generateRefreshToken() throws BusinessResourceException;
    public void validateRefreshToken(String token) throws BusinessResourceException;
    public void deleteRefreshToken(String token) throws BusinessResourceException;
}
