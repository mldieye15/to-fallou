package sn.ucad.office.pjobac.modules.demande.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sn.ucad.office.pjobac.modules.academie.Academie;
import sn.ucad.office.pjobac.modules.annee.Annee;
import sn.ucad.office.pjobac.modules.centre.Centre;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemande;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.session.Session;
import sn.ucad.office.pjobac.modules.ville.Ville;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DemandeDetailsCandidatResponse{
    private Long demandeId;
    private AppUser user;
    private Ville ville;
    private Academie academie;
    private Session session;
    private Centre centre;
    private EtatDemande etatDemande;
    private Integer ordreArrivee;
    private LocalDateTime dateDemande;
    private LocalDateTime dateRejetDemande;
    private LocalDateTime dateConfirmationDemande;
    private Long detailsCandidatId;
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
}
