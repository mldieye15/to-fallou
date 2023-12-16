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
}
