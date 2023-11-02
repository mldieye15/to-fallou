package sn.ucad.office.pjobac.modules.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SessionDao extends JpaRepository<Session, Long> {
}
