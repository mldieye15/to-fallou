package sn.ucad.office.pjobac.modules.typeCentre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.fonction.Fonction;

import java.util.Optional;


@Repository
public interface TypeCentreDao extends JpaRepository<TypeCentre, Long> {
    Optional<TypeCentre> findByLibelleLong(String libelleLong);
    @Query("SELECT tc FROM TypeCentre tc WHERE tc.libelleLong= :libelleLong AND tc.id != :typeCentreId")
    Optional<TypeCentre> findByLibelleLongAndIdNot(@Param("libelleLong") String libelleLong, @Param("typeCentreId") Long typeCentreId);
}
