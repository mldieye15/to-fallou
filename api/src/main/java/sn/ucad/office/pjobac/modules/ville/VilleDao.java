package sn.ucad.office.pjobac.modules.ville;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.academie.Academie;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.typeSession.TypeSession;

import java.util.List;
import java.util.Optional;


@Repository
public interface VilleDao extends JpaRepository<Ville, Long> {
    List<Ville> findByAcademie(Academie academie);

    Optional<Ville> findByLibelleLong(String libelleLong);
    @Query("SELECT v FROM Ville v WHERE v.totalJury>0")
    List<Ville> allWithJury();
    @Query("SELECT v FROM Ville v WHERE v.libelleLong= :libelleLong AND v.id != :villeId")
    Optional<Ville> findByLibelleLongAndIdNot(@Param("libelleLong") String libelleLong, @Param("villeId") Long villeId);
    Optional<Ville> findByLibelleCourt(String libelleCourt);
    @Query("SELECT COALESCE(SUM(c.nombreJury), 0) FROM Centre c WHERE c.ville.id = :villeId")
    int getTotalJuryByVilleId(@Param("villeId") Long villeId);
    @Modifying
    @Query("UPDATE Ville v SET v.totalJury = :totalJury WHERE v.id = :villeId")
    void updateTotalJury(@Param("villeId") Long villeId, @Param("totalJury") int totalJury);
    @Query("SELECT COUNT(d) FROM Demande d WHERE d.centre.ville = :ville AND d.etatDemande.libelleLong = 'acceptée' AND d.session.ouvert = true")
    int totalDemandeAccepteByVille(@Param("ville") Ville ville);
    @Query("SELECT COUNT(d) FROM Demande d WHERE d.centre.ville = :ville AND d.etatDemande.libelleLong IN ('acceptée', 'validée') AND d.session.ouvert = true")
    int totalDemandeAccepteOrValideByVille(@Param("ville") Ville ville);
    @Query("SELECT COUNT(d) FROM Demande d WHERE d.ville= :ville AND d.etatDemande.libelleLong='validée' AND d.session.ouvert = true")
    int totalJuryAffecteByVille(@Param("ville") Ville ville);
//    @Query("SELECT v FROM Ville v " +
//            "WHERE v.academie = :academie " +
//            "AND v.id NOT IN (SELECT d.ville.id FROM Demande d WHERE d.user = :user AND d.academie = :academie AND d.session IN (SELECT s FROM Session s WHERE s.annee.encours = true)) " +
//            "AND v.totalJury > (SELECT COUNT(d) FROM Demande d WHERE d.ville = v AND d.etatDemande.libelleLong = 'validée' AND d.session IN (SELECT s FROM Session s WHERE s.annee.encours = true)) ")
//    List<Ville> availableVillesForUserAndAcademy(@Param("user") AppUser user, @Param("academie") Academie academie);

    @Query("SELECT v FROM Ville v " +
            "WHERE v.academie.id = :academieId " +
            "AND v.id NOT IN (SELECT d.ville.id FROM Demande d WHERE d.user = :user AND d.academie.id = :academieId AND d.session.ouvert = true) " +
            "AND v.totalJury > (SELECT COUNT(d) FROM Demande d WHERE d.ville = v AND d.etatDemande.libelleLong = 'validée' AND d.session.ouvert = true) ")
    List<Ville> availableVillesForUserAndAcademy(@Param("user") AppUser user, @Param("academieId") Long academieId);


}
