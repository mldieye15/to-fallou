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
import sn.ucad.office.pjobac.exception.ResourceAlreadyExists;
import sn.ucad.office.pjobac.exception.ResourceNotFoundException;
import sn.ucad.office.pjobac.modules.security.user.UserService;
import sn.ucad.office.pjobac.modules.security.user.dto.UserRequest;
import sn.ucad.office.pjobac.modules.security.user.dto.UserResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/api/v1/users")
@RequiredArgsConstructor
public class UserResource {
    private final UserService service;

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<UserResponse>> allPaginate(
            @SortDefault(sort = "email") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<UserResponse>  response = service.all(pageable);
        return new ResponseEntity< SimplePage<UserResponse> >(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserResponse>> all(){
        List<UserResponse> response = service.all();
        return new ResponseEntity< List<UserResponse> >(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<UserResponse>> one(@PathVariable(value = "id") String id) {
        Optional<UserResponse> response = service.oneById(id);
        return new ResponseEntity<Optional<UserResponse>>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> add(@RequestBody @Valid UserRequest request) {
        UserResponse response = service.add(request);
        return new ResponseEntity<UserResponse>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> maj(@PathVariable(value="id") String id,
                                            @RequestBody @Valid UserRequest request) {
        UserResponse response = service.maj(request, id);
        return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
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
    @GetMapping("/matricule-availability")
    public ResponseEntity<Boolean> checkMatriculeAvailability(@RequestParam String matricule) {
        try {
            service.verifyMatriculeUnique(matricule);
            return ResponseEntity.ok(true);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(false);
        }
    }
    @GetMapping("/username-availability")
    public ResponseEntity<Boolean> verifyUsernamelUnique(@RequestParam String username) {
        try {
            service.verifyUsernamelUnique(username);
            return ResponseEntity.ok(true);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(false);
        }
    }
}


