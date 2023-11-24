package sn.ucad.office.pjobac.modules.typeEtablissement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TypeEtablissementDao extends JpaRepository<TypeEtablissement, Long> {
    Optional<TypeEtablissement> findByLibelleLong(String libelleLong);
}
