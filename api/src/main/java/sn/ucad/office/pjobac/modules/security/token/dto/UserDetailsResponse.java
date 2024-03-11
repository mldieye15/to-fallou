package sn.ucad.office.pjobac.modules.security.token.dto;

import lombok.*;
import sn.ucad.office.pjobac.modules.etablissement.Etablissement;
import sn.ucad.office.pjobac.modules.security.role.dto.RoleResponse;

import java.util.Collection;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDetailsResponse {
    private Long userId;
    private String matricule;
    private String prenoms;
    private String nom;
    //    private Date dateNaiss;
    private String email;
    //    private String username;
    private String mdpasse;
    private String profileImageUrl;
    private String sexe;
    private String telephone;
    private Integer anciennete;
    private Etablissement etablissement;
    private String code;
    private  String banque;
    private  String codeBanque;
    private  String codeAgence;
    private  String numeroCompte;
    private  String cleRib;
    private boolean isEnabled;
    private boolean isLocked;
    private String identifiant;
    private Date lastLoginDate;
    private Date joinDate;
    private Collection<RoleResponse> roles;
}
