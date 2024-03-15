package sn.ucad.office.pjobac.modules.etablissement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.centre.Centre;

import java.util.Optional;


@Repository
public interface EtablissementDao extends JpaRepository<Etablissement, Long> {
    Optional<Etablissement> findByLibelleLong(String libelleLong);
    Optional<Etablissement> findByLibelleCourt(String libelleCourt);
    @Query("SELECT e FROM Etablissement e WHERE e.libelleLong= :libelleLong AND e.id != :etablissementId")
    Optional<Etablissement> findByLibelleLongAndIdNot(@Param("libelleLong") String libelleLong, @Param("etablissementId") Long etablissementId);
}
