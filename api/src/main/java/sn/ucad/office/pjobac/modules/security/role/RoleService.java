package sn.ucad.office.pjobac.modules.security.role;

import org.springframework.data.domain.Pageable;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.security.role.dto.RoleAudit;
import sn.ucad.office.pjobac.modules.security.role.dto.RoleRequest;
import sn.ucad.office.pjobac.modules.security.role.dto.RoleResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    public List<RoleResponse> all() throws BusinessResourceException;
    public SimplePage<RoleResponse> all(Pageable pageable) throws BusinessResourceException;
    public Optional<RoleResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;
    RoleResponse add(RoleRequest request) throws BusinessResourceException;
    RoleResponse maj(RoleRequest request, String id)  throws NumberFormatException, BusinessResourceException;
    public String del(String id) throws NumberFormatException, BusinessResourceException;
    public Optional<RoleAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
    public Optional<AppRole> roleByNom(String nom) throws BusinessResourceException;
}
