package sn.ucad.office.pjobac.modules.centre.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sn.ucad.office.pjobac.modules.typeCentre.TypeCentre;
import sn.ucad.office.pjobac.modules.ville.Ville;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CentreResponse {
    private Long id;
    private String libelleLong;
    private String code;
    private Integer nombreJury;
    private Ville ville;
    private TypeCentre typeCentre;

}
