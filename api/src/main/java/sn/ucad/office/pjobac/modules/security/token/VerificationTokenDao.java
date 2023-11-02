package sn.ucad.office.pjobac.modules.security.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenDao extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
