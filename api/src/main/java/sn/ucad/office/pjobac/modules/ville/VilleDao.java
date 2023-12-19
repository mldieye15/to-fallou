package sn.ucad.office.pjobac.modules.ville;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.academie.Academie;

import java.util.List;


@Repository
public interface VilleDao extends JpaRepository<Ville, Long> {
    List<Ville> findByAcademie(Academie academie);
    @Query("SELECT COALESCE(SUM(c.nombreJury), 0) FROM Centre c WHERE c.ville.id = :villeId")
    int getTotalJuryByVilleId(@Param("villeId") Long villeId);
    @Modifying
    @Query("UPDATE Ville v SET v.totalJury = :totalJury WHERE v.id = :villeId")
    void updateTotalJury(@Param("villeId") Long villeId, @Param("totalJury") int totalJury);
    @Query("SELECT COALESCE(SUM(c.nombreJuryAffecte), 0) FROM Centre c WHERE c.ville.id = :villeId")
    int getTotalDemandeAccepteByVilleId(@Param("villeId") Long villeId);
    @Modifying
    @Query("UPDATE Ville v SET v.totalDemandeAccepte = :totalDemandeAccepte WHERE v.id = :villeId")
    void updateDemandeAccepte(@Param("villeId") Long villeId, @Param("totalDemandeAccepte") int totalDemandeAccepte);
    @Modifying
    @Query("UPDATE Ville v SET v.totalJuryAffecte = :totalJuryAffecte WHERE v.id = :villeId")
    void updateTotalJuryAffecte(@Param("villeId") Long villeId, @Param("totalJuryAffecte") int totalJuryAffecte);
    @Query("SELECT COUNT(d) FROM Demande d WHERE d.ville.id = :villeId AND d.etatDemande.id=:etatDemandeId")
    int totalJuryAffecteByVille(@Param("villeId") Long villeId,@Param("etatDemandeId") Long etatDemandeId);
    @Modifying
    @Query("UPDATE Ville v SET v.quotaDemandeAccepte = true WHERE v.id = :villeId")
    void updateVilleQuotaDemandeAccepteTrue(@Param("villeId") Long villeId);
    @Modifying
    @Query("UPDATE Ville v SET v.quotaDemandeAccepte = false WHERE v.id = :villeId")
    void updateVilleQuotaDemandeAccepteFalse(@Param("villeId") Long villeId);
    @Modifying
    @Query("UPDATE Ville v SET v.quota = true WHERE v.id = :villeId")
    void updateVilleQuotaTrue(@Param("villeId") Long villeId);
    @Modifying
    @Query("UPDATE Ville v SET v.quota = false WHERE v.id = :villeId")
    void updateVilleQuotaFalse(@Param("villeId") Long villeId);
}
