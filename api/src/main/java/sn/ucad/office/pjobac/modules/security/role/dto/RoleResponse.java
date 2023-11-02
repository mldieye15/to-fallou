package sn.ucad.office.pjobac.modules.security.role.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleResponse {
    private Long id;
    private String nom;
}
