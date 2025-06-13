package sn.ucad.office.pjobac.modules.security.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<AppUser, Long> {
//    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByMatricule(String email);
//    Optional<AppUser> findByEmailAndIdNot(String email, Long id);
    @Query("SELECT u FROM AppUser u WHERE u.email = :email AND u.id != :userId")
    Optional<AppUser> findByEmailAndIdNot(@Param("email") String email, @Param("userId") Long userId);
//    Optional<AppUser> findByMatriculeAndIdNot(String matricule, Long id);
    @Query("SELECT u FROM AppUser u WHERE u.matricule = :matricule AND u.id != :userId")
    Optional<AppUser> findByMatriculeAndIdNot(@Param("matricule") String matricule, @Param("userId") Long userId);
    @Query("SELECT u FROM AppUser u JOIN u.roles r WHERE r.nom = 'ROLE_ADMIN'")
    List<AppUser> adminRole();
    @Query("SELECT u FROM AppUser u JOIN u.roles r WHERE r.nom = 'ROLE_USER'")
    List<AppUser> userRole();
    @Query("SELECT u FROM AppUser u JOIN u.roles r WHERE r.nom = 'ROLE_PLANIFICATEUR'")
    List<AppUser> planificateurRole();
    @Query("SELECT u FROM AppUser u JOIN u.roles r WHERE r.nom = 'ROLE_SUPERVISSEUR'")
    List<AppUser> supervisseurRole();
    @Query("SELECT  COUNT(d) FROM Demande d WHERE d.etatDemande.libelleLong='validée' AND d.user=:user")
    int anciennete(@Param("user") AppUser user);
    @Query("""
          SELECT DISTINCT u
          FROM AppUser u
          WHERE u.id IN (
            SELECT dc.candidat
            FROM DetailsCandidat dc
            WHERE dc.annee.encours = true
          )
          AND u.id NOT IN (
            SELECT d.user.id
            FROM Demande d
            WHERE d.etatDemande.id = 3
              AND d.session.ouvert  = true
          )
        """)
    List<AppUser> findAppUsersWhoAppliedAndHaveNoValidatedDemandInCurrentSession();
}
