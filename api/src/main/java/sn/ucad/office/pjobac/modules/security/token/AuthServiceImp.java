package sn.ucad.office.pjobac.modules.security.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.exception.ResourceAlreadyExists;
import sn.ucad.office.pjobac.modules.security.mail.MailService;
import sn.ucad.office.pjobac.modules.security.mail.NotificationEmail;
import sn.ucad.office.pjobac.modules.security.mail.NotificationEmailHtml;
import sn.ucad.office.pjobac.modules.security.refresh.RefreshTokenService;
import sn.ucad.office.pjobac.modules.security.refresh.dto.RefreshTokenRequest;
import sn.ucad.office.pjobac.modules.security.role.AppRole;
import sn.ucad.office.pjobac.modules.security.role.RoleService;
import sn.ucad.office.pjobac.modules.security.token.dto.AuthenticationResponse;
import sn.ucad.office.pjobac.modules.security.token.dto.LoginRequest;
import sn.ucad.office.pjobac.modules.security.token.dto.UserDetailsResponse;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.security.user.UserMapper;
import sn.ucad.office.pjobac.modules.security.user.UserService;
import sn.ucad.office.pjobac.modules.security.user.dto.*;
import sn.ucad.office.pjobac.utils.EncryptionUtils;
import sn.ucad.office.pjobac.utils.JwtProvider;

