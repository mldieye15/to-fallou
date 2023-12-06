package sn.ucad.office.pjobac.modules.annee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AnneeResponse {
    private Long id;
    private String libelleLong;
    private  boolean encours;
}
