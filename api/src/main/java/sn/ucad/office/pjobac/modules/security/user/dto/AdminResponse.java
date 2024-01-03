package sn.ucad.office.pjobac.modules.security.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sn.ucad.office.pjobac.modules.security.role.dto.RoleResponse;

import java.util.Collection;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminResponse {
    private Long id;
    private String matricule;
    private String prenoms;
    private String nom;
    private Date dateNaiss;
    private String email;
    private String username;
    private String mdpasse;
    private String profileImageUrl;
    private String sexe;
    private String telephone;
    private boolean isEnabled;
    private boolean isLocked;
    private String identifiant;
    private Date lastLoginDate;
    private Date joinDate;
    private Collection<RoleResponse> roles;
}
