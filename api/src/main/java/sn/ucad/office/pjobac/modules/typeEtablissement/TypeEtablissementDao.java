package sn.ucad.office.pjobac.modules.typeEtablissement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TypeEtablissementDao extends JpaRepository<TypeEtablissement, Long> {
}
