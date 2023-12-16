package sn.ucad.office.pjobac.modules.etatDemande;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EtatDemandeDao extends JpaRepository<EtatDemande, Long> {
    Optional<EtatDemande> findByLibelleLong(String libelleLong);

}
