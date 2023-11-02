package sn.ucad.office.pjobac.modules.etatDemande.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EtatDemandeResponse {
    private Long id;
    private String libelleLong;
}
