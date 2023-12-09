package sn.ucad.office.pjobac.modules.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SessionDao extends JpaRepository<Session, Long> {

    @Query("SELECT s FROM Session s WHERE s.annee.encours = true AND s.sessionOuvert = true")
    List<Session> findEnCoursSession();

    @Query("SELECT s FROM Session s WHERE s.sessionOuvert = true")
    List<Session> findSessionsOuvertes();

    @Query("SELECT s FROM Session s WHERE s.annee.encours = true AND s.sessionOuvert = true AND s.candidatureOuvert = true")
    List<Session> findCandidaturesOuvertes();
}
