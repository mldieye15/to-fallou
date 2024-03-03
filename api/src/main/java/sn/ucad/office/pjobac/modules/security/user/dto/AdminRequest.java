package sn.ucad.office.pjobac.modules.security.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminRequest {
    private String matricule;
    private String prenoms;
    private String nom;
//    private String dateNaiss;
    private String email;
//    private String username;
    private String mdpasse;
    private String profileImageUrl;
    private String sexe;
    private String telephone;
}
