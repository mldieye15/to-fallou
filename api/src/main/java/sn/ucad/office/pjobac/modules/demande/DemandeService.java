package sn.ucad.office.pjobac.modules.demande;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import sn.ucad.office.pjobac.modules.demande.dto.*;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface DemandeService {
    public List<DemandeResponse> all() throws BusinessResourceException;
//    List<DemandeDetailsCandidatResponse> allWithAffectable() throws BusinessResourceException;
    public Map<Long, List<DemandeDetailsCandidatResponse>> allGroupedByUser() throws BusinessResourceException;
    List<DemandeResponse> allForUser() throws BusinessResourceException;

    public SimplePage<DemandeResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<DemandeResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public DemandeResponse add(DemandeRequest req) throws BusinessResourceException;
    public List<DemandeResponse> addAll(List<DemandeRequest> req) throws BusinessResourceException;

    public DemandeResponse maj(DemandeRequest req, String demandeId) throws NumberFormatException, NoSuchElementException, BusinessResourceException;
    public DemandeResponse accepterDemande(DemandeAccepter req, String demandeId) throws NumberFormatException, NoSuchElementException, BusinessResourceException;
    public DemandeResponse validerDemande(String demandeId) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<DemandeAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
    int totalJuryAffecteByVille(String villeId)throws NumberFormatException, BusinessResourceException;
    public void demandeObseleteByVille(Long villeId)throws NumberFormatException, BusinessResourceException;
    public void rejeterDemande(Long villeId)throws NumberFormatException, BusinessResourceException;
    boolean hasAcceptedDemande(String userId);
    boolean quotaAccepteByVille(String villeId);
}
