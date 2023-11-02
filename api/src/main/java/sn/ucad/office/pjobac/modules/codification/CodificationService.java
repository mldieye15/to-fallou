package sn.ucad.office.pjobac.modules.codification;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.codification.dto.CodificationAudit;
import sn.ucad.office.pjobac.modules.codification.dto.CodificationRequest;
import sn.ucad.office.pjobac.modules.codification.dto.CodificationResponse;
import sn.ucad.office.pjobac.modules.fonction.dto.FonctionAudit;
import sn.ucad.office.pjobac.modules.fonction.dto.FonctionRequest;
import sn.ucad.office.pjobac.modules.fonction.dto.FonctionResponse;
import sn.ucad.office.pjobac.utils.SimplePage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface CodificationService {
    public List<CodificationResponse> all() throws BusinessResourceException;

    public SimplePage<CodificationResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<CodificationResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public CodificationResponse add(CodificationRequest req) throws BusinessResourceException;

    public CodificationResponse maj(CodificationRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<CodificationAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
}
