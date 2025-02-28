package sn.ucad.office.pjobac.modules.canditatAuth.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.modules.canditatAuth.CandidatAuthoriserService;
import sn.ucad.office.pjobac.modules.canditatAuth.dto.CandidatAuthoriserRequest;
import sn.ucad.office.pjobac.modules.canditatAuth.dto.CandidatAuthoriserResponse;
import sn.ucad.office.pjobac.modules.security.user.dto.UserResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/api/v1/candidatAuthorisers")
@RequiredArgsConstructor
public class CandidatAuthoriserResource {
    private final CandidatAuthoriserService service;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<CandidatAuthoriserResponse>> allPaginate(
            @SortDefault(sort = "libelleLong") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<CandidatAuthoriserResponse>  response = service.all(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CandidatAuthoriserResponse>> all(){
        List<CandidatAuthoriserResponse> response = service.all();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/allNotAutoriser")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserResponse>> allNotAutoriser(){
        List<UserResponse> response = service.allNotAuthoriser();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<CandidatAuthoriserResponse>> one(@PathVariable(value = "id") String id) {
        Optional<CandidatAuthoriserResponse> response = service.oneById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<CandidatAuthoriserResponse> add(@RequestBody @Valid CandidatAuthoriserRequest request) {
        CandidatAuthoriserResponse response = service.add(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<CandidatAuthoriserResponse> maj(@PathVariable(value="id") String id,
                                                @RequestBody @Valid CandidatAuthoriserRequest request) {
        CandidatAuthoriserResponse response = service.maj(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importerCandidat(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = service.importerCandidat(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erreur", e.getMessage()));
        }
    }
    @PutMapping("/{id}/autorisation")
    public ResponseEntity<Void> autorisation(@PathVariable Long id) {
        service.autorisation(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/hasEnabledNonAutoriserUsers")
    public ResponseEntity<Boolean> hasEnabledNonAutoriserUsers() {
        boolean result = service.hasEnabledNonAutoriserUsers();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PutMapping("/disableNonAutoriser")
    public ResponseEntity<Integer> disableNonAutoriserUsers() {
        int updatedCount = service.disableAllNonAutoriserUsers();
        return ResponseEntity.ok(updatedCount);
    }

}

