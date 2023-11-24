package sn.ucad.office.pjobac.modules.typeCentre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TypeCentreDao extends JpaRepository<TypeCentre, Long> {
    Optional<TypeCentre> findByLibelleLong(String libelleLong);
}
