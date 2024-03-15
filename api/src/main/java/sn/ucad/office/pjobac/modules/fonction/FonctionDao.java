package sn.ucad.office.pjobac.modules.fonction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.centre.Centre;

import java.util.Optional;


@Repository
public interface FonctionDao extends JpaRepository<Fonction, Long> {
    Optional<Fonction> findByLibelleLong(String libelleLong);
    @Query("SELECT f FROM Fonction f WHERE f.libelleLong= :libelleLong AND f.id != :fonctionId")
    Optional<Fonction> findByLibelleLongAndIdNot(@Param("libelleLong") String libelleLong, @Param("fonctionId") Long fonctionId);
}
