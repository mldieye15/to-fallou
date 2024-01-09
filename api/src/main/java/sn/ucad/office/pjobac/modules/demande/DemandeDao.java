package sn.ucad.office.pjobac.modules.demande;

import jakarta.transaction.Transactional;
import org.hibernate.sql.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.centre.Centre;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeDetailsCandidat;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemande;
import sn.ucad.office.pjobac.modules.security.user.AppUser;

import java.util.List;
import java.util.Optional;


@Repository
public interface DemandeDao extends JpaRepository<Demande, Long> {
    @Query(value = "SELECT d.id, v.*, s.*,u.nom as nom,a.*,c.*,e.*, d.date_creation, dc.affectable,dc.note " +
            "FROM demande d " +
            "LEFT JOIN details_candidat dc ON d.user_id = dc.candidat_id " +
            "LEFT JOIN ville v ON d.ville_id = v.id " +
            "LEFT JOIN academie a  ON d.academie_id = a.id " +
            "LEFT JOIN centre c  ON d.Centre_id = c.id " +
            "LEFT JOIN users u ON d.user_id = u.id " +
            "LEFT JOIN etat_demande e  ON d.etat_demande_id = e.id " +
            "LEFT JOIN session s ON d.session_id = s.id", nativeQuery = true)
    List<DemandeDetailsCandidat> demandesWithAffectable();

    List<Demande> findByUser(AppUser user);

    @Transactional
    @Modifying
    @Query("UPDATE Demande d SET d.etatDemande = :obseleteEtat WHERE d.ville.id = :villeId AND d.etatDemande = :enAttenteEtat")
    void demandeObselete(@Param("villeId") Long villeId, @Param("obseleteEtat") EtatDemande obseleteEtat, @Param("enAttenteEtat") EtatDemande enAttenteEtat);
    @Transactional
    @Modifying
    @Query("UPDATE Demande d SET d.etatDemande = :rejeterEtat WHERE d.user.id = :userId AND d.etatDemande <> :valider")
    void rejeterDemande(@Param("userId") Long villeId, @Param("rejeterEtat") EtatDemande rejeterEtat, @Param("valider") EtatDemande validerEtat);
    @Query("SELECT COUNT(d) > 0 FROM Demande d WHERE d.user = :user AND d.etatDemande.libelleLong = 'ACCEPTE'")
    boolean hasAcceptedDemande(@Param("user") AppUser user);

}