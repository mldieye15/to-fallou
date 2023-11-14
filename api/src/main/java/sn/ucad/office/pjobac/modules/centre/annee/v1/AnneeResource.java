package sn.ucad.office.pjobac.modules.annee.v1;

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
import sn.ucad.office.pjobac.modules.annee.dto.AnneeRequest;
import sn.ucad.office.pjobac.modules.annee.dto.AnneeResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/api/v1/annees")
@RequiredArgsConstructor
public class AnneeResource {
    private final AnneeService service;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<AnneeResponse>> allPaginate(
            @SortDefault(sort = "liblleLong") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<AnneeResponse>  response = service.all(pageable);
        return new ResponseEntity< SimplePage<AnneeResponse> >(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AnneeResponse>> all(){
        List<AnneeResponse> response = service.all();
        return new ResponseEntity< List<AnneeResponse> >(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<AnneeResponse>> one(@PathVariable(value = "id") String id) {
        Optional<AnneeResponse> response = service.oneById(id);
        return new ResponseEntity<Optional<AnneeResponse>>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<AnneeResponse> add(@RequestBody @Valid AnneeRequest request) {
        AnneeResponse response = service.add(request);
        return new ResponseEntity<AnneeResponse>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<AnneeResponse> maj(@PathVariable(value="id") String id,
                                             @RequestBody @Valid AnneeRequest request) {
        AnneeResponse response = service.maj(request, id);
        return new ResponseEntity<AnneeResponse>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

