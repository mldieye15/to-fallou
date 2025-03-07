package sn.ucad.office.pjobac.modules.session.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.modules.session.SessionService;
import sn.ucad.office.pjobac.modules.session.dto.SessionRequest;
import sn.ucad.office.pjobac.modules.session.dto.SessionResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/v1/sessions")
@RequiredArgsConstructor
public class SessionResource {
    private final SessionService service;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<SessionResponse>> allPaginate(
            @SortDefault(sort = "liblleLong") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<SessionResponse>  response = service.all(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SessionResponse>> all(){
        List<SessionResponse> response = service.all();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/sessionsArchive")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SessionResponse>> sessionsArchive(){
        List<SessionResponse> response = service.sessionsArchive();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/sessionsOuvertes")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SessionResponse>> findSessionsOuvertes(){
        List<SessionResponse> response = service.findSessionsOuvertes();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/candidaturesOuvertes")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SessionResponse>> findCandidaturesOuvertes(){
        List<SessionResponse> response = service.findCandidaturesOuvertes();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/enCoursSession")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SessionResponse>> findEnCoursSession(){
        List<SessionResponse> response = service.findEnCoursSession();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<SessionResponse>> one(@PathVariable(value = "id") String id) {
        Optional<SessionResponse> response = service.oneById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<SessionResponse> add(@RequestBody @Valid SessionRequest request) {
        SessionResponse response = service.add(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<SessionResponse> maj(@PathVariable(value="id") String id,
                                               @RequestBody @Valid SessionRequest request) {
        SessionResponse response = service.maj(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("{sessionId}/etatSession")
    public ResponseEntity<Void> changerEtatSession(@PathVariable Long sessionId) {
            service.changerEtatSession(sessionId);
            return ResponseEntity.ok().build();
    }
        @PutMapping("/{sessionId}/etatCandidature")
        public ResponseEntity<Void> changerEtatCandidature(@PathVariable Long sessionId) {
            service.changerEtatCandidature(sessionId);
            return ResponseEntity.ok().build();
        }
    @PutMapping("/{sessionId}/etatModification")
    public ResponseEntity<Void> etatModification(@PathVariable Long sessionId) throws InterruptedException {
        service.changerEtatModification(sessionId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{sessionId}/etatPhaseTwo")
    public ResponseEntity<Void> etatPhaseTwo(@PathVariable Long sessionId) throws InterruptedException {
        service.changerEtatPhaseTwo(sessionId);
        return ResponseEntity.ok().build();
    }

}

