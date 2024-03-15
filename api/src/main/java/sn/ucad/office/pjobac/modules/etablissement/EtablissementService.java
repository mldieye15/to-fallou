package sn.ucad.office.pjobac.modules.etablissement;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import sn.ucad.office.pjobac.modules.etablissement.dto.EtablissementAudit;
import sn.ucad.office.pjobac.modules.etablissement.dto.EtablissementRequest;
import sn.ucad.office.pjobac.modules.etablissement.dto.EtablissementResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface EtablissementService {
    public List<EtablissementResponse> all() throws BusinessResourceException;

    public SimplePage<EtablissementResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<EtablissementResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public EtablissementResponse add(EtablissementRequest req) throws BusinessResourceException;

    public EtablissementResponse maj(EtablissementRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<EtablissementAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
    void verifyEtablissementUnique(String libelleLong)throws  BusinessResourceException;
    void verifyUniqueLibelleCourt(String libelleCourt)throws  BusinessResourceException;
    boolean verifyLibelleLongUniqueUp(String libelleLong, Long id)throws  BusinessResourceException;
}
