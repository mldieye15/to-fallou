package sn.ucad.office.pjobac.modules.typeEtablissement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TypeEtablissementAudit {
    private Long id;
    private String libelleLong;
    private String libelleCourt;
    private String auteur;
    private LocalDateTime dateCreation;
    private String modificateur;
    private LocalDateTime dateModification;
}