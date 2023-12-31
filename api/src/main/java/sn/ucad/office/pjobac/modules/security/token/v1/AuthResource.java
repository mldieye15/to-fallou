package sn.ucad.office.pjobac.modules.security.token.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ucad.office.pjobac.modules.security.refresh.dto.RefreshTokenRequest;
import sn.ucad.office.pjobac.modules.security.token.AuthService;
import sn.ucad.office.pjobac.modules.security.token.dto.AuthenticationResponse;
import sn.ucad.office.pjobac.modules.security.token.dto.LoginRequest;
import sn.ucad.office.pjobac.modules.security.token.dto.UserDetailsResponse;
import sn.ucad.office.pjobac.modules.security.user.dto.RoleToUserRequest;
import sn.ucad.office.pjobac.modules.security.user.dto.UserRequest;
import sn.ucad.office.pjobac.modules.security.user.dto.UserResponse;

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
}

