package sn.ucad.office.pjobac.modules.centre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.ville.Ville;

import java.util.List;
import java.util.Optional;


@Repository
public interface CentreDao extends JpaRepository<Centre, Long> {
    Optional<Centre> findByLibelleLong(String libelleLong);
    @Query("SELECT COUNT(j) FROM Jury j WHERE j.centre.id = :centreId")
    int totalJuryByCentre(@Param("centreId") Long centreId);
    @Query("SELECT MAX(CAST(j.numero AS int)) FROM Jury j WHERE j.centre.id = :centreId")
    Integer findLastJuryNumber(@Param("centreId") Long centreId);
    @Modifying
    @Query("UPDATE Centre c SET c.nombreJury = :totalJury WHERE c.id = :centreId")
    void updateTotalJury(@Param("centreId") Long centreId, @Param("totalJury") int totalJury);
    @Query("SELECT COUNT(d) FROM Demande d WHERE d.centre.id = :centreId")
    int totalJuryAffecteByCentre(@Param("centreId") Long centreId);
    @Modifying
    @Query("UPDATE Centre c SET c.nombreJuryAffecte = :nombreJuryAffecte WHERE c.id = :centreId")
    void updateTotalJuryAffecte(@Param("centreId") Long centreId, @Param("nombreJuryAffecte") int nombreJuryAffecte);
    @Query("SELECT c FROM Centre c " +
            "WHERE c.ville = :ville " +
            "AND NOT EXISTS (SELECT d FROM Demande d WHERE d.centre = c)")
    List<Centre> findCentresSansDemandeParVille(@Param("ville") Ville ville);
    @Query("SELECT c FROM Centre c " +
            "WHERE c.ville = :ville " +
            "AND c.quota = false")
    List<Centre> findCentresQuotaNonAtteintParVille(@Param("ville") Ville ville);
    @Modifying
    @Query("UPDATE Centre c SET c.quota = true WHERE c.id = :centreId")
    void updateCentreQuota(@Param("centreId") Long centreId);
    @Modifying
    @Query("UPDATE Centre c SET c.quota = false WHERE c.id = :centreId")
    void updateCentreQuotaFalse(@Param("centreId") Long centreId);
}

