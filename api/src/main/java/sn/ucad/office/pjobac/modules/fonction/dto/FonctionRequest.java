package sn.ucad.office.pjobac.modules.fonction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FonctionRequest {
    private String libelleLong;
    private String libelleCourt;
    private String nombrePoint;
}
