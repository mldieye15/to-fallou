package sn.ucad.office.pjobac.modules.academie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.academie.dto.AcademieResponse;
import sn.ucad.office.pjobac.modules.academie.dto.AcademieVille;
import sn.ucad.office.pjobac.modules.academie.dto.AcademieVilleResponse;
import sn.ucad.office.pjobac.modules.demande.Demande;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.ville.Ville;

import java.util.List;
import java.util.Optional;


@Repository
public interface AcademieDao extends JpaRepository<Academie, Long> {
    Optional <Academie> findByLibelleLong(String libelleLong);
    @Query("SELECT a FROM Academie a WHERE a.libelleLong= :libelleLong AND a.id != :academieId")
    Optional<Academie> findByLibelleLongAndIdNot(@Param("libelleLong") String libelleLong, @Param("academieId") Long academieId);
    Optional <Academie> findByLibelleCourt(String libelleCourt);
    @Query(value = "select a.libelle_long as libelleLong, a.libelle_court, v.libelle_long  from  academies a  join villes v  on a.id=v.id",  nativeQuery = true)
    List <AcademieVille> academieWithVille();
    @Query("SELECT a FROM Academie a WHERE a.id NOT IN (SELECT d.academie.id FROM Demande d WHERE d.user = :user AND d <> :demande GROUP BY d.academie.id HAVING COUNT(d.academie.id) >= 2)")
    List<Academie> availableAcademiesForUser(@Param("user") AppUser user, @Param("demande")Demande demande);
    @Query("SELECT DISTINCT a FROM Academie a " +
            "WHERE " +
            "   (SELECT COUNT(c) FROM Centre c WHERE c.ville.academie = a AND c.typeCentre.libelleLong = 'SECONDAIRE') > " +
            "   (SELECT COUNT(d.centre) FROM Demande d WHERE d.centre.typeCentre.libelleLong = 'SECONDAIRE' AND d.session.ouvert = true AND d.centre.ville.academie = a)")
    List<Academie> findAcademieWithCitySecondary();
    @Query("SELECT a FROM Academie a " +
            "WHERE a.id IN (" +
            "  SELECT v.academie.id FROM Ville v " +
            "  WHERE v.id NOT IN (" +
            "    SELECT d.ville.id FROM Demande d " +
            "    WHERE d.user = :user " +
            "    AND d.academie.id = a.id " +
            "    AND d.session.ouvert = true" +
            "  ) " +
            "  AND v.totalJury > (" +
            "    SELECT COUNT(d) FROM Demande d " +
            "    WHERE d.ville = v " +
            "    AND d.etatDemande.libelleLong = 'validée' " +
            "    AND d.session.ouvert = true" +
            "  )" +
            ")")
    List<Academie> availableAcademiesForUser(@Param("user") AppUser user);


}

