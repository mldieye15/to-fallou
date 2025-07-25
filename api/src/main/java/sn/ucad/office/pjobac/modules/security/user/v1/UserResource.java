package sn.ucad.office.pjobac.modules.security.user.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.exception.ResourceNotFoundException;
import sn.ucad.office.pjobac.modules.security.user.ProfileUserServie;
import sn.ucad.office.pjobac.modules.security.user.UserService;
import sn.ucad.office.pjobac.modules.security.user.dto.*;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/v1/users")
@RequiredArgsConstructor
public class UserResource {
    private final UserService service;
    private  final ProfileUserServie profileUserServie;

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<UserResponse>> allPaginate(
            @SortDefault(sort = "email") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<UserResponse>  response = service.all(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserResponse>> all(){
        List<UserResponse> response = service.all();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/allNonAffecter")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserResponse>> allPasValider(){
        List<UserResponse> response = service.allWhoAppliedAndHaveNoValidatedDemandInCurrentSession();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/users")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserResponse>> user(){
        List<UserResponse> response = service.user();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/admins")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AdminResponse>> admin(){
        List<AdminResponse> response = service.admin();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/planificateurs")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AdminResponse>> planificateur(){
        List<AdminResponse> response = service.planificateur();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/supervisseurs")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AdminResponse>> supervisseur(){
        List<AdminResponse> response = service.supervisseur();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<UserResponse>> one(@PathVariable(value = "id") String id) {
        Optional<UserResponse> response = service.oneById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> add(@RequestBody @Valid UserRequest request) {
        UserResponse response = service.add(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> maj(@PathVariable(value="id") String id,
                                            @RequestBody @Valid UserEditRequest request) {
        UserResponse response = service.maj(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping(value = "upProfileUser")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> majProfileUser(@RequestBody @Valid UserEditRequest request) {
        UserResponse response = profileUserServie.majProfileUser(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping(value = "/upProfileAdmin")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<AdminResponse> majProfilAdmin(@RequestBody @Valid AdminEditRequest request) {
        AdminResponse response = profileUserServie.majProfilAdmin(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping(value = "/upAdmin/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<AdminResponse> majAdmin(@PathVariable(value="id") String id,
                                                  @RequestBody @Valid AdminEditRequest request) {
        AdminResponse response = service.majAdmin(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/email-availability")
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
        try {
            service.verifyEmailUnique(email);
            return ResponseEntity.ok(true);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(false);
        }
        }
    @GetMapping("/email-availabilityUp")
    public ResponseEntity<Boolean> checkEmailAvailabilityUp(@RequestParam Long userId, @RequestParam String email) {
        try {
            boolean isUnique = service.verifyEmailUniqueUp(email, userId);
            // Si le matricule est unique, retourne true
            return ResponseEntity.ok(isUnique);
        } catch (BusinessResourceException e) {
            // Si le matricule n'est pas unique, retourne false
            return ResponseEntity.ok(false);
        }
    }
    @GetMapping("/matricule-availabilityUp")
    public ResponseEntity<Boolean> checkMatriculeAvailabilityUp(@RequestParam Long userId, @RequestParam String matricule) {
        try {
            boolean isUnique = service.verifyMatriculeUniqueUp(matricule, userId);
            // Si le matricule est unique, retourne true
            return ResponseEntity.ok(isUnique);
        } catch (BusinessResourceException e) {
            // Si le matricule n'est pas unique, retourne false
            return ResponseEntity.ok(false);
        }
    }
    @GetMapping("/matricule-availability")
    public ResponseEntity<Boolean> checkMatriculeAvailability(@RequestParam String matricule) {
        try {
            service.verifyMatriculeUnique(matricule);
            return ResponseEntity.ok(true);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(false);
        }
    }
    @PutMapping("/{id}/bloquer")
    public ResponseEntity<Void> ListeRouge(@PathVariable Long id) {
        service.listeRouge(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}/listeNoire")
    public ResponseEntity<Void> listeNoire(@PathVariable Long id) {
        service.listeNoire(id);
        return ResponseEntity.ok().build();
    }
}


