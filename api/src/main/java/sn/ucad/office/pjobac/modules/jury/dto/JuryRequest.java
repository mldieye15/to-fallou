package sn.ucad.office.pjobac.modules.jury.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JuryRequest {
    private String nom;
    private String numero;
    private String centre;
//    private String session;
    private boolean technique;
}
