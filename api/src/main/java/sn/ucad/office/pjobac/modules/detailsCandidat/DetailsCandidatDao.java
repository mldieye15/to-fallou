package sn.ucad.office.pjobac.modules.detailsCandidat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DetailsCandidatDao extends JpaRepository<DetailsCandidat, Long> {
}
