package sn.ucad.office.pjobac.modules.demande;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.centre.Centre;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeDetailsCandidat;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemande;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.session.Session;
import sn.ucad.office.pjobac.modules.session.SessionDao;
import sn.ucad.office.pjobac.modules.ville.Ville;

import java.util.List;


@Repository
public interface DemandeDao extends JpaRepository<Demande, Long> {
    @Query("SELECT d FROM Demande d WHERE d.user=:user AND d.session.ouvert = true ")
    List<Demande> findByUser( @Param("user") AppUser user);

    @Transactional
    @Modifying
    @Query("UPDATE Demande d SET d.etatDemande = :obseleteEtat WHERE d.ville.id = :villeId AND d.etatDemande = :enAttenteEtat")
    void demandeObselete(@Param("villeId") Long villeId, @Param("obseleteEtat") EtatDemande obseleteEtat, @Param("enAttenteEtat") EtatDemande enAttenteEtat);

    @Transactional
    @Modifying
    @Query("UPDATE Demande d SET d.etatDemande = :rejeterEtat WHERE d.user.id = :userId AND d.etatDemande <> :valider")
    void rejeterDemande(@Param("userId") Long villeId, @Param("rejeterEtat") EtatDemande rejeterEtat, @Param("valider") EtatDemande validerEtat);
    @Modifying
    @Transactional
    @Query("UPDATE Demande d SET d.etatDemande = :rejeterEtat, d.centre = null WHERE d.id = :demandeId")
    void annulerDemande(@Param("rejeterEtat") EtatDemande rejeterEtat, @Param("demandeId") Long demandeId);
    @Modifying
    @Transactional
    @Query("UPDATE Demande d SET d.etatDemande = :pendingEtat WHERE d.etatDemande=:obsoleteEtat AND d.ville.id = :villeId")
    void pendingDemande(@Param("villeId") Long villeId, @Param("pendingEtat") EtatDemande pendingEtat,@Param("obsoleteEtat") EtatDemande obsoleteEtat);
    @Modifying
    @Transactional
    @Query("UPDATE Demande d SET d.etatDemande = :rejeterEtat, d.centre = null WHERE d.id = :demandeId")
    void nonAffectable(@Param("rejeterEtat") EtatDemande rejeterEtat, @Param("demandeId") Long demandeId);
    @Query("SELECT COUNT(d) > 0 FROM Demande d WHERE d.user = :user AND d.etatDemande.libelleLong = 'acceptée' " +
            "AND d.session.ouvert = true")
    boolean hasAcceptedDemande(@Param("user") AppUser user);
    @Query("SELECT COUNT(d) > 0 FROM Demande d WHERE d.user = :user AND d.proposition = true " +
            "AND d.session.ouvert = true")
    boolean hasPropositionDemande(@Param("user") AppUser user);
    @Query("SELECT d FROM Demande d WHERE d.etatDemande.libelleLong='en attente' AND d.session.ouvert = true")
    List<Demande> demandePending();
    @Query("SELECT d FROM Demande d WHERE d.etatDemande.libelleLong='acceptée' AND d.session.ouvert = true")
    List<Demande> demandeAccepter();
    @Query("SELECT d FROM Demande d WHERE d.ville = :ville  AND d.etatDemande.libelleLong='obsolète' AND d.session.ouvert = true" )
    List<Demande> demandeObseleteByVille(@Param("ville")Ville ville);
    @Query("SELECT d FROM Demande d WHERE d.etatDemande.libelleLong='obsolète' AND d.session.ouvert = true" )
    List<Demande> allDemandeObselete();
    @Query("SELECT d FROM Demande d WHERE d.etatDemande.libelleLong='validée' AND d.session.ouvert = true" )
    List<Demande> allDemandeValider();
    @Query("SELECT d FROM Demande d WHERE d.etatDemande.libelleLong='en attente' AND d.session.ouvert = true AND d.proposition = true")
    List<Demande> allDemandeProposer();
    @Query("SELECT d FROM Demande d WHERE d.ville = :ville AND d.session.ouvert = true" )
    List<Demande> demandeByVille(@Param("ville")Ville ville);
    @Query("SELECT d FROM Demande d WHERE d.centre = :centre AND d.session.ouvert = true")
    List<Demande> demandeByCentre(@Param("centre") Centre centre);
    @Query("SELECT d FROM Demande d WHERE d.session = :session " )
    List<Demande> demandeBySession(@Param("session") Session session);
    @Query("SELECT d FROM Demande d WHERE d.session.ouvert = true" )
    List<Demande> demandeBySession();
    @Query("SELECT COUNT(d) FROM Demande d WHERE d.ville=:ville AND d.session.ouvert = true ")
    int totalDemandeByVille(@Param("ville")Ville ville);
    @Query("SELECT COUNT(d) FROM Demande d WHERE d.centre=:centre AND d.etatDemande.libelleLong='validée' AND d.session.ouvert = true ")
    int totalAffectedByCentre(@Param("centre")Centre centre);
    @Query("SELECT COUNT(d) FROM Demande d WHERE d.centre=:centre AND d.etatDemande.libelleLong='validée' AND d.jury IS NOT NULL AND d.session.ouvert = true")
    int totalAffectedJuryByCentre(@Param("centre") Centre centre);




}