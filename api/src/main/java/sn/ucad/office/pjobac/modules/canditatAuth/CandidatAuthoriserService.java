package sn.ucad.office.pjobac.modules.canditatAuth;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.canditatAuth.dto.CandidatAuthoriserAudit;
import sn.ucad.office.pjobac.modules.canditatAuth.dto.CandidatAuthoriserRequest;
import sn.ucad.office.pjobac.modules.canditatAuth.dto.CandidatAuthoriserResponse;
import sn.ucad.office.pjobac.modules.security.user.dto.UserResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface CandidatAuthoriserService {
    public List<CandidatAuthoriserResponse> all() throws BusinessResourceException;
    public List<UserResponse> allNotAuthoriser() throws BusinessResourceException;
    public SimplePage<CandidatAuthoriserResponse> all(Pageable pageable) throws BusinessResourceException;

    public Optional<CandidatAuthoriserResponse> oneById(String id) throws NumberFormatException, BusinessResourceException;

    public CandidatAuthoriserResponse add(CandidatAuthoriserRequest req) throws BusinessResourceException;

    public CandidatAuthoriserResponse maj(CandidatAuthoriserRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException;

    public String del(String id) throws NumberFormatException, BusinessResourceException;

    public Optional<CandidatAuthoriserAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException;
    public Map<String, Object> importerCandidat(MultipartFile file) throws Exception;
    public void autorisation(Long id);
    public boolean hasEnabledNonAutoriserUsers();
    public int disableAllNonAutoriserUsers();


}
