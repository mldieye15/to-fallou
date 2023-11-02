package sn.ucad.office.pjobac.modules.jury;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JuryDao extends JpaRepository<Jury, Long> {
}
