package sn.ucad.office.pjobac.modules.etatDemande;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EtatDemandeDao extends JpaRepository<EtatDemande, Long> {
}
