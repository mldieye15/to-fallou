package sn.ucad.office.pjobac.modules.canditatAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CandidatAuthoriserResponse {
    private String id;
    private String code;
    private String matricule;
    private String prenoms;
    private String nom;
    private String telephone;
    private String etablisement;
    private Long utiCree;
    protected LocalDateTime dateCreation ;
    protected Long utiModifie;
    protected LocalDateTime dateModification;
}
