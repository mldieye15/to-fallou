package sn.ucad.office.pjobac.modules.demande.dto;



import sn.ucad.office.pjobac.modules.academie.Academie;
import sn.ucad.office.pjobac.modules.annee.Annee;
import sn.ucad.office.pjobac.modules.centre.Centre;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemande;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.session.Session;
import sn.ucad.office.pjobac.modules.ville.Ville;

import java.time.LocalDateTime;

public interface DemandeDetailsCandidat {
    Long getId();

    AppUser getUser();

    Ville getVille();

    Academie getAcademie();

    Session getSession();

    Centre getCentre();

    EtatDemande getEtatDemande();

    Integer getOrdreArrivee();

    LocalDateTime getDateDemande();

    Integer getNote();

    boolean isAffectable();

}
