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
    AppUser getCandidat();
    Annee getAnnee();
    Integer getBonus();
    Integer getMalus();
    Integer getNoteFonction();
    Integer getNoteEtablissementProvenance();
    Integer getNoteAnciennete();
    Integer getNote();
    Integer getNoteSupervisseur();
    String getAppreciation();
    boolean isAffectable();
    Long getUtiCree();
    LocalDateTime getDateCreation();
    Long getUtiModifie();
    LocalDateTime getDateModification();

    // Champs de Demande
    AppUser getUser();
    String getVille();
    Academie getAcademie();
    Session getSession();
    Centre getCentre();
    EtatDemande getEtatDemande();
//    Integer getOrdreArrivee();
    Integer getDelaisDeValidation();
    LocalDateTime getDateDemande();
    LocalDateTime getDateRejetDemande();
    LocalDateTime getDateConfirmationDemande();
    Long getUtiCreeDemande();
    LocalDateTime getDateCreationDemande();
    Long getUtiModifieDemande();
    LocalDateTime getDateModificationDemande();
}
