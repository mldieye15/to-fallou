package sn.ucad.office.pjobac.modules.codification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodificationDao extends JpaRepository<Codification, Long> {
    Optional<Codification> findByEmail(String email);
}
