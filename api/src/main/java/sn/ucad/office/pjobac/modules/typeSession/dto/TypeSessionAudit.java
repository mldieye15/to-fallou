package sn.ucad.office.pjobac.modules.typeSession.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TypeSessionAudit {
    private Long id;
    private String libelleLong;
    private String auteur;
    private LocalDateTime dateCreation;
    private String modificateur;
    private LocalDateTime dateModification;
}
