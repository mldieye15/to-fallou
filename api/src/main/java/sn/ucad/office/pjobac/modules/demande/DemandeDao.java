package sn.ucad.office.pjobac.modules.demande;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DemandeDao extends JpaRepository<Demande, Long> {
}
