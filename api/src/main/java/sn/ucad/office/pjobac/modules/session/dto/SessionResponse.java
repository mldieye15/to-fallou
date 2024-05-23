package sn.ucad.office.pjobac.modules.session.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sn.ucad.office.pjobac.modules.annee.Annee;
import sn.ucad.office.pjobac.modules.typeSession.TypeSession;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SessionResponse {
    private Long id;
    private String libelleLong;
    private Date dateDebut;
    private Date dateFin;
    private Integer nombreDemandeAutorise;
    private Integer delaisValidation;
    private Date dateOuvertureDepotCandidature;
    private Date dateClotureDepotCandidature;
    private Annee annee;
    private TypeSession typeSession;
    private  boolean ouvert;
    private  boolean candidature;
    private  boolean modification;
    private boolean phaseTwo;
}
