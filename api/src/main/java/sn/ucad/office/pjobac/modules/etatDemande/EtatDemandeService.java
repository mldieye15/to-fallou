package sn.ucad.office.pjobac.modules.etatDemande;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import sn.ucad.office.pjobac.modules.etatDemande.dto.EtatDemandeAudit;
import sn.ucad.office.pjobac.modules.etatDemande.dto.EtatDemandeRequest;
import sn.ucad.office.pjobac.modules.etatDemande.dto.EtatDemandeResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface EtatDemandeService {
    public List<EtatDemandeResponse> all() throws BusinessResourceException;

    public SimplePage<EtatDemandeResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<EtatDemandeResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public EtatDemandeResponse add(EtatDemandeRequest req) throws BusinessResourceException;

    public EtatDemandeResponse maj(EtatDemandeRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<EtatDemandeAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
    Optional<Long> findIdByLibelleLong(String libelleLong) throws BusinessResourceException;
    Optional<EtatDemande> findByLibelleLong(String libelleLong) throws BusinessResourceException;
}