import javax.management.relation.RoleNotFoundException;
import java.time.Instant;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthServiceImp implements AuthService {
    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper mapper;
    private final MailService mailService;
    private final VerificationTokenService verifTokenService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public UserResponse inscrire(UserRequest request) throws BusinessResourceException {
        try{
            AppUser entity = userService.addForAuthService(request);
            log.info("Debug 001-generation verifcation token:  "+entity.toString());
            //  generation token
            String token = verifTokenService.genVerifToken(entity);
//            mailService.sendMail(new NotificationEmail(
//                    AppConstants.MESS_ACTIV_COMPTE_OBJET,
//                    entity.getEmail(),
//                    AppConstants.MESS_ACTIV_COMPTE_CONTENT +
//                            AppConstants.LIEN_ACTIV_COMPTE+"/"+token
//            ));
            NotificationEmailHtml notificationEmail = new NotificationEmailHtml();
            notificationEmail.setSubject("Activation du compte");
            notificationEmail.setRecipient(entity.getEmail());
            notificationEmail.setTemplateName("validationCompte.html");
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("prenom", entity.getPrenoms());
            emailVariables.put("nom", entity.getNom());
            emailVariables.put("lien", AppConstants.LIEN_ACTIV_COMPTE+"/"+token);
            notificationEmail.setEmailVariables(emailVariables);

            mailService.sendHtmlEmail(notificationEmail);
//            userService.addRoleToUser(entity.getUsername(), "ROLE_USER");
            RoleToUserRequest roleRequest = new RoleToUserRequest();
            roleRequest.setEmail(request.getEmail());
            roleRequest.setRole("ROLE_USER");
            userService.addRoleToUser(roleRequest);
            UserResponse response;
            response = mapper.toEntiteResponse(entity);
            //  Activation automatique à enlever une fois le probl

            //
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Ajout user: donnée existante ou contrainte non respectée"+e.toString());
            throw new BusinessResourceException("data-error", "Donnée existante ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch(Exception ex){
            log.error("Ajout user: erreur inattandue est rencontrée."+ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un utilisateur: "+request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AdminResponse addAdmin(AdminRequest request) throws BusinessResourceException {
        try{
            AppUser entity = userService.addAdminForAuthService(request);
            log.info("Debug 001-generation verifcation token:  "+entity.toString());
            //  generation token
            String token = verifTokenService.genVerifToken(entity);
//            mailService.sendMail(new NotificationEmail(
//                    AppConstants.MESS_ACTIV_COMPTE_OBJET,
//                    entity.getEmail(),
//                    AppConstants.MESS_ACTIV_COMPTE_CONTENT +
//                            AppConstants.LIEN_ACTIV_COMPTE+"/"+token
//            ));
            NotificationEmailHtml notificationEmail = new NotificationEmailHtml();
            notificationEmail.setSubject("Activation du compte");
            notificationEmail.setRecipient(entity.getEmail());
            notificationEmail.setTemplateName("validationCompte.html");
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("prenom", entity.getPrenoms());
            emailVariables.put("nom", entity.getNom());
            emailVariables.put("lien", AppConstants.LIEN_ACTIV_COMPTE+"/"+token);
            notificationEmail.setEmailVariables(emailVariables);

            mailService.sendHtmlEmail(notificationEmail);
            RoleToUserRequest roleRequest = new RoleToUserRequest();
            roleRequest.setEmail(request.getEmail());
            roleRequest.setRole("ROLE_ADMIN");
            userService.addRoleToUser(roleRequest);
            AdminResponse response;
            response = mapper.userToAdminResponse(entity);
            //  Activation automatique à enlever une fois le probl

            //
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Ajout user: donnée existante ou contrainte non respectée"+e.toString());
            throw new BusinessResourceException("data-error", "Donnée existante ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch(Exception ex){
            log.error("Ajout user: erreur inattandue est rencontrée."+ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un utilisateur: "+request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AdminResponse addPlanificateur(AdminRequest request) throws BusinessResourceException {
        try{
            AppUser entity = userService.addAdminForAuthService(request);
            log.info("Debug 001-generation verifcation token:  "+entity.toString());
            //  generation token
            String token = verifTokenService.genVerifToken(entity);
//            mailService.sendMail(new NotificationEmail(
//                    AppConstants.MESS_ACTIV_COMPTE_OBJET,
//                    entity.getEmail(),
//                    AppConstants.MESS_ACTIV_COMPTE_CONTENT +
//                            AppConstants.LIEN_ACTIV_COMPTE+"/"+token
//            ));
            NotificationEmailHtml notificationEmail = new NotificationEmailHtml();
            notificationEmail.setSubject("Activation du compte");
            notificationEmail.setRecipient(entity.getEmail());
            notificationEmail.setTemplateName("validationCompte.html");
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("prenom", entity.getPrenoms());
            emailVariables.put("nom", entity.getNom());
            emailVariables.put("lien", AppConstants.LIEN_ACTIV_COMPTE+"/"+token);
            notificationEmail.setEmailVariables(emailVariables);

            mailService.sendHtmlEmail(notificationEmail);
            RoleToUserRequest roleRequest = new RoleToUserRequest();
            roleRequest.setEmail(request.getEmail());
            roleRequest.setRole("ROLE_PLANIFICATEUR");
            userService.addRoleToUser(roleRequest);
            AdminResponse response;
            response = mapper.userToAdminResponse(entity);
            //  Activation automatique à enlever une fois le probl

            //
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Ajout user: donnée existante ou contrainte non respectée"+e.toString());
            throw new BusinessResourceException("data-error", "Donnée existante ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch(Exception ex){
            log.error("Ajout user: erreur inattandue est rencontrée."+ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un utilisateur: "+request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AdminResponse addSupervisseur(AdminRequest request) throws BusinessResourceException {
        try{
            AppUser entity = userService.addAdminForAuthService(request);
            log.info("Debug 001-generation verifcation token:  "+entity.toString());
            //  generation token
            String token = verifTokenService.genVerifToken(entity);
//            mailService.sendMail(new NotificationEmail(
//                    AppConstants.MESS_ACTIV_COMPTE_OBJET,
//                    entity.getEmail(),
//                    AppConstants.MESS_ACTIV_COMPTE_CONTENT +
//                            AppConstants.LIEN_ACTIV_COMPTE+"/"+token
//            ));
            NotificationEmailHtml notificationEmail = new NotificationEmailHtml();
            notificationEmail.setSubject("Activation du compte");
            notificationEmail.setRecipient(entity.getEmail());
            notificationEmail.setTemplateName("validationCompte.html");
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("prenom", entity.getPrenoms());
            emailVariables.put("nom", entity.getNom());
            emailVariables.put("lien", AppConstants.LIEN_ACTIV_COMPTE+"/"+token);
            notificationEmail.setEmailVariables(emailVariables);

            mailService.sendHtmlEmail(notificationEmail);
            RoleToUserRequest roleRequest = new RoleToUserRequest();
            roleRequest.setEmail(request.getEmail());
            roleRequest.setRole("ROLE_SUPERVISSEUR");
            userService.addRoleToUser(roleRequest);
            AdminResponse response;
            response = mapper.userToAdminResponse(entity);
            //  Activation automatique à enlever une fois le probl

            //
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Ajout user: donnée existante ou contrainte non respectée"+e.toString());
            throw new BusinessResourceException("data-error", "Donnée existante ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch(Exception ex){
            log.error("Ajout user: erreur inattandue est rencontrée."+ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un utilisateur: "+request.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void verifToken(String token) {
        try {
            Optional<VerificationToken> verificationToken = verifTokenService.findByToken(token);
            log.info("T {} ", verificationToken);
            if(verificationToken.get().getId() == null){
                log.warn("Aucun utilisateur avec " + token + " trouvé. <verifToken>");
                throw new BusinessResourceException("InvalidToken", "Aucun utilisateur trouvé avec ce token", HttpStatus.NOT_ACCEPTABLE);
            }

            log.info("Token trouve et activation en cours. <verifToken>");
            this.fetchUserAndEnable(verificationToken.get());
            //return entityFound;
        } catch (NoSuchElementException e) {
            log.warn("Aucun utilisateur trouve avec le token: " + token + ". <verifToken>.");
            throw new BusinessResourceException("NotValidToken", "Aucun utilisateur avec le token trouve.", HttpStatus.NOT_FOUND);
        } catch (BusinessResourceException e) {
            log.error("Verification token: Une erreur inatteandue est rencontrée.");
            throw new BusinessResourceException("NotFoundToken", "Aucun utilisateur ne correspond à ce token: "+token);
        }
    }
    @Override
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        try {
            String email = verificationToken.getUser().getEmail();
            Optional<AppUser> user = userService.userByEmail(email);
            if(user.get().getId() == null){
                log.warn("Aucun utilisateur trouve avec ce token <fetchUserAndEnable>");
                throw new BusinessResourceException("NotFoundUserByToken", "Aucun utilisateur trouve avec le token.", HttpStatus.NOT_ACCEPTABLE);
            }
            userService.activeUser(user.get(), true);
            log.info("Utilisateur "+user.get().getEmail()+" active avec succes. <fetchUserAndEnable>");
        } catch (NoSuchElementException e) {
            log.warn("Aucun utilisateur trouve avec ce token. <fetchUserAndEnable>.");
            throw new BusinessResourceException("NotValidToken", "Aucun utilisateur avec trouve.", HttpStatus.NOT_ACCEPTABLE);
        } catch (BusinessResourceException e) {
            log.error("Activation utilisateur: Une erreur inattandue est rencontrée. <fetchUserAndEnable>");
            throw new BusinessResourceException("NotValidToken", "Aucun utilisateur ne correspond à ce token.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            String email = user.getUsername();
            return userService.userByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur avec le nom d'utilisateur: " + email + " trouvé."));
        } else {
            // Le principal est probablement de type String (nom d'utilisateur) ou autre objet
            String email = principal.toString();
            return userService.userByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur avec le nom d'utilisateur: " + email + " trouvé."));
        }
    }
    @Override
    public UserDetailsResponse getCurrentUserDetails() throws BusinessResourceException {
        AppUser user = getCurrentUser();

        // Déchiffrer les champs sensibles
        String codeBanqueDecrypte = null;
        String codeAgenceDecrypte = null;
        String numeroCompteDecrypte = null;
        String cleRibDecrypte = null;

        try {
            codeBanqueDecrypte = EncryptionUtils.decrypt(user.getCodeBanque());
            codeAgenceDecrypte = EncryptionUtils.decrypt(user.getCodeAgence());
            numeroCompteDecrypte = EncryptionUtils.decrypt(user.getNumeroCompte());
            cleRibDecrypte = EncryptionUtils.decrypt(user.getCleRib());
        } catch (Exception e) {
            // Gérer les exceptions de déchiffrement ici
            // Par exemple, journaliser l'erreur, renvoyer des valeurs par défaut, etc.
            e.printStackTrace();
        }
        return UserDetailsResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .matricule(user.getMatricule())
                .nom(user.getNom())
                .prenoms(user.getPrenoms())
                .mdpasse(user.getMdpasse()) // Garder le mot de passe chiffré
                .profileImageUrl(user.getProfileImageUrl())
                .sexe(user.getSexe())
                .telephone(user.getTelephone())
                .anciennete(user.getAnciennete())
                .etablissement(user.getEtablissement())
                .code(user.getCode())
                .banque(user.getBanque())
                .codeBanque(codeBanqueDecrypte) // Utiliser le champ déchiffré
                .codeAgence(codeAgenceDecrypte) // Utiliser le champ déchiffré
                .numeroCompte(numeroCompteDecrypte) // Utiliser le champ déchiffré
                .cleRib(cleRibDecrypte) // Utiliser le champ déchiffré
                .build();
    }


    @Override
    public AuthenticationResponse login(LoginRequest request) {
        log.warn("Login request: {} <login>", request.toString());
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        String role;
        role = authenticate.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");

        //  recuperation fullname et photo profile
        Optional<AppUser> userBis = userService.userByEmail(request.getEmail());
        String fullName = "";
        String photo = "";
        String initiale = "";
        if(userBis.isPresent()){
            fullName = userBis.get().getPrenoms()+" "+userBis.get().getNom().toUpperCase(Locale.ROOT);
            photo = userBis.get().getProfileImageUrl();
            initiale = userBis.get().getPrenoms().charAt(0)+""+userBis.get().getNom().charAt(0);
        }
        //
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtexpirationtime()))
                .email(request.getEmail())
                .fullname(fullName)
                .photo(photo)
                .initiale(initiale)
                .role(role)
                .build();
        /*return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtexpirationtime()))
                .username(request.getUsername())
                .build();*/
        /* return new AuthenticationResponse(token, request.getUsername()); */
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws BusinessResourceException {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getEmail());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtexpirationtime()))
                .email(refreshTokenRequest.getEmail())
                .build();
    }
    @Override
    public void deleteRefreshToken(String token) throws BusinessResourceException {
        log.warn("Suppression token: {} <deleteRefreshToken>", token);
        refreshTokenService.deleteRefreshToken(token);
    }

    @Override
    public void addRoleToUser(RoleToUserRequest request) throws BusinessResourceException {
        userService.addRoleToUser(request);
    }

    @Override
    public void removeRoleFromUser(String username, String roleName) throws BusinessResourceException {

    }

    @Override
    public void requestPasswordReset(String email) throws BusinessResourceException, InterruptedException {
        Optional<AppUser> entity = userService.userByEmail(email);
        AppUser user = entity.orElseThrow(() -> new BusinessResourceException("UserNotFound", "Utilisateur non trouvé.", HttpStatus.NOT_FOUND));
        String resetToken = verifTokenService.genVerifToken(user);
        NotificationEmailHtml notificationEmail = new NotificationEmailHtml();
        notificationEmail.setSubject("Réinitialiser le mot de passe");
        notificationEmail.setRecipient(user.getEmail());
        notificationEmail.setTemplateName("reinitialisationMpd.html");
        Map<String, Object> emailVariables = new HashMap<>();
        emailVariables.put("prenom", user.getPrenoms());
        emailVariables.put("nom", user.getNom());
        emailVariables.put("lien", AppConstants.LIEN_RESET_PASSWORD+"/"+resetToken);
        notificationEmail.setEmailVariables(emailVariables);

        mailService.sendHtmlEmail(notificationEmail);
    }
    @Override
    public void fetchUserWithToken(VerificationToken verificationToken, String newPassword) throws BusinessResourceException {
        try {
            String email = verificationToken.getUser().getEmail();
            Optional<AppUser> user = userService.userByEmail(email);
            if(user.get().getId() == null){
                log.warn("Aucun utilisateur trouve avec ce token <fetchUserWithToken>");
                throw new BusinessResourceException("NotFoundUserByToken", "Aucun utilisateur trouve avec le token.", HttpStatus.NOT_ACCEPTABLE);
            }
            userService.resetPassword(user.get(), newPassword);
            log.info("Mot de passe de  "+user.get().getEmail()+" modifier avec succes. <fetchUserWithToken>");
        } catch (NoSuchElementException e) {
            log.warn("Aucun utilisateur trouve avec ce token. <fetchUserAndEnable>.");
            throw new BusinessResourceException("Not ValidToken", "Aucun utilisateur avec trouve.", HttpStatus.NOT_ACCEPTABLE);
        } catch (BusinessResourceException e) {
            log.error("Réinisialisation: Une erreur inattandue est rencontrée. <fetchUserWithToken>");
            throw new BusinessResourceException("Not ValidToken", "Aucun utilisateur ne correspond à ce token.", HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public void resetPassword(String token, String newPassword) throws BusinessResourceException {
        try {
            Optional<VerificationToken> verificationToken = verifTokenService.findByToken(token);
            log.info("T {} ", verificationToken);
            if(verificationToken.get().getId() == null){
                log.warn("Aucun utilisateur avec " + token + " trouvé. <resetPassword>");
                throw new BusinessResourceException("InvalidToken", "Aucun utilisateur trouvé avec ce token", HttpStatus.NOT_ACCEPTABLE);
            }
            log.info("Token trouve et rejet en cours. <resetPassword>");
            this.fetchUserWithToken(verificationToken.get(),newPassword);
        } catch (NoSuchElementException e) {
            log.warn("Aucun utilisateur trouve avec le token: " + token + ". <resetPassword>.");
            throw new BusinessResourceException("Not ValidToken", "Aucun utilisateur avec le token trouve.", HttpStatus.NOT_FOUND);
        } catch (BusinessResourceException e) {
            log.error("Verification token: Une erreur inatteandue est rencontrée.");
            throw new BusinessResourceException("Not FoundToken", "Aucun utilisateur ne correspond à ce token: "+token);
        }

    }
}
