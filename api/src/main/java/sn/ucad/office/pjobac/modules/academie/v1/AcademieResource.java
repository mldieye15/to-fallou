package sn.ucad.office.pjobac.modules.academie.v1;

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
import sn.ucad.office.pjobac.modules.academie.AcademieService;
import sn.ucad.office.pjobac.modules.academie.dto.AcademieRequest;
import sn.ucad.office.pjobac.modules.academie.dto.AcademieResponse;

import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/v1/academies")
@RequiredArgsConstructor
public class AcademieResource {
    private final AcademieService service;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<AcademieResponse>> allPaginate(
            @SortDefault(sort = "libelleLong") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<AcademieResponse>  response = service.all(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AcademieResponse>> all(){
        List<AcademieResponse> response = service.all();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/allForSecondary")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AcademieResponse>> allForSecondary(){
        List<AcademieResponse> response = service.allForSecondary();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/availableAcademiesForUser")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AcademieResponse>> availableAcademiesForUser(@RequestParam String demandeId){
        List<AcademieResponse> response = service.availableAcademiesForUser(demandeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<AcademieResponse>> one(@PathVariable(value = "id") String id) {
        Optional<AcademieResponse> response = service.oneById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<AcademieResponse> add(@RequestBody @Valid AcademieRequest request) {
        AcademieResponse response = service.add(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<AcademieResponse> maj(@PathVariable(value="id") String id,
                                                @RequestBody @Valid AcademieRequest request) {
        AcademieResponse response = service.maj(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/libelle-availability")
    public ResponseEntity<Boolean> checkAcademieAvailability(@RequestParam String libelleLong) {
        try {
            service.verifyAcademieUnique(libelleLong);
            return ResponseEntity.ok(true);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(false);
        }
    }
    @GetMapping("/code-availability")
    public ResponseEntity<Boolean> checkCodeAvailability(@RequestParam String libelleCourt) {
        try {
            service.verifyUniqueLibelleCourt(libelleCourt);
            return ResponseEntity.ok(true);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(false);
        }
    }
    @GetMapping("/libelle-availabilityUp")
    public ResponseEntity<Boolean> checklibelleAvailabilityUp(@RequestParam Long academieId, @RequestParam String libelleLong) {
        try {
            boolean isUnique = service.verifyLibelleLongUniqueUp(libelleLong, academieId);
            // Si le libelleLong est unique, retourne true
            return ResponseEntity.ok(isUnique);
        } catch (BusinessResourceException e) {
            // Si le libelleLong n'est pas unique, retourne false
            return ResponseEntity.ok(false);
        }
    }
}

