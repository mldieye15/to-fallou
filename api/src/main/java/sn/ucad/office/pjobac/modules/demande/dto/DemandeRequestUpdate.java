package sn.ucad.office.pjobac.modules.demande.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DemandeRequestUpdate {
    private String academie;
    private String ville;
    private String etatDemande;
    private String centre;
    private String jury;
    private String user;
    private LocalDateTime dateDemande;
}
