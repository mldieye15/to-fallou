package sn.ucad.office.pjobac.modules.ville;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.academie.Academie;

import java.util.List;


@Repository
public interface VilleDao extends JpaRepository<Ville, Long> {
    List<Ville> findByAcademie(Academie academie);
}
