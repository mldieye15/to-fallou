package sn.ucad.office.pjobac.modules.centre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CentreDao extends JpaRepository<Centre, Long> {
    Optional<Centre> findByLibelleLong(String libelleLong);
    @Query("SELECT COUNT(j) FROM Jury j WHERE j.centre.id = :centreId")
    int totalJuryByCentre(@Param("centreId") Long centreId);
    @Query("SELECT MAX(CAST(j.numero AS int)) FROM Jury j WHERE j.centre.id = :centreId")
    Integer findLastJuryNumber(@Param("centreId") Long centreId);
}
