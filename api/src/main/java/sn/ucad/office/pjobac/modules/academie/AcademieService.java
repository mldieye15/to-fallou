package sn.ucad.office.pjobac.modules.academie;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import sn.ucad.office.pjobac.modules.academie.dto.AcademieAudit;
import sn.ucad.office.pjobac.modules.academie.dto.AcademieRequest;
import sn.ucad.office.pjobac.modules.academie.dto.AcademieResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface AcademieService {
    public List<AcademieResponse> all() throws BusinessResourceException;
    public List<AcademieResponse> availableAcademiesForUser(String demandeId) throws BusinessResourceException;

    public SimplePage<AcademieResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<AcademieResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public AcademieResponse add(AcademieRequest req) throws BusinessResourceException;

    public AcademieResponse maj(AcademieRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<AcademieAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
    void verifyAcademieUnique(String libelleLong)throws  BusinessResourceException;
    void verifyUniqueLibelleCourt(String libelleCourt)throws  BusinessResourceException;
    boolean verifyLibelleLongUniqueUp(String libelleLong, Long academieId)throws  BusinessResourceException;
}
