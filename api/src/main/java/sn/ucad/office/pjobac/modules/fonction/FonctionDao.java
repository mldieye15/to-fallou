package sn.ucad.office.pjobac.modules.fonction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FonctionDao extends JpaRepository<Fonction, Long> {
}
