package sn.ucad.office.pjobac.modules.session.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sn.ucad.office.pjobac.modules.annee.Annee;
import sn.ucad.office.pjobac.modules.typeSession.TypeSession;

import java.time.LocalDate;

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
    private Annee   annee;
    private TypeSession typeSession;
}
