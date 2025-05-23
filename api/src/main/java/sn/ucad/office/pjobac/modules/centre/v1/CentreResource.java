package sn.ucad.office.pjobac.modules.centre.v1;

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
import sn.ucad.office.pjobac.modules.centre.CentreService;
import sn.ucad.office.pjobac.modules.centre.dto.CentreRequest;
import sn.ucad.office.pjobac.modules.centre.dto.CentreResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/v1/centres")
@RequiredArgsConstructor
public class CentreResource {
    private final CentreService service;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<CentreResponse>> allPaginate(
            @SortDefault(sort = "liblleLong") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<CentreResponse>  response = service.all(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/centre/{villeId}")
    public ResponseEntity<List<CentreResponse>> allAvecQuota(@PathVariable String villeId) {
        List<CentreResponse> response = service.allAvecQuota(villeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/proposition/{villeId}")
    public ResponseEntity<List<CentreResponse>> allAvecQuotaProposition(@PathVariable String villeId) {
        List<CentreResponse> response = service.allAvecQuotaProposition(villeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/centreSecondary/{villeId}")
    public ResponseEntity<List<CentreResponse>> SecondaryByVille(@PathVariable String villeId) {
        List<CentreResponse> response = service.allSecondaryByVille(villeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CentreResponse>> all(){
        List<CentreResponse> response = service.all();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/allWithJury")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CentreResponse>> allWithJury(){
        List<CentreResponse> response = service.allWithJury();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/by-ville/{villeId}")
    public ResponseEntity<List<CentreResponse>> centresAvecQByVille(@PathVariable String villeId) {
        List<CentreResponse> response = service.centreParVilleSansJury(villeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/centreForProposition/{villeId}")
    public ResponseEntity<List<CentreResponse>> centresAvecQForProposition(@PathVariable String villeId) {
        List<CentreResponse> response = service.centreParVilleSansJuryForProposition(villeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<CentreResponse>> one(@PathVariable(value = "id") String id) {
        Optional<CentreResponse> response = service.oneById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<CentreResponse> add(@RequestBody @Valid CentreRequest request) {
        CentreResponse response = service.add(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<CentreResponse> maj(@PathVariable(value="id") String id,
                                              @RequestBody @Valid CentreRequest request) {
        CentreResponse response = service.maj(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/libelle-availability")
    public ResponseEntity<Boolean> checkCentreAvailability(@RequestParam String libelleLong) {
        try {
            service.verifyCentreUnique(libelleLong);
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
    public ResponseEntity<Boolean> checklibelleAvailabilityUp(@RequestParam Long centreId, @RequestParam String libelleLong) {
        try {
            boolean isUnique = service.verifyLibelleLongUniqueUp(libelleLong, centreId);
            return ResponseEntity.ok(isUnique);
        } catch (BusinessResourceException e) {
            return ResponseEntity.ok(false);
        }
    }
    @PutMapping("/update-jury-count")
    public ResponseEntity<String> updateNombreJury() {
        try {
            service.updateAllCentresNombreJury();
            return ResponseEntity.ok("Nombre de jurys mis à jour avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour du nombre de jurys : " + e.getMessage());
        }
    }

}

