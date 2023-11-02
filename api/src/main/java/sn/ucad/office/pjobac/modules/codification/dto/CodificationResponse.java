package sn.ucad.office.pjobac.modules.codification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CodificationResponse {
    private Long id;
    private String email;
    private String code;
}
