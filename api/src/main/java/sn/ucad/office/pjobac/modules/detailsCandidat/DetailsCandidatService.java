package sn.ucad.office.pjobac.modules.detailsCandidat;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import sn.ucad.office.pjobac.modules.detailsCandidat.dto.DetailsCandidatAudit;
import sn.ucad.office.pjobac.modules.detailsCandidat.dto.DetailsCandidatRequest;
import sn.ucad.office.pjobac.modules.detailsCandidat.dto.DetailsCandidatResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface DetailsCandidatService {
    public List<DetailsCandidatResponse> all() throws BusinessResourceException;

    public SimplePage<DetailsCandidatResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<DetailsCandidatResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public DetailsCandidatResponse add(DetailsCandidatRequest req) throws BusinessResourceException;

    public DetailsCandidatResponse maj(DetailsCandidatRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<DetailsCandidatAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
}
