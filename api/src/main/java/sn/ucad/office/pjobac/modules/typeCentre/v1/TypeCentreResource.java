package sn.ucad.office.pjobac.modules.typeCentre.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.modules.typeCentre.TypeCentreService;
import sn.ucad.office.pjobac.modules.typeCentre.dto.TypeCentreRequest;
import sn.ucad.office.pjobac.modules.typeCentre.dto.TypeCentreResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/api/v1/typeCentres")
@RequiredArgsConstructor
public class TypeCentreResource {
    private final TypeCentreService service;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<TypeCentreResponse>> allPaginate(
            @SortDefault(sort = "liblleLong") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<TypeCentreResponse>  response = service.all(pageable);
        return new ResponseEntity< SimplePage<TypeCentreResponse> >(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TypeCentreResponse>> all(){
        List<TypeCentreResponse> response = service.all();
        return new ResponseEntity< List<TypeCentreResponse> >(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<TypeCentreResponse>> one(@PathVariable(value = "id") String id) {
        Optional<TypeCentreResponse> response = service.oneById(id);
        return new ResponseEntity<Optional<TypeCentreResponse>>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<TypeCentreResponse> add(@RequestBody @Valid TypeCentreRequest request) {
        TypeCentreResponse response = service.add(request);
        return new ResponseEntity<TypeCentreResponse>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<TypeCentreResponse> maj(@PathVariable(value="id") String id,
                                                  @RequestBody @Valid TypeCentreRequest request) {
        TypeCentreResponse response = service.maj(request, id);
        return new ResponseEntity<TypeCentreResponse>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

