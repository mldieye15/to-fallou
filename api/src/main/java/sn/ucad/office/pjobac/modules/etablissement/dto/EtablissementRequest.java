package sn.ucad.office.pjobac.modules.etablissement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EtablissementRequest {
    private String libelleLong;
    private String code;
    private String typeEtablissement;
    private String ville;
}
