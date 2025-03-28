package sn.ucad.office.pjobac.modules.jury.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sn.ucad.office.pjobac.modules.centre.Centre;
import sn.ucad.office.pjobac.modules.session.Session;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JuryResponse {
    private Long id;
    private String nom;
    private String numero;
    private Centre centre;
    private boolean technique;
    private Session session;
}
