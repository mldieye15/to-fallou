package sn.ucad.office.pjobac.modules.security.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.exception.ResourceAlreadyExists;
import sn.ucad.office.pjobac.modules.codification.CodificationService;
import sn.ucad.office.pjobac.modules.security.role.AppRole;
import sn.ucad.office.pjobac.modules.security.role.RoleService;
import sn.ucad.office.pjobac.modules.security.user.dto.*;
import sn.ucad.office.pjobac.utils.SimplePage;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImp implements UserService {
    private final UserDao dao;
    private final UserMapper mapper;
    private final RoleService roleService;
    private final CodificationService codificationService;

    @Override
    public List<UserResponse> all() throws BusinessResourceException {
        log.info("Liste des users. <all>");
        List<AppUser> users = dao.findAll();
        List<UserResponse> response;
        response = users.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public List<AdminResponse> admin() throws BusinessResourceException {
        log.info("Liste des administrateurs. <all>");
        List<AppUser> admins = dao.adminRole();
        List<AdminResponse> response;
        response = admins.stream()
                .map(mapper::userToAdminResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public List<AdminResponse> planificateur() throws BusinessResourceException {
        log.info("Liste des planificateurs. <all>");
        List<AppUser> planificateurs = dao.planificateurRole();
        List<AdminResponse> response;
        response = planificateurs.stream()
                .map(mapper::userToAdminResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public List<AdminResponse> supervisseur() throws BusinessResourceException {
        log.info("Liste des supervisseurs. <all>");
        List<AppUser> supervisseurs = dao.supervisseurRole();
        List<AdminResponse> response;
        response = supervisseurs.stream()
                .map(mapper::userToAdminResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public List<AdminResponse> user() throws BusinessResourceException {
            log.info("Liste des supervisseurs. <all>");
            List<AppUser> users = dao.userRole();
            List<AdminResponse> response;
            response = users.stream()
                    .map(mapper::userToAdminResponse)
                    .collect(Collectors.toList());
            return response;
    }

    @Override
    public SimplePage<UserResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("Liste des users avec pagination. <all>");
        final Page<AppUser> page = dao.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable
        );
    }

    @Override
    public Optional<UserResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            AppUser user = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "User avec " + id + " non trouvé(e).", HttpStatus.NOT_FOUND)
                    );
            log.info("User avec id: " + id + " trouvé(e). <oneById>");
            Optional<UserResponse> response;
            response = Optional.ofNullable(mapper.toEntiteResponse(user));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public UserResponse add(UserRequest request) throws BusinessResourceException {
        try {
            log.info("Vérification du code pour l'email {} et le code {}", request.getEmail(), request.getCode());
            boolean isCodeValid = codificationService.verifyCode(request.getCode(), request.getEmail());
            log.info("Résultat de la vérification du code : {}", isCodeValid);
            if (!isCodeValid) {
                log.error("Le code ou l'email n'est pas valide.");
                throw new BusinessResourceException("verification-error", "Le code ou l'email n'est pas valide.", HttpStatus.BAD_REQUEST);
            }
            log.info("Debug 001-request:  " + request.toString());
            AppUser entity = mapper.requestToEntity(request);
            log.info("Debug 002-request_to_entity:  " + entity.toString());
            UserResponse response = mapper.toEntiteResponse(dao.save(entity));
            log.info("Ajout " + response.getUsername() + " effectué avec succés <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Ajout user: donnée existante ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée existante ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout user: erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un utilisateur: " + request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AdminResponse addAdmin(AdminRequest request) throws BusinessResourceException {
        try {
            log.info("Debug 001-request:  " + request.toString());
            AppUser entity = mapper.adminRequestToUser(request);
            log.info("Debug 002-request_to_entity:  " + entity.toString());
            AdminResponse response = mapper.userToAdminResponse(dao.save(entity));
            log.info("Ajout " + response.getUsername() + " effectué avec succés <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Ajout user: donnée existante ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée existante ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout user: erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un utilisateur: " + request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AdminResponse addSupervisseur(AdminRequest request) throws BusinessResourceException {
        try {
            log.info("Debug 001-request:  " + request.toString());
            AppUser entity = mapper.adminRequestToUser(request);
            log.info("Debug 002-request_to_entity:  " + entity.toString());
            AdminResponse response = mapper.userToAdminResponse(dao.save(entity));
            log.info("Ajout " + response.getUsername() + " effectué avec succés <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Ajout user: donnée existante ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée existante ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout user: erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un utilisateur: " + request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AdminResponse addPlanificateur(AdminRequest request) throws BusinessResourceException {
        try {
            log.info("Debug 001-request:  " + request.toString());
            AppUser entity = mapper.adminRequestToUser(request);
            log.info("Debug 002-request_to_entity:  " + entity.toString());
            AdminResponse response = mapper.userToAdminResponse(dao.save(entity));
            log.info("Ajout " + response.getUsername() + " effectué avec succés <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Ajout user: donnée existante ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée existante ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout user: erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un utilisateur: " + request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserResponse maj(UserRequest request, String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            AppUser entiteOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "User avec " + id + " non trouvé(e).", HttpStatus.NOT_FOUND)
                    );
            AppUser entity = mapper.requestToEntiteUp(entiteOptional, request);
            UserResponse response = mapper.toEntiteResponse(dao.save(entity));
            log.info("Mise à jour " + response.getUsername() + " effectué avec succés <maj>");
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Maj role: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de maj role: " + request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AdminResponse majAdmin(AdminRequest request, String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            AppUser entiteOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "User avec " + id + " non trouvé(e).", HttpStatus.NOT_FOUND)
                    );
            AppUser entity = mapper.adminRequestToUserUp(entiteOptional, request);
            AdminResponse response = mapper.userToAdminResponse(dao.save(entity));
            log.info("Mise à jour " + response.getUsername() + " effectué avec succés <maj>");
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Maj role: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de maj role: " + request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public String del(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            AppUser entity = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "User avec " + id + " non trouvé(e).", HttpStatus.NOT_FOUND)
                    );
            dao.deleteById(myId);
            log.info("Role avec id & nom: " + id + " & " + entity.getUsername() + " supprimé avec succés. <del>");
            String response;
            response = "Role: " + entity.getUsername() + " supprimée avec succés. <del>";
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<UserAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            AppUser entity = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "User avec " + id + " non trouvé(e).", HttpStatus.NOT_FOUND)
                    );
            log.info("User avec id: " + id + " trouvé. <auditOneById>");
            Optional<UserAudit> response;
            response = Optional.ofNullable(mapper.toEntiteAudit(entity, "admin", "admin"));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<AppUser> userByUsername(String username) throws BusinessResourceException {
        try {
            Optional<AppUser> response = dao.findByUsername(username);
            log.info("User avec username: " + username + " trouvé. <userBy username>");
            return response;
        } catch (Exception ex) {
            log.error("User by username: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("not-found", "Role avec nom: " + username + " non trouvé(e).", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void addRoleToUser(String username, String nom) throws BusinessResourceException {
        try {
            AppUser user = this.userByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur avec: " + username + " trouve."));//.get();
            AppRole role = roleService.roleByNom(nom).orElseThrow(() -> new RoleNotFoundException("Aucun role avec: " + nom + " trouve."));//.get();
            user.getRoles().add(role);
            log.warn("{} affected to username: {} with <addRoleToUser>", nom, username);
        } catch (Exception ex) {
            log.error("User by username: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("not-found", "Role avec nom: " + username + " non trouvé(e).", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void addRoleToUser(RoleToUserRequest request) throws BusinessResourceException {
        try {
            AppUser user = this.userByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur avec: " + request.getUsername() + " trouve."));//.get();
            AppRole role = roleService.roleByNom(request.getRole()).orElseThrow(() -> new RoleNotFoundException("Aucun role avec: " + request.getRole() + " trouve."));//.get();
            user.getRoles().add(role);
            log.info("{} affected to username: {} with <addRoleToUser>", request.getRole(), request.getUsername());
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Affectation role to user: donnée existante ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée existante ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Affectation role to user: erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique d'affectation role to user': " + request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AppUser addForAuthService(UserRequest request) throws BusinessResourceException {
        try {
            log.info("Debug 001-request:  " + request.toString());
            AppUser entity = mapper.requestToEntity(request);
            log.info("Debug 002-request_to_entity:  " + entity.toString());
            AppUser response = dao.save(entity);
            log.info("Ajout " + response.getUsername() + " effectué avec succés <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Ajout user: donnée existante ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée existante ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout user: erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un utilisateur: " + request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AppUser addAdminForAuthService(AdminRequest request) throws BusinessResourceException {
        try {
            log.info("Debug 001-request:  " + request.toString());
            AppUser entity = mapper.adminRequestToUser(request);
            log.info("Debug 002-request_to_entity:  " + entity.toString());
            AppUser response = dao.save(entity);
            log.info("Ajout " + response.getUsername() + " effectué avec succés <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Ajout user: donnée existante ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée existante ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout user: erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un utilisateur: " + request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public AppUser activeUser(AppUser user, Boolean action) throws BusinessResourceException {
        try {
            if (user.getId() == null) {
                log.warn("Aucun utilisateur trouvé. <activeUser>");
                throw new BusinessResourceException("NotFoundUser", "Utilisateur non trouvé.", HttpStatus.NOT_FOUND);
            }

            user.setEnabled(action);
            AppUser result = dao.saveAndFlush(user);
            log.info("Utilisateur active/desactive: " + user.getUsername() + " avec succes. <maj>.");
            return result;
        } catch (NoSuchElementException e) {
            log.warn("Aucun utilisateur avec  trouvé. <activeUser>.");
            throw new BusinessResourceException("NotFound", "Utilisateur non trouvé.", HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            log.error("Activation utilisateur: Une erreur technique est rencontree - donnee en doublon ou contrainte non respectée ");
            throw new BusinessResourceException("SqlError", "Une erreur technique est rencontrée: donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Activation utilisateur: Une erreur inattandue est rencontree.");
            throw new BusinessResourceException("UpdateError", "Erreur technique d'activation d'un utilisateur: ", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void verifyMatriculeUnique(String matricule) throws BusinessResourceException {
        if (dao.findByMatricule(matricule).isPresent()) {
            throw new ResourceAlreadyExists("Le matricule existe déjà.");
        }
    }

    @Override
    public void verifyEmailUnique(String email) throws BusinessResourceException {
        if (dao.findByEmail(email).isPresent()) {
            throw new ResourceAlreadyExists("Le email existe déjà.");
        }

    }
    @Override
    public void verifyUsernamelUnique(String username) throws BusinessResourceException {
        if(dao.findByUsername(username).isPresent()){
            throw new ResourceAlreadyExists("Le username  existe déjà.");
        }

    }
}
