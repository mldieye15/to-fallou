package sn.ucad.office.pjobac.modules.jury;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.centre.Centre;
import sn.ucad.office.pjobac.modules.fonction.Fonction;

import java.util.List;
import java.util.Optional;


@Repository
public interface JuryDao extends JpaRepository<Jury, Long> {
    Optional<Jury> findByNom(String nom);
    @Query("SELECT j FROM Jury j WHERE j.numero= :numero AND j.session.ouvert = true ")
    Optional<Jury> findByNumero(@Param("numero") String numero);
    @Query("SELECT j FROM Jury j WHERE j.numero= :numero AND j.id != :juryId AND j.session.ouvert = true ")
    Optional<Jury> findByNumeroAndIdNot(@Param("numero") String numero, @Param("juryId") Long juryId);
    @Query("SELECT j FROM Jury j WHERE j.nom= :nom AND j.id != :juryId")
    Optional<Jury> findByNomAndIdNot(@Param("nom") String nom, @Param("juryId") Long juryId);
    @Query("SELECT j FROM Jury j WHERE j.centre = :centre AND j.session.ouvert = true AND NOT EXISTS (SELECT d FROM Demande d WHERE d.jury.id = j.id)")
    List<Jury> juryNonAffecterByCentre(@Param("centre") Centre centre);
    @Query("SELECT j FROM Jury j WHERE j.session.annee.encours = true ")
    List<Jury> juryByEncours();


}
