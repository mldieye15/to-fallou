package sn.ucad.office.pjobac.modules.typeEtablissement.v1;

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
import sn.ucad.office.pjobac.modules.typeEtablissement.TypeEtablissementService;
import sn.ucad.office.pjobac.modules.typeEtablissement.dto.TypeEtablissementRequest;
import sn.ucad.office.pjobac.modules.typeEtablissement.dto.TypeEtablissementResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/v1/typeEtablissements")
@RequiredArgsConstructor
public class TypeEtablissementResource {
    private final TypeEtablissementService service;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<TypeEtablissementResponse>> allPaginate(
            @SortDefault(sort = "liblleLong") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<TypeEtablissementResponse>  response = service.all(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TypeEtablissementResponse>> all(){
        List<TypeEtablissementResponse> response = service.all();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<TypeEtablissementResponse>> one(@PathVariable(value = "id") String id) {
        Optional<TypeEtablissementResponse> response = service.oneById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<TypeEtablissementResponse> add(@RequestBody @Valid TypeEtablissementRequest request) {
        TypeEtablissementResponse response = service.add(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<TypeEtablissementResponse> maj(@PathVariable(value="id") String id,
                                                         @RequestBody @Valid TypeEtablissementRequest request) {
        TypeEtablissementResponse response = service.maj(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/libelle-availability")
    public ResponseEntity<Boolean> checkTypeEtablissementAvailability(@RequestParam String libelleLong) {
        try {
            service.verifyTypeEtablissementUnique(libelleLong);
            return ResponseEntity.ok(true);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(false);
        }
    }
    @GetMapping("/libelle-availabilityUp")
    public ResponseEntity<Boolean> checklibelleAvailabilityUp(@RequestParam Long typeId, @RequestParam String libelleLong) {
        try {
            boolean isUnique = service.verifyLibelleLongUniqueUp(libelleLong, typeId);
            return ResponseEntity.ok(isUnique);
        } catch (BusinessResourceException e) {
            return ResponseEntity.ok(false);
        }
    }
}

