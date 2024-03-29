package sn.ucad.office.pjobac.modules.security.user.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sn.ucad.office.pjobac.modules.etablissement.Etablissement;
import sn.ucad.office.pjobac.modules.fonction.Fonction;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequest {
    private String matricule;
    private String prenoms;
    private String nom;
//    private String  dateNaiss;
    private String email;
//    private String username;
    private String mdpasse;
    private String profileImageUrl;
    private String sexe;
    private String telephone;
//    private Integer anciennete;
//    private String fonction;
    private String etablissement;
    private String code;
    private  String banque;
    private  String codeBanque;
    private  String codeAgence;
    private  String numeroCompte;
    private  String cleRib;

}
