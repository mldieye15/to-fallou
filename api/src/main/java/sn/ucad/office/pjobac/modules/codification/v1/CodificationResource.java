package sn.ucad.office.pjobac.modules.codification.v1;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.modules.codification.CodificationService;
import sn.ucad.office.pjobac.modules.codification.dto.CodificationRequest;
import sn.ucad.office.pjobac.modules.codification.dto.CodificationResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/api/v1/codifications")
@RequiredArgsConstructor
public class CodificationResource {
    private final CodificationService service;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<CodificationResponse>> allPaginate(
            @SortDefault(sort = "liblleLong") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<CodificationResponse>  response = service.all(pageable);
        return new ResponseEntity< SimplePage<CodificationResponse> >(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CodificationResponse>> all(){
        List<CodificationResponse> response = service.all();
        return new ResponseEntity< List<CodificationResponse> >(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<CodificationResponse>> one(@PathVariable(value = "id") String id) {
        Optional<CodificationResponse> response = service.oneById(id);
        return new ResponseEntity<Optional<CodificationResponse>>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<CodificationResponse> add(@RequestBody @Valid CodificationRequest request) {
       CodificationResponse response = service.add(request);
        return new ResponseEntity<CodificationResponse>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<CodificationResponse> maj(@PathVariable(value="id") String id,
                                             @RequestBody @Valid CodificationRequest request) {
        CodificationResponse response = service.maj(request, id);
        return new ResponseEntity<CodificationResponse>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @GetMapping("/get-code")
    public ResponseEntity<?> inscriptionCode(@RequestParam String email) {
        CodificationRequest request;
        request = new CodificationRequest(email,null);
        Optional<CodificationResponse> responseOptional = service.inscriptionCode(request);

        if (responseOptional.isPresent()) {
            CodificationResponse response = responseOptional.get();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucun code de vérification trouvé pour l'e-mail : " + email, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestParam String email) throws Exception {
        CodificationRequest request;
        request = new CodificationRequest(email,null);

        try {
            service.sendCode(request);
            return new ResponseEntity<>("Code envoyé avec succès.", HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>("Aucun code de vérification trouvé pour l'e-mail : " + email, HttpStatus.NOT_FOUND);
        }
    }







}

