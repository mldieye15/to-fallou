package sn.ucad.office.pjobac.modules.detailsCandidat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.ville.Ville;

import java.util.List;
import java.util.Optional;


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
    @Query("SELECT dc FROM DetailsCandidat dc WHERE dc.candidat = :user AND dc.annee.encours = true")
    DetailsCandidat detailsForUserAndSession(@Param("user") AppUser user);
    @Query("SELECT MAX(dc.note) " +
            "FROM Demande d " +
            "JOIN DetailsCandidat dc ON dc.candidat = d.user " +
            "WHERE d.etatDemande.libelleLong = 'EN ATTENTE' " +
            "AND d.ville = :ville")
    Integer maxNote(@Param("ville") Ville ville);

    @Query("SELECT dc FROM DetailsCandidat dc INNER JOIN Demande d WHERE d.ville = :ville")
    List<DetailsCandidat> findByVille(@Param("ville") Ville ville);
    @Query("SELECT dc FROM DetailsCandidat dc WHERE dc.annee.encours = true")
    List<DetailsCandidat> findByEncours();
    @Query("SELECT dc FROM DetailsCandidat dc WHERE dc.candidat = :user AND dc.annee.encours = true")
    Optional<DetailsCandidat> findByCandidatId(@Param("user") AppUser user);
}
