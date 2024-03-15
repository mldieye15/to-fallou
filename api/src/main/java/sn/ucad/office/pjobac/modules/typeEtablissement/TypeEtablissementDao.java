package sn.ucad.office.pjobac.modules.typeEtablissement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.fonction.Fonction;

import java.util.Optional;


@Repository
public interface TypeEtablissementDao extends JpaRepository<TypeEtablissement, Long> {
    Optional<TypeEtablissement> findByLibelleLong(String libelleLong);
    @Query("SELECT te FROM TypeEtablissement te WHERE te.libelleLong= :libelleLong AND te.id != :typeEtablissementId")
    Optional<TypeEtablissement> findByLibelleLongAndIdNot(@Param("libelleLong") String libelleLong, @Param("typeEtablissementId") Long typeEtablissementId);
}
