package sn.ucad.office.pjobac.modules.etablissement.v1;

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
import sn.ucad.office.pjobac.modules.etablissement.EtablissementService;
import sn.ucad.office.pjobac.modules.etablissement.dto.EtablissementRequest;
import sn.ucad.office.pjobac.modules.etablissement.dto.EtablissementResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/api/v1/etablissements")
@RequiredArgsConstructor
public class EtablissementResource {
    private final EtablissementService service;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<EtablissementResponse>> allPaginate(
            @SortDefault(sort = "liblleLong") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<EtablissementResponse>  response = service.all(pageable);
        return new ResponseEntity< SimplePage<EtablissementResponse> >(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<EtablissementResponse>> all(){
        List<EtablissementResponse> response = service.all();
        return new ResponseEntity< List<EtablissementResponse> >(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<EtablissementResponse>> one(@PathVariable(value = "id") String id) {
        Optional<EtablissementResponse> response = service.oneById(id);
        return new ResponseEntity<Optional<EtablissementResponse>>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<EtablissementResponse> add(@RequestBody @Valid EtablissementRequest request) {
        EtablissementResponse response = service.add(request);
        return new ResponseEntity<EtablissementResponse>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<EtablissementResponse> maj(@PathVariable(value="id") String id,
                                                     @RequestBody @Valid EtablissementRequest request) {
        EtablissementResponse response = service.maj(request, id);
        return new ResponseEntity<EtablissementResponse>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

