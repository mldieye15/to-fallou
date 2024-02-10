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
public class DemandeRequest {
    private String academie;
    private String ville;
    private String session;
    private LocalDateTime dateDemande;
}
