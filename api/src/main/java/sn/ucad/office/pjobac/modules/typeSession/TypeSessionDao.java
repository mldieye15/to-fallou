package sn.ucad.office.pjobac.modules.typeSession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.typeEtablissement.TypeEtablissement;

import java.util.Optional;


@Repository
public interface TypeSessionDao extends JpaRepository<TypeSession, Long> {
    Optional<TypeSession> findByLibelleLong(String libelleLong);
    @Query("SELECT ts FROM TypeSession ts WHERE ts.libelleLong= :libelleLong AND ts.id != :typeSessionId")
    Optional<TypeSession> findByLibelleLongAndIdNot(@Param("libelleLong") String libelleLong, @Param("typeSessionId") Long typeSessionId);
}
