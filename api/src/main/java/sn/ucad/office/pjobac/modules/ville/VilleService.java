package sn.ucad.office.pjobac.modules.ville;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import sn.ucad.office.pjobac.modules.ville.dto.VilleAudit;
import sn.ucad.office.pjobac.modules.ville.dto.VilleRequest;
import sn.ucad.office.pjobac.modules.ville.dto.VilleResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface VilleService {
    public List<VilleResponse> all() throws BusinessResourceException;

    public SimplePage<VilleResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<VilleResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public VilleResponse add(VilleRequest req) throws BusinessResourceException;

    public VilleResponse maj(VilleRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<VilleAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
}
