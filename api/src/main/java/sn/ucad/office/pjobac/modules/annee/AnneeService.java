package sn.ucad.office.pjobac.modules.annee;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.annee.dto.AnneeRequest;
import sn.ucad.office.pjobac.modules.annee.dto.AnneeResponse;
import sn.ucad.office.pjobac.modules.annee.dto.AnneeAudit;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface AnneeService {
    public List<AnneeResponse> all() throws BusinessResourceException;

    public List<AnneeResponse> anneeEncours() throws BusinessResourceException;
    public SimplePage<AnneeResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<AnneeResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public AnneeResponse add(AnneeRequest req) throws BusinessResourceException;

    public AnneeResponse maj(AnneeRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<AnneeAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
    void verifyAnneeUnique(String libelleLong)throws  BusinessResourceException;
    void changerEtatAnnee(Long anneeId);
}
