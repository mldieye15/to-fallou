package sn.ucad.office.pjobac.modules.security.user;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.security.user.dto.RoleToUserRequest;
import sn.ucad.office.pjobac.modules.security.user.dto.UserAudit;
import sn.ucad.office.pjobac.modules.security.user.dto.UserRequest;
import sn.ucad.office.pjobac.modules.security.user.dto.UserResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<UserResponse> all() throws BusinessResourceException;
    public SimplePage<UserResponse> all(Pageable pageable) throws BusinessResourceException;
    public Optional<UserResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;
    UserResponse add(UserRequest request) throws BusinessResourceException;
    UserResponse maj(UserRequest request, String id)  throws NumberFormatException, BusinessResourceException;
    public String del(String id) throws NumberFormatException, BusinessResourceException;
    public Optional<UserAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
    public Optional<AppUser> userByUsername(String username) throws BusinessResourceException;
    void addRoleToUser(String username, String nom) throws BusinessResourceException;
    void addRoleToUser(RoleToUserRequest request) throws BusinessResourceException;
    AppUser addForAuthService(UserRequest request) throws BusinessResourceException;
    public AppUser activeUser(AppUser user, Boolean action) throws BusinessResourceException;
}
