package sn.ucad.office.pjobac.modules.codification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CodificationRequest {
    private String email;
    private String code;

//    public CodificationRequest(String email) {
//        this.email = email;
//    }
}
