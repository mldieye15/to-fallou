package sn.ucad.office.pjobac.modules.security.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleToUserRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String role;
}
