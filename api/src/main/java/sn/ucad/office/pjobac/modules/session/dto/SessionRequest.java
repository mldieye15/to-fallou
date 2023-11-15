package sn.ucad.office.pjobac.modules.session.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SessionRequest {
    private String  libelleLong;
    private String  dateDeDebut;
    private String  dateDeFin;
    private Integer nombreDemandeAutorise;
    private Integer delaisValidation;
    private String  dateDeOuvertureDepotCandidature;
    private String  dateDeClotureDepotCandidature;
    private String   annee;
    private String typeSession;
}
