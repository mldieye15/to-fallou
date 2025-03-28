package sn.ucad.office.pjobac.modules.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.ville.Ville;

import java.util.List;
import java.util.Optional;


@Repository
public interface SessionDao extends JpaRepository<Session, Long> {

    @Query("SELECT s FROM Session s WHERE s.annee.encours = true AND s.ouvert = true")
    List<Session> findEnCoursSession();

    @Query("SELECT s FROM Session s WHERE s.annee.encours = true AND s.ouvert = true")
    Optional<Session> enCoursSession();

    @Query("SELECT s FROM Session s WHERE s.ouvert = true")
    List<Session> findSessionsOuvertes();

    @Query("SELECT s FROM Session s WHERE s.annee.encours = true AND s.ouvert = true AND s.candidature = true")
    List<Session> findCandidaturesOuvertes();
    @Query("SELECT s FROM Session s  WHERE s.annee.encours = false")
    List<Session> sessionsArchive();
    @Query("SELECT s FROM Session s WHERE s.id != :sessionId")
    List<Session> findAllByIdNot(@Param("sessionId") Long sessionId);
}
