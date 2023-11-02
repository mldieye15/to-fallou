package sn.ucad.office.pjobac.modules.typeEtablissement;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import sn.ucad.office.pjobac.modules.typeEtablissement.dto.TypeEtablissementAudit;
import sn.ucad.office.pjobac.modules.typeEtablissement.dto.TypeEtablissementRequest;
import sn.ucad.office.pjobac.modules.typeEtablissement.dto.TypeEtablissementResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface TypeEtablissementService {
    public List<TypeEtablissementResponse> all() throws BusinessResourceException;

    public SimplePage<TypeEtablissementResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<TypeEtablissementResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public TypeEtablissementResponse add(TypeEtablissementRequest req) throws BusinessResourceException;

    public TypeEtablissementResponse maj(TypeEtablissementRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<TypeEtablissementAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
}
