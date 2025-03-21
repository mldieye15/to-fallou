package sn.ucad.office.pjobac.modules.typeSession.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sn.ucad.office.pjobac.modules.academie.Academie;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VilleResponse {
    private Long id;
    private String libelleLong;
    private String libelleCourt;
    private Academie academie;
    private int totalJury;
    private int totalDemandes;
    private int quota;
    private double rapportJuryDemande;

}
