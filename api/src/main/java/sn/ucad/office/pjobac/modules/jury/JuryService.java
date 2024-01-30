package sn.ucad.office.pjobac.modules.jury;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;


import sn.ucad.office.pjobac.modules.centre.dto.CentreResponse;
import sn.ucad.office.pjobac.modules.jury.dto.JuryAudit;
import sn.ucad.office.pjobac.modules.jury.dto.JuryRequest;
import sn.ucad.office.pjobac.modules.jury.dto.JuryResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface JuryService {
    public List<JuryResponse> all() throws BusinessResourceException;

    public SimplePage<JuryResponse> all(Pageable pageable) throws BusinessResourceException;
    public List<JuryResponse> juryNonAffecterByCentre(String centreId) throws BusinessResourceException;

    public Optional<JuryResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public JuryResponse add(JuryRequest req) throws BusinessResourceException;

    public JuryResponse maj(JuryRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<JuryAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
    int countJuryByCentre(Long centreId);
    void updateCentreTotalJury(Long centreId);
    int countJuryByVille(Long villeId);
    void updateVilleTotalJury(Long villeId);
    void verifyJuryUnique(String numero)throws  BusinessResourceException;
}
