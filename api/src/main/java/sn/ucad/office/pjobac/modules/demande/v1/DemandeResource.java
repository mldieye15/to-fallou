package sn.ucad.office.pjobac.modules.demande.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ucad.office.pjobac.config.AppConstants;

import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.demande.DemandeService;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeAccepter;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeRequest;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/api/v1/demandes")
@RequiredArgsConstructor
public class DemandeResource {
    private final DemandeService service;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<DemandeResponse>> allPaginate(
            @SortDefault(sort = "liblleLong") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<DemandeResponse>  response = service.all(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/obselete/{villeId}")
    public ResponseEntity<String> demandePourVille(@PathVariable Long villeId) {
        try {
           service.demandeObseleteByVille(villeId);
            return new ResponseEntity<>("État de demande mis à jour avec succès pour la ville " + villeId, HttpStatus.OK);
        } catch (BusinessResourceException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DemandeResponse>> all(){
        List<DemandeResponse> response = service.all();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<DemandeResponse>> one(@PathVariable(value = "id") String id) {
        Optional<DemandeResponse> response = service.oneById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<DemandeResponse> add(@RequestBody @Valid DemandeRequest request) {
        DemandeResponse response = service.add(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping(value = "/addAll")
    public ResponseEntity<List<DemandeResponse>> addAll(@RequestBody @Valid List<DemandeRequest> requests) {
        List<DemandeResponse> responses = service.addAll(requests);
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }
    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<DemandeResponse> maj(@PathVariable(value="id") String id,
                                               @RequestBody @Valid DemandeRequest request) {
        DemandeResponse response = service.maj(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping(value = "/accepter/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<DemandeResponse> accepterDemande(@PathVariable(value="id") String id,
                                               @RequestBody @Valid DemandeAccepter accepter) {
        DemandeResponse response = service.accepterDemande(accepter, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/valider/{userId}")
    public ResponseEntity<DemandeResponse> validerDemande(@PathVariable("userId") String userId) {
        try {
            DemandeResponse response = service.validerDemande(userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessResourceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

