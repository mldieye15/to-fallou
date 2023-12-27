package sn.ucad.office.pjobac.modules.security.token.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDetailsResponse {
    private Long userId;
    private String username;
    private String nom;
    private String role;
}
