package sn.ucad.office.pjobac.modules.demande.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sn.ucad.office.pjobac.modules.academie.Academie;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemande;
import sn.ucad.office.pjobac.modules.session.Session;
import sn.ucad.office.pjobac.modules.ville.Ville;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DemandeResponse {
    private Long id;
    private Ville ville;
    private Session session;
    private Academie academie;
    private EtatDemande etatDemande;
    private LocalDateTime dateDemande=LocalDateTime.now();
}
