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
import sn.ucad.office.pjobac.modules.centre.CentreService;
import sn.ucad.office.pjobac.modules.centre.dto.CentreResponse;
import sn.ucad.office.pjobac.modules.demande.DemandeService;
import sn.ucad.office.pjobac.modules.demande.dto.*;
import sn.ucad.office.pjobac.modules.detailsCandidat.OrdreArriveService;
import sn.ucad.office.pjobac.modules.typeSession.dto.VilleResponse;
import sn.ucad.office.pjobac.modules.ville.VilleService;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pjobac/v1/demandes")
@RequiredArgsConstructor
public class DemandeResource {
    private final DemandeService service;
    private final VilleService villeService;
    private CentreService centreService;
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
    @GetMapping("/allValider")
    // @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DemandeResponse>> allValider(){
        List<DemandeResponse> response = service.allValider();
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
    @GetMapping("/allDemandeProposer")
// @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DemandeResponse>> allDemandeProposer() {
        List<DemandeResponse> response = service.allDemandeProposer();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/allDemandeAccepter")
// @PreAuthorize("hasRole('USER_LISTE') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DemandeResponse>> allDemandeAccepter () {
        List<DemandeResponse> response = service.allDemandeAccepter();
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
    @PutMapping(value = "/maj2/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<DemandeResponse> maj2(@PathVariable(value="id") String id,
                                               @RequestBody @Valid DemandeRequestUpdate request) {
        DemandeResponse response = service.maj2(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping(value = "/accepter/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<DemandeResponse> accepterDemande(@PathVariable(value="id") String id,
                                               @RequestBody @Valid DemandeAccepter accepter) {
        DemandeResponse response = service.proposition(accepter, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping(value = "/nonAffectable/{id}")
    // @PreAuthorize("hasRole('USER_MAJ') or hasRole('ADMIN')")
    public ResponseEntity<DemandeResponse> nonAffectable(@PathVariable(value="id") String id) {
        try {
            service.nonAffectableDemande(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BusinessResourceException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "/annuler/{id}")
    public ResponseEntity<DemandeResponse> rejeterDemande(@PathVariable(value="id") String id) {
        try {
            service.annulerDemande(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BusinessResourceException ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    @PutMapping("/validerSecondary/{userId}")
    public ResponseEntity<DemandeResponse> validerDemandeSecondary(@PathVariable("userId") String userId) {
        try {
            DemandeResponse response = service.validerDemandeSecondary(userId);
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
    @GetMapping("/hasPropositionDemande")
    public ResponseEntity<Boolean> hasPropositionDemande(@RequestParam String userId) {
        boolean result = service.hasPropositionDemande(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
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
    @GetMapping("/quotaProposition")
    public ResponseEntity<Boolean> quotaPropositionByVille(@RequestParam String villeId) {
        boolean result = service.quotaPropositionByVille(villeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/quotaAccepteSecondary")
    public ResponseEntity<Boolean> quotaAccepteByVilleSecondary(@RequestParam String villeId) {
        boolean result = service.quotaAccepteByVilleSecondary(villeId);
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
    @PutMapping("/accepterAll")
    public ResponseEntity<?> accepterAll(@RequestBody List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return new ResponseEntity<>("Aucun ID de demande fourni.", HttpStatus.BAD_REQUEST);
            }
            service.accepterDemande(ids);
            return new ResponseEntity<>(
                    String.format("%d demandes ont été acceptées avec succès.", ids.size()),
                    HttpStatus.OK
            );
        } catch (BusinessResourceException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        } catch (Exception ex) {
            return new ResponseEntity<>("Erreur technique lors de l'acceptation des demandes.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping(value = "/proposition/{id}")
    public ResponseEntity<Optional<VilleResponse>> onePropistion(@PathVariable(value = "id") String id) {
        Optional<VilleResponse> response = villeService.onePropositionById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/centreForProposition/{villeId}")
    public ResponseEntity<List<CentreResponse>> centresAvecQForProposition(@PathVariable String villeId) {
        List<CentreResponse> response = centreService.centreParVilleSansJuryForProposition(villeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

