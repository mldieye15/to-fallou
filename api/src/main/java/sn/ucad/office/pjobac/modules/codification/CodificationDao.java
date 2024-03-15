package sn.ucad.office.pjobac.modules.codification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.security.user.AppUser;

import java.util.Optional;

@Repository
public interface CodificationDao extends JpaRepository<Codification, Long> {
    Optional<Codification> findByEmail(String email);
    @Query("SELECT c FROM Codification c WHERE c.email = :email AND c.id != :codificationId")
    Optional<Codification> findByEmailAndIdNot(@Param("email") String email, @Param("codificationId") Long codificationId);
    @Query("SELECT c FROM Codification c WHERE c.code = :code AND c.id != :codificationId")
    Optional<Codification> findByCodeAndIdNot(@Param("code") String code, @Param("codificationId") Long codificationId);
    Optional<Codification> findByCodeAndEmail(String code,String email);
    Optional<Codification> findByCode(String code);

}
