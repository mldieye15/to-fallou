package sn.ucad.office.pjobac.modules.security.token;

import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.security.refresh.dto.RefreshTokenRequest;
import sn.ucad.office.pjobac.modules.security.token.dto.AuthenticationResponse;
import sn.ucad.office.pjobac.modules.security.token.dto.LoginRequest;
import sn.ucad.office.pjobac.modules.security.token.dto.UserDetailsResponse;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.security.user.dto.RoleToUserRequest;
import sn.ucad.office.pjobac.modules.security.user.dto.UserRequest;
import sn.ucad.office.pjobac.modules.security.user.dto.UserResponse;

public interface AuthService {
    UserResponse inscrire(UserRequest request) throws BusinessResourceException;
    //public String generateVerificationToken(AppUser user) throws BusinessResourceException;
    public void verifToken(String token) throws BusinessResourceException;
    public void fetchUserAndEnable(VerificationToken verificationToken) throws BusinessResourceException;
    public AppUser getCurrentUser() throws BusinessResourceException;
    public UserDetailsResponse getCurrentUserDetails()throws BusinessResourceException;
    public AuthenticationResponse login(LoginRequest request) throws BusinessResourceException;
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws BusinessResourceException;
    public void deleteRefreshToken(String token) throws BusinessResourceException;
    public void addRoleToUser(RoleToUserRequest request) throws BusinessResourceException;
    public void removeRoleFromUser(String username, String roleName)throws BusinessResourceException;

}
