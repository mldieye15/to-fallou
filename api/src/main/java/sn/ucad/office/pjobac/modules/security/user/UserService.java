package sn.ucad.office.pjobac.modules.security.user;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.security.user.dto.*;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<UserResponse> all() throws BusinessResourceException;
    public List<AdminResponse> admin() throws BusinessResourceException;
    public List<AdminResponse> planificateur() throws BusinessResourceException;
    public List<AdminResponse> supervisseur() throws BusinessResourceException;
    public List<AdminResponse> user() throws BusinessResourceException;
    public SimplePage<UserResponse> all(Pageable pageable) throws BusinessResourceException;
    public Optional<UserResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;
    UserResponse add(UserRequest request) throws BusinessResourceException;
    AdminResponse addAdmin(AdminRequest request) throws BusinessResourceException;
    AdminResponse addSupervisseur(AdminRequest request) throws BusinessResourceException;
    AdminResponse addPlanificateur(AdminRequest request) throws BusinessResourceException;
    UserResponse maj(UserRequest request, String id)  throws NumberFormatException, BusinessResourceException;
    AdminResponse majAdmin(AdminEditRequest request, String id)  throws NumberFormatException, BusinessResourceException;
    public String del(String id) throws NumberFormatException, BusinessResourceException;
    public Optional<UserAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
    public Optional<AppUser> userByUsername(String username) throws BusinessResourceException;
    public Optional<AppUser> userByEmail(String email) throws BusinessResourceException;
    void addRoleToUser(String username, String nom) throws BusinessResourceException;
    void addRoleToUser(RoleToUserRequest request) throws BusinessResourceException;
    AppUser addForAuthService(UserRequest request) throws BusinessResourceException;
    AppUser addAdminForAuthService(AdminRequest request) throws BusinessResourceException;
    public AppUser activeUser(AppUser user, Boolean action) throws BusinessResourceException;
    public AppUser resetPassword(AppUser user, String newPassword) throws BusinessResourceException;
    void verifyMatriculeUnique(String matricule) throws BusinessResourceException;

    void verifyEmailUnique(String email)throws  BusinessResourceException;
    void verifyUsernamelUnique(String username)throws  BusinessResourceException;
}
