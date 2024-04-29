package sn.ucad.office.pjobac.modules.demande.v1;

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
import sn.ucad.office.pjobac.modules.demande.DemandeService;
import sn.ucad.office.pjobac.modules.demande.dto.*;
import sn.ucad.office.pjobac.modules.detailsCandidat.DetailsCandidatService;
import sn.ucad.office.pjobac.modules.detailsCandidat.OrdreArriveService;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/api/v1/demandes")
@RequiredArgsConstructor
public class DemandeResource {
    private final DemandeService service;
    private final OrdreArriveService ordreArrive;

    @GetMapping("")
    //@PreAuthorize("hasRole('ROLE_USER_LISTE') or hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SimplePage<DemandeResponse>> allPaginate(
            @SortDefault(sort = "liblleLong") @PageableDefault(size = AppConstants.DEFAULT_PAGE_SIZE) final Pageable pageable
    ){
        SimplePage<DemandeResponse>  response = service.all(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/obselete/{villeId}")
    public ResponseEntity<String> demandePourVille(@PathVariable Long villeId) {
        try {
           service.demandeObseleteByVille(villeId);
            return new ResponseEntity<>("État de demande mis à jour avec succès pour la ville " + villeId, HttpStatus.OK);
        } catch (BusinessResourceException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
    }
//    @GetMapping("/all")
//    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<List<DemandeResponse>> all(){
//        List<DemandeResponse> response = service.all();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//    @GetMapping("/withAffectable")
//    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<List<DemandeDetailsCandidatResponse>> withAffectable(){
//        List<DemandeDetailsCandidatResponse> response = service.allWithAffectable();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
    @GetMapping("/allForUser")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DemandeResponse>> allForUser(){
        List<DemandeResponse> response = service.allForUser();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/allGroupedByUser")
// @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<Long, List<DemandeDetailsCandidatResponse>>> allGroupedByUser() {
        Map<Long, List<DemandeDetailsCandidatResponse>> response = service.allGroupedByUser();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/allGroupedByUserAndSession/{sessionId}")
// @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<Long, List<DemandeResponse>>> allGroupedByUserAndSession(@PathVariable("sessionId") String sessionId) {
        Map<Long, List<DemandeResponse>> response = service.allGroupedByUserAndSession(sessionId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    @GetMapping("/all")
// @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<Long, List<DemandeDetailsCandidatResponse>>> all() {
        Map<Long, List<DemandeDetailsCandidatResponse>> response = service.all();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/recap")
// @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DemandeResponse>> recap() {
        List<DemandeResponse> response = service.recaptDemandes();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/demandeByVille/{villeId}")
// @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity <List<DemandeDetailsCandidatResponse>> demandeByVille(@PathVariable("villeId") String villeId) {
         List<DemandeDetailsCandidatResponse> response = service.demandeByVille(villeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/allObsolete")
// @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity <List<DemandeDetailsCandidatResponse>> allObsolete() {
        List<DemandeDetailsCandidatResponse> response = service.allObsolete();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/demandeByCentre/{centreId}")
// @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity <List<DemandeDetailsCandidatResponse>> demandeByCentre(@PathVariable("centreId") String centreId) {
        List<DemandeDetailsCandidatResponse> response = service.demandeByCentre(centreId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/demandeBySession/{sessionId}")
// @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<Long, List<DemandeDetailsCandidatResponse>>> demandeBySession(@PathVariable("sessionId") String sessionId) {
        Map<Long, List<DemandeDetailsCandidatResponse>> response = service.demandeBySession(sessionId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<DemandeResponse>> one(@PathVariable(value = "id") String id) {
        Optional<DemandeResponse> response = service.oneById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping(value = "/")
    // @PreAuthorize("hasRole('USER_ADD') or hasRole('ADMIN')")
    public ResponseEntity<DemandeResponse> add(@RequestBody @Valid DemandeRequest request) {
        DemandeResponse response = service.add(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping(value = "/addAll")
    public ResponseEntity<List<DemandeResponse>> addAll(@RequestBody @Valid List<DemandeRequest> requests) {
        List<DemandeResponse> responses = service.addAll(requests);
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }
    @PutMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<DemandeResponse> maj(@PathVariable(value="id") String id,
                                               @RequestBody @Valid DemandeRequest request) {
        DemandeResponse response = service.maj(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping(value = "/accepter/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<DemandeResponse> accepterDemande(@PathVariable(value="id") String id,
                                               @RequestBody @Valid DemandeAccepter accepter) {
        DemandeResponse response = service.accepterDemande(accepter, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping(value = "/affecter/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<DemandeResponse> affecterJury(@PathVariable(value="id") String id,
                                                        @RequestBody @Valid DemandeAffecterJury affecter) {
        DemandeResponse response = service.affecterJury(affecter, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/valider/{userId}")
    public ResponseEntity<DemandeResponse> validerDemande(@PathVariable("userId") String userId) {
        try {
            DemandeResponse response = service.validerDemande(userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BusinessResourceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping(value = "/{id}")
    // @PreAuthorize("hasRole('USER_DEL') or hasRole('ADMIN')")
    public ResponseEntity<Void> del(@PathVariable(value="id") String id) {
        service.del(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/hasAcceptedDemande")
    public ResponseEntity<Boolean> hasAcceptedDemande(@RequestParam String userId) {
        boolean result = service.hasAcceptedDemande(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/quotaAccepte")
    public ResponseEntity<Boolean> quotaAccepteByVille(@RequestParam String villeId) {
        boolean result = service.quotaAccepteByVille(villeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/totalJuryAffecte/{villeId}")
    public ResponseEntity<Integer> getTotalJuryAffecteByVille(@PathVariable String villeId) {
        try {
            int totalJuryAffecte = service.totalJuryAffecteByVille(villeId);
            return new ResponseEntity<>(totalJuryAffecte, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (BusinessResourceException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
//    @GetMapping("/updateOrderByVille")
//    public ResponseEntity<String> updateOrderByVille() {
//        try {
//            ordreArrive.updateOrderByVille();
//            return ResponseEntity.ok("Mise à jour réussie de l'ordre d'arrivée par ville.");
//        } catch (BusinessResourceException e) {
//            // Gérer les exceptions liées aux ressources métier
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour de l'ordre d'arrivée.");
//        }
//    }
    @GetMapping("/updateRangByVille")
    public ResponseEntity<String> updateRangByVille() {
        try {
            ordreArrive.updateRangByVille();
            return ResponseEntity.ok("Mise à jour réussie des rang par ville.");
        } catch (BusinessResourceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la mise à jour de l'ordre d'arrivée.");
        }
    }

}

