package sn.ucad.office.pjobac.modules.centre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CentreDao extends JpaRepository<Centre, Long> {
    Optional<Centre> findByLibelleLong(String libelleLong);
}
