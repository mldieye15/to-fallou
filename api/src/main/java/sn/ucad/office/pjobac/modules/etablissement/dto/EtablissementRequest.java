package sn.ucad.office.pjobac.modules.etablissement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sn.ucad.office.pjobac.modules.typeEtablissement.TypeEtablissement;
import sn.ucad.office.pjobac.modules.ville.Ville;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EtablissementRequest {
    private String libelleLong;
    private String code;
    private TypeEtablissement typeEtablissement;
    private Ville ville;
}
