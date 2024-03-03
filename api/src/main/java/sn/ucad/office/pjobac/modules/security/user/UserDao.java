package sn.ucad.office.pjobac.modules.security.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<AppUser, Long> {
//    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByMatricule(String email);
    @Query("SELECT u FROM AppUser u JOIN u.roles r WHERE r.nom = 'ROLE_ADMIN'")
    List<AppUser> adminRole();
    @Query("SELECT u FROM AppUser u JOIN u.roles r WHERE r.nom = 'ROLE_USER'")
    List<AppUser> userRole();
    @Query("SELECT u FROM AppUser u JOIN u.roles r WHERE r.nom = 'ROLE_PLANIFICATEUR'")
    List<AppUser> planificateurRole();
    @Query("SELECT u FROM AppUser u JOIN u.roles r WHERE r.nom = 'ROLE_SUPERVISSEUR'")
    List<AppUser> supervisseurRole();
}
