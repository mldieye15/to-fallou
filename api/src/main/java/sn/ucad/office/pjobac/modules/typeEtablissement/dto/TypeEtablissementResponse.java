package sn.ucad.office.pjobac.modules.typeEtablissement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sn.ucad.office.pjobac.modules.fonction.Fonction;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TypeEtablissementResponse {
    private Long id;
    private String libelleLong;
    private String libelleCourt;
    private String nombrePoint;
    private Fonction fonction;
}
