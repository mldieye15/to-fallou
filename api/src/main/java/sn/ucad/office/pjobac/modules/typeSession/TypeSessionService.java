package sn.ucad.office.pjobac.modules.typeSession;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.typeSession.dto.TypeSessionAudit;
import sn.ucad.office.pjobac.modules.typeSession.dto.TypeSessionRequest;
import sn.ucad.office.pjobac.modules.typeSession.dto.TypeSessionResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface TypeSessionService {
    public List<TypeSessionResponse> all() throws BusinessResourceException;

    public SimplePage<TypeSessionResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<TypeSessionResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public TypeSessionResponse add(TypeSessionRequest req) throws BusinessResourceException;

    public TypeSessionResponse maj(TypeSessionRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<TypeSessionAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
}
