package sn.ucad.office.pjobac.modules.detailsCandidat.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.modules.detailsCandidat.DetailsCandidatService;
import sn.ucad.office.pjobac.modules.detailsCandidat.dto.DetailsCandidatRequest;
import sn.ucad.office.pjobac.modules.detailsCandidat.dto.DetailsCandidatResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/api/v1/detailsCandidats")
@RequiredArgsConstructor
public class DetailsCandidatResource {
    private final DetailsCandidatService service;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<DetailsCandidatResponse>> allPaginate(
            @SortDefault(sort = "appreciation") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<DetailsCandidatResponse>  response = service.all(pageable);
        return new ResponseEntity< SimplePage<DetailsCandidatResponse> >(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DetailsCandidatResponse>> all(){
        List<DetailsCandidatResponse> response = service.all();
        return new ResponseEntity< List<DetailsCandidatResponse> >(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<DetailsCandidatResponse>> one(@PathVariable(value = "id") String id) {
        Optional<DetailsCandidatResponse> response = service.oneById(id);
        return new ResponseEntity<Optional<DetailsCandidatResponse>>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<DetailsCandidatResponse> add(@RequestBody @Valid DetailsCandidatRequest request) {
        DetailsCandidatResponse response = service.add(request);
        return new ResponseEntity<DetailsCandidatResponse>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<DetailsCandidatResponse> maj(@PathVariable(value="id") String id,
                                                       @RequestBody @Valid DetailsCandidatRequest request) {
        DetailsCandidatResponse response = service.maj(request, id);
        return new ResponseEntity<DetailsCandidatResponse>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

