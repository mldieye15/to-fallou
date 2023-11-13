package sn.ucad.office.pjobac.modules.academie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AcademieResponse {
    private Long id;
    private String libelleLong;
    private String libelleCourt;
}
