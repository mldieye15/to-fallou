package sn.ucad.office.pjobac.modules.detailsCandidat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sn.ucad.office.pjobac.modules.security.user.AppUser;


@Repository
public interface DetailsCandidatDao extends JpaRepository<DetailsCandidat, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE DetailsCandidat dc SET dc.note = dc.noteSupervisseur + dc.noteEtablissementProvenance + dc.noteFonction + dc.noteAnciennete + dc.bonus - dc.malus WHERE dc.candidat = :candidat")
    void updateNoteBy(@Param("candidat") AppUser candidat);
    @Transactional
    @Modifying
    @Query("UPDATE DetailsCandidat dc SET dc.note = dc.noteSupervisseur + dc.noteEtablissementProvenance + dc.noteFonction + dc.noteAnciennete + dc.bonus - dc.malus WHERE dc = :detailsCandidat")
    void updateNote(@Param("detailsCandidat") DetailsCandidat detailsCandidat);
    @Query("SELECT dc FROM DetailsCandidat dc WHERE dc.candidat = :user")
    DetailsCandidat detailsForUser(@Param("user") AppUser user);

}
