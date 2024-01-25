package sn.ucad.office.pjobac.modules.security.token.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.security.refresh.dto.RefreshTokenRequest;
import sn.ucad.office.pjobac.modules.security.token.AuthService;
import sn.ucad.office.pjobac.modules.security.token.VerificationToken;
import sn.ucad.office.pjobac.modules.security.token.dto.AuthenticationResponse;
import sn.ucad.office.pjobac.modules.security.token.dto.LoginRequest;
import sn.ucad.office.pjobac.modules.security.token.dto.UserDetailsResponse;
import sn.ucad.office.pjobac.modules.security.user.dto.*;

@RestController
@RequestMapping("/pjobac/api/auth/v1")
@RequiredArgsConstructor
public class AuthResource {

    private final AuthService service;

    @PostMapping("/inscription")
    public ResponseEntity<UserResponse> addV1(@RequestBody @Valid UserRequest request) {
        UserResponse response = service.inscrire(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping(value = "/addAdmin")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<AdminResponse> addAdmin(@RequestBody @Valid AdminRequest request) {
        AdminResponse response = service.addAdmin(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping(value = "/addPlanificateur")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<AdminResponse> addPlanificateur(@RequestBody @Valid AdminRequest request) {
        AdminResponse response = service.addPlanificateur(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping(value = "/addSupervisseur")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<AdminResponse> addSupervisseur(@RequestBody @Valid AdminRequest request) {
        AdminResponse response = service.addSupervisseur(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping(value = "/verif-token/{token}")
    public ResponseEntity<String> verifToken(@PathVariable String token) {
        service.verifToken(token);
        return new ResponseEntity<>("Activation compte effectué avec succés.", HttpStatus.OK);
    }

    @PostMapping(value = "/connexion")
    public ResponseEntity<AuthenticationResponse>  login(@RequestBody @Valid LoginRequest loginRequest) {
        AuthenticationResponse result = service.login(loginRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/refresh-token")
    public ResponseEntity<AuthenticationResponse>  refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        AuthenticationResponse result = service.refreshToken(refreshTokenRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/deconnexion")
    public ResponseEntity<String> logout(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        service.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK) .body("Merci de votre visite et au revoir!");
    }

    @PostMapping(value = "/role-to-user")
    // @PreAuthorize("hasRole('USER_ROLTOUSER') or hasRole('ADMIN')")
    public ResponseEntity<?> addRoleToUser(@RequestBody @Valid RoleToUserRequest request) {
        service.addRoleToUser(request);
        return ResponseEntity.ok().build();//.body(request.getRole()+" affected to "+request.getUsername());
    }
    @GetMapping("/current")
    public ResponseEntity<UserDetailsResponse> getCurrentUserDetails() {
        UserDetailsResponse userDetailsResponse = service.getCurrentUserDetails();
        return ResponseEntity.ok(userDetailsResponse );
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        try {
            service.requestPasswordReset(email);
            return ResponseEntity.ok("Demande de réinitialisation de mot de passe réussie. Veuillez vérifier votre e-mail.");
        } catch (BusinessResourceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la demande de réinitialisation de mot de passe.");
        }
    }
    @PostMapping("/new-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            service.resetPassword(token, newPassword);
            return ResponseEntity.ok("Mot de passe réinitialisé avec succès");
        } catch (BusinessResourceException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping("/resetWithToken")
    public ResponseEntity<String> resetPasswordWithToken(@RequestBody VerificationToken verificationToken,
                                                         @RequestParam String newPassword) {
        try {
            service.fetchUserWithToken(verificationToken, newPassword);
            return ResponseEntity.ok("Mot de passe réinitialisé avec succès");
        } catch (BusinessResourceException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}

