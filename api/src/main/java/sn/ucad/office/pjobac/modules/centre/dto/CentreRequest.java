package sn.ucad.office.pjobac.modules.centre.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CentreRequest {
    private String libelleLong;
    private String libelleCourt;
    private String ville;
    private String typeCentre;
}
