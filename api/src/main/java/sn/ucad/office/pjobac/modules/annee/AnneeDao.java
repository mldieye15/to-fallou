package sn.ucad.office.pjobac.modules.annee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.centre.annee.Annee;

import java.util.Optional;


@Repository
public interface AnneeDao extends JpaRepository<Annee, Long> {
    Optional<Annee> findByLibelleLong(String libelleLong);
}
