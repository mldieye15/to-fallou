package sn.ucad.office.pjobac.modules.centre.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CentreResponse {
    private Long id;
    private String libelleLong;
    private String libelleCourt;
}
