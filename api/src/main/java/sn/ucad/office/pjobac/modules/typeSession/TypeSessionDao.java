package sn.ucad.office.pjobac.modules.typeSession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TypeSessionDao extends JpaRepository<TypeSession, Long> {
    Optional<TypeSession> findByLibelleLong(String libelleLong);
}
