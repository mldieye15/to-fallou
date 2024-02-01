package sn.ucad.office.pjobac.modules.jury;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.centre.Centre;

import java.util.List;
import java.util.Optional;


@Repository
public interface JuryDao extends JpaRepository<Jury, Long> {
    Optional<Jury> findByNom(String nom);
    @Query("SELECT j FROM Jury j " +
            "WHERE j.centre = :centre AND j.session.annee.encours = true " +
            "AND NOT EXISTS (SELECT d FROM Demande d WHERE d.jury.id = j.id)")
    List<Jury> juryNonAffecterByCentre(@Param("centre") Centre centre);
    @Query("SELECT j FROM Jury j WHERE j.session.annee.encours = true ")
    List<Jury> juryByEncours();


}
