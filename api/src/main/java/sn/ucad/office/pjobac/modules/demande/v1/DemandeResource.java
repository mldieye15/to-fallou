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

import sn.ucad.office.pjobac.modules.demande.DemandeService;
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
        return new ResponseEntity< SimplePage<DemandeResponse> >(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DemandeResponse>> all(){
        List<DemandeResponse> response = service.all();
        return new ResponseEntity< List<DemandeResponse> >(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<DemandeResponse>> one(@PathVariable(value = "id") String id) {
        Optional<DemandeResponse> response = service.oneById(id);
        return new ResponseEntity<Optional<DemandeResponse>>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<DemandeResponse> add(@RequestBody @Valid DemandeRequest request) {
        DemandeResponse response = service.add(request);
        return new ResponseEntity<DemandeResponse>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<DemandeResponse> maj(@PathVariable(value="id") String id,
                                               @RequestBody @Valid DemandeRequest request) {
        DemandeResponse response = service.maj(request, id);
        return new ResponseEntity<DemandeResponse>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

