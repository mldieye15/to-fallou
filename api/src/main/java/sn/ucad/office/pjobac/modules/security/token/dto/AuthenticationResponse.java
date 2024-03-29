package sn.ucad.office.pjobac.modules.security.token.dto;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AuthenticationResponse {
    private String authenticationToken;
    private String refreshToken;
    private Instant expiresAt;
    private String email;
    private String role;
    private String fullname;
    private String photo;
    private String initiale;
}
