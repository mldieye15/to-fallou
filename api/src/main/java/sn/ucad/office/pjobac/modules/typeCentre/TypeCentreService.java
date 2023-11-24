package sn.ucad.office.pjobac.modules.typeCentre;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import sn.ucad.office.pjobac.modules.typeCentre.dto.TypeCentreAudit;
import sn.ucad.office.pjobac.modules.typeCentre.dto.TypeCentreRequest;
import sn.ucad.office.pjobac.modules.typeCentre.dto.TypeCentreResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface TypeCentreService {
    public List<TypeCentreResponse> all() throws BusinessResourceException;

    public SimplePage<TypeCentreResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<TypeCentreResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public TypeCentreResponse add(TypeCentreRequest req) throws BusinessResourceException;

    public TypeCentreResponse maj(TypeCentreRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<TypeCentreAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
    void verifyLibelleUnique(String libelleLong) throws  BusinessResourceException;
}
