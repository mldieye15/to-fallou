package sn.ucad.office.pjobac.modules.detailsCandidat.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sn.ucad.office.pjobac.modules.annee.Annee;
import sn.ucad.office.pjobac.modules.security.user.AppUser;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetailsCandidatResponse {
    private Long id;
    private AppUser candidat;
    private AppUser auteur;
    private Annee annee;
    private int bonus;
    private int malus;
    private int noteFonction;
    private int noteEtablissementProvenance;
    private int noteAnciennete;
    private int note;
    private int noteSupervisseur;
    private String appreciation;
    private boolean affectable;
    protected LocalDateTime dateModification;
}
