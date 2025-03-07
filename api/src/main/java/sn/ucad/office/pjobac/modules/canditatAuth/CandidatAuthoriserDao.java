package sn.ucad.office.pjobac.modules.canditatAuth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.ville.Ville;

import java.util.List;


public interface  CandidatAuthoriserDao extends JpaRepository<CandidatAuthoriser, Long> {
    @Query("SELECT c.code FROM CandidatAuthoriser c")
    List<String> findAllCodes();

    // Récupérer tous les matricules existants
    @Query("SELECT c.matricule FROM CandidatAuthoriser c")
    List<String> findAllMatricules();

    // Vérifier si un code existe déjà
    boolean existsByCode(String code);

    // Vérifier si un matricule existe déjà
    boolean existsByMatricule(String matricule);
    @Query("SELECT au FROM AppUser au LEFT JOIN CandidatAuthoriser ca ON au.code = ca.code WHERE au.code IS NOT NULL AND ca.code IS NULL")
    List<AppUser> nonAutoriser();

    @Query("SELECT COUNT(au) > 0 FROM AppUser au WHERE au.isEnabled = true AND au.code IS NOT NULL AND NOT EXISTS (SELECT ca FROM CandidatAuthoriser ca WHERE ca.code = au.code)")
    boolean existsEnabledNonAutoriser();

    @Transactional
    @Modifying
    @Query("UPDATE AppUser au SET au.isEnabled = false WHERE au.code IS NOT NULL AND NOT EXISTS (SELECT ca FROM CandidatAuthoriser ca WHERE ca.code = au.code)")
    int disableNonAutoriserUsers();

}
