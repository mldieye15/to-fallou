package sn.ucad.office.pjobac.modules.canditatAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CandidatAuthoriserAudit {
    private Long id;
    private String code;
    private String auteur;
    private LocalDateTime dateCreation;
    private String modificateur;
    private LocalDateTime dateModification;
}
