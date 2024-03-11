package sn.ucad.office.pjobac.modules.security.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.exception.ResourceAlreadyExists;
import sn.ucad.office.pjobac.modules.codification.CodificationService;
import sn.ucad.office.pjobac.modules.security.role.RoleService;
import sn.ucad.office.pjobac.modules.security.token.AuthService;
import sn.ucad.office.pjobac.modules.security.user.dto.AdminEditRequest;
import sn.ucad.office.pjobac.modules.security.user.dto.AdminResponse;
import sn.ucad.office.pjobac.modules.security.user.dto.UserEditRequest;
import sn.ucad.office.pjobac.modules.security.user.dto.UserResponse;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProfileUserServiceImp  implements  ProfileUserServie{
    private final UserDao dao;
    private final UserMapper mapper;
    private final AuthService authService;
    private final CodificationService codificationService;
    final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserResponse majProfileUser(UserEditRequest request) throws NumberFormatException, BusinessResourceException {
        try {
            AppUser user= authService.getCurrentUser();
            AppUser entity = mapper.requestToEntiteUp(user, request);
            UserResponse response = mapper.toEntiteResponse(dao.save(entity));
            log.info("Mise à jour " + response.getEmail() + " effectué avec succés <maj>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        }
        catch (Exception ex) {
            log.error("Maj role: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de maj role: " + request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AdminResponse majProfilAdmin(AdminEditRequest request) throws NumberFormatException, BusinessResourceException {
        try {
            AppUser user= authService.getCurrentUser();
            AppUser entity = mapper.adminRequestToUserUp(user, request);
            AdminResponse response = mapper.userToAdminResponse(dao.save(entity));
            log.info("Mise à jour " + response.getEmail() + " effectué avec succés <maj>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Maj role: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de maj role: " + request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
