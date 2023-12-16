package sn.ucad.office.pjobac.modules.demande;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemande;

import java.util.List;
import java.util.Optional;


@Repository
public interface DemandeDao extends JpaRepository<Demande, Long> {
    @Query("SELECT d FROM Demande d WHERE d.user.id = :userId")
    List<Demande> findAllByUserId(@Param("userId") Long userId);
}
