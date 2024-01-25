package sn.ucad.office.pjobac.modules.ville;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.academie.Academie;
import sn.ucad.office.pjobac.modules.security.user.AppUser;

import java.util.List;


@Repository
public interface VilleDao extends JpaRepository<Ville, Long> {
    List<Ville> findByAcademie(Academie academie);
    @Query("SELECT COALESCE(SUM(c.nombreJury), 0) FROM Centre c WHERE c.ville.id = :villeId")
    int getTotalJuryByVilleId(@Param("villeId") Long villeId);
    @Modifying
    @Query("UPDATE Ville v SET v.totalJury = :totalJury WHERE v.id = :villeId")
    void updateTotalJury(@Param("villeId") Long villeId, @Param("totalJury") int totalJury);
    @Query("SELECT COUNT(d) FROM Demande d WHERE d.centre.ville = :ville AND d.etatDemande.libelleLong = 'ACCEPTE' AND d.session IN (SELECT s FROM Session s WHERE s.annee.encours = true)")
    int totalDemandeAccepteByVille(@Param("ville") Ville ville);
    @Query("SELECT COUNT(d) FROM Demande d WHERE d.ville= :ville AND d.etatDemande.libelleLong='VALIDE' AND d.session IN (SELECT s FROM Session s WHERE s.annee.encours = true)")
    int totalJuryAffecteByVille(@Param("ville") Ville ville);
    @Query("SELECT v FROM Ville v " +
            "WHERE v.academie = :academie " +
            "AND v.id NOT IN (SELECT d.ville.id FROM Demande d WHERE d.user = :user AND d.academie = :academie) " +
            "AND v.totalJury > (SELECT COUNT(d) FROM Demande d WHERE d.ville = v AND d.etatDemande.libelleLong = 'VALIDE' AND d.session IN (SELECT s FROM Session s WHERE s.annee.encours = true)) ")
    List<Ville> availableVillesForUserAndAcademy(@Param("user") AppUser user, @Param("academie") Academie academie);

}
