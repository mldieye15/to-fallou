package sn.ucad.office.pjobac.modules.fonction;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.fonction.dto.FonctionAudit;
import sn.ucad.office.pjobac.modules.fonction.dto.FonctionRequest;
import sn.ucad.office.pjobac.modules.fonction.dto.FonctionResponse;
import sn.ucad.office.pjobac.utils.SimplePage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface FonctionService {
    public List<FonctionResponse> all() throws BusinessResourceException;

    public SimplePage<FonctionResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<FonctionResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public FonctionResponse add(FonctionRequest req) throws BusinessResourceException;

    public FonctionResponse maj(FonctionRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<FonctionAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
}
