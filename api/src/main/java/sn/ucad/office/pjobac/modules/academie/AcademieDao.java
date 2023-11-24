package sn.ucad.office.pjobac.modules.academie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.academie.dto.AcademieResponse;
import sn.ucad.office.pjobac.modules.academie.dto.AcademieVille;
import sn.ucad.office.pjobac.modules.academie.dto.AcademieVilleResponse;

import java.util.List;
import java.util.Optional;


@Repository
public interface AcademieDao extends JpaRepository<Academie, Long> {
    Optional <Academie> findByLibelleLong(String libelleLong);
    /*@Query(value = "select a.libelle_long, a.libelle_court, v.libeele_long  from  academies a  join villes v  on a.ville_id=v.id",  nativeQuery = true)
    List <AcademieVilleResponse> academieWithVille();*/

    @Query(value = "select a.libelle_long as libelleLong, a.libelle_court, v.libelle_long  from  academies a  join villes v  on a.id=v.id",  nativeQuery = true)
    List <AcademieVille> academieWithVille();
}
// select a.libelle_long,a.libelle_court,v.libeele_long  from  academies a  join villes v  on a.ville_id=v.id
