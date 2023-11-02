package sn.ucad.office.pjobac.modules.ville.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.modules.annee.AnneeService;
import sn.ucad.office.pjobac.modules.ville.VilleService;
import sn.ucad.office.pjobac.modules.ville.dto.VilleRequest;
import sn.ucad.office.pjobac.modules.ville.dto.VilleResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/api/v1/villes")
@RequiredArgsConstructor
public class VilleResource {
    private final VilleService service;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<VilleResponse>> allPaginate(
            @SortDefault(sort = "libelleLong") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<VilleResponse>  response = service.all(pageable);
        return new ResponseEntity< SimplePage<VilleResponse> >(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<VilleResponse>> all(){
        List<VilleResponse> response = service.all();
        return new ResponseEntity< List<VilleResponse> >(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<VilleResponse>> one(@PathVariable(value = "id") String id) {
        Optional<VilleResponse> response = service.oneById(id);
        return new ResponseEntity<Optional<VilleResponse>>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<VilleResponse> add(@RequestBody @Valid VilleRequest request) {
        VilleResponse response = service.add(request);
        return new ResponseEntity<VilleResponse>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<VilleResponse> maj(@PathVariable(value="id") String id,
                                             @RequestBody @Valid VilleRequest request) {
        VilleResponse response = service.maj(request, id);
        return new ResponseEntity<VilleResponse>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

