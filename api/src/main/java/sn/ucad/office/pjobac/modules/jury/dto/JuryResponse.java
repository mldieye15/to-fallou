package sn.ucad.office.pjobac.modules.jury.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sn.ucad.office.pjobac.modules.centre.Centre;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JuryResponse {
    private Long id;
    private String numero;
    private Centre centre;
}
