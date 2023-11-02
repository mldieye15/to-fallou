package sn.ucad.office.pjobac.modules.security.role.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.modules.security.role.RoleService;
import sn.ucad.office.pjobac.modules.security.role.dto.RoleRequest;
import sn.ucad.office.pjobac.modules.security.role.dto.RoleResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;

@RestController
@RequestMapping("/ipms/api/v1/roles")
@RequiredArgsConstructor
public class RoleResource {
    private final RoleService service;

    @GetMapping("")
    // @PreAuthorize("hasRole('ROLE_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<RoleResponse>> allPaginate(
            @SortDefault(sort = "nom") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<RoleResponse>  response = service.all(pageable);
        return new ResponseEntity< SimplePage<RoleResponse> >(response, HttpStatus.OK);
    }
    @GetMapping("/all")
    // @PreAuthorize("hasRole('ROLE_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RoleResponse>> all(){
        List<RoleResponse> response = service.all();
        return new ResponseEntity< List<RoleResponse> >(response, HttpStatus.OK);
    }

    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('ROLE_ADD') or hasRole('ADMIN')")
    public ResponseEntity<RoleResponse> add(@RequestBody @Valid RoleRequest request) {
        RoleResponse response = service.add(request);
        return new ResponseEntity<RoleResponse>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('ROLE_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<RoleResponse> maj(@PathVariable(value="id") String id,
                                            @RequestBody @Valid RoleRequest request) {
        RoleResponse response = service.maj(request, id);
        return new ResponseEntity<RoleResponse>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('ROLE_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
