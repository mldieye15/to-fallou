package sn.ucad.office.pjobac.modules.centre;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import sn.ucad.office.pjobac.modules.centre.dto.CentreAudit;
import sn.ucad.office.pjobac.modules.centre.dto.CentreRequest;
import sn.ucad.office.pjobac.modules.centre.dto.CentreResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface CentreService {
    public List<CentreResponse> all() throws BusinessResourceException;
    public List<CentreResponse> allWithJury() throws BusinessResourceException;
    public List<CentreResponse> allAvecQuota(String villeId) throws BusinessResourceException;
    public List<CentreResponse> allAvecQuotaProposition(String villeId) throws BusinessResourceException;
    public List<CentreResponse> allSecondaryByVille(String villeId) throws BusinessResourceException;
    public List<CentreResponse> centreParVilleSansJury(String villeId) throws BusinessResourceException;
    public List<CentreResponse> centreParVilleSansJuryForProposition(String villeId) throws BusinessResourceException;

    public SimplePage<CentreResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<CentreResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public CentreResponse add(CentreRequest req) throws BusinessResourceException;

    public CentreResponse maj(CentreRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<CentreAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
    void verifyCentreUnique(String libelleLong)throws  BusinessResourceException;
    void verifyUniqueLibelleCourt(String libelleCourt)throws  BusinessResourceException;
    boolean verifyLibelleLongUniqueUp(String libelleLong, Long id)throws  BusinessResourceException;
}
