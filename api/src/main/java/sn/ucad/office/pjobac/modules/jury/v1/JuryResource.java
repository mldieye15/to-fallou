package sn.ucad.office.pjobac.modules.jury.v1;

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
import sn.ucad.office.pjobac.exception.ResourceNotFoundException;
import sn.ucad.office.pjobac.modules.centre.dto.CentreResponse;
import sn.ucad.office.pjobac.modules.jury.JuryService;
import sn.ucad.office.pjobac.modules.jury.dto.JuryRequest;
import sn.ucad.office.pjobac.modules.jury.dto.JuryResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/v1/jurys")
@RequiredArgsConstructor
public class JuryResource {
    private final JuryService service;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<JuryResponse>> allPaginate(
            @SortDefault(sort = "liblleLong") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<JuryResponse>  response = service.all(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<JuryResponse>> all(){
        List<JuryResponse> response = service.all();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/allBySession")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<JuryResponse>> allBySession(){
        List<JuryResponse> response = service.allBySession();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/by-centre/{centreId}")
    public ResponseEntity<List<JuryResponse>> juryNonAffecterByCentre(@PathVariable String centreId) {
        List<JuryResponse> response = service.juryNonAffecterByCentre(centreId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<JuryResponse>> one(@PathVariable(value = "id") String id) {
        Optional<JuryResponse> response = service.oneById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<JuryResponse> add(@RequestBody @Valid JuryRequest request) {
        JuryResponse response = service.add(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<JuryResponse> maj(@PathVariable(value="id") String id,
                                            @RequestBody @Valid JuryRequest request) {
        JuryResponse response = service.maj(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
//    @GetMapping("/nom-availability")
//    public ResponseEntity<Boolean> checkJuryAvailability(@RequestParam String nom) {
//        try {
//            service.verifyJuryUnique(nom);
//            return ResponseEntity.ok(true);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.ok(false);
//        }
//    }
//    @GetMapping("/nom-availabilityUp")
//    public ResponseEntity<Boolean> checkJuryAvailabilityUp(@RequestParam Long juryId, @RequestParam String libelleLong) {
//        try {
//            boolean isUnique = service.verifyNomUniqueUp(libelleLong, juryId);
//            return ResponseEntity.ok(isUnique);
//        } catch (BusinessResourceException e) {
//            return ResponseEntity.ok(false);
//        }
//    }

    @GetMapping("/numero-availability")
    public ResponseEntity<Boolean> checkJuryAvailability(@RequestParam String numero) {
        try {
            service.verifyNumeroUnique(numero);
            return ResponseEntity.ok(true);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(false);
        }
    }
    @GetMapping("/numero-availabilityUp")
    public ResponseEntity<Boolean> checkJuryAvailabilityUp(@RequestParam Long juryId, @RequestParam String numero) {
        try {
            boolean isUnique = service.verifyNumeroUniqueUp(numero, juryId);
            return ResponseEntity.ok(isUnique);
        } catch (BusinessResourceException e) {
            return ResponseEntity.ok(false);
        }
    }
}

