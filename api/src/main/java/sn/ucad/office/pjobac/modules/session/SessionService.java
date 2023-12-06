package sn.ucad.office.pjobac.modules.session;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import sn.ucad.office.pjobac.modules.session.dto.SessionAudit;
import sn.ucad.office.pjobac.modules.session.dto.SessionRequest;
import sn.ucad.office.pjobac.modules.session.dto.SessionResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface SessionService {
    public List<SessionResponse> all() throws BusinessResourceException;

    public List<SessionResponse> allActiveSessions() throws BusinessResourceException;
    public SimplePage<SessionResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<SessionResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public SessionResponse add(SessionRequest req) throws BusinessResourceException;

    public SessionResponse maj(SessionRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<SessionAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
    void changerEtatSession(Long sessionId);

    void changerEtatCandidature(Long sessionId);
}
