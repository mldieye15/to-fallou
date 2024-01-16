package sn.ucad.office.pjobac.modules.jury;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface JuryDao extends JpaRepository<Jury, Long> {
    Optional<Jury> findByNumero(String numero);
}
