package sn.ucad.office.pjobac.modules.ville;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.exception.ResourceAlreadyExists;
import sn.ucad.office.pjobac.modules.academie.Academie;
import sn.ucad.office.pjobac.modules.academie.AcademieDao;
import sn.ucad.office.pjobac.modules.demande.DemandeDao;
import sn.ucad.office.pjobac.modules.security.token.AuthService;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.security.user.UserDao;
import sn.ucad.office.pjobac.modules.ville.dto.VilleAudit;
import sn.ucad.office.pjobac.modules.ville.dto.VilleRequest;
import sn.ucad.office.pjobac.modules.ville.dto.VilleResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@Slf4j
@RequiredArgsConstructor
public class VilleServiceImp implements VilleService {
    private final VilleMapper mapper;
    private final VilleDao dao;
    private final AcademieDao academieDao;
    private final AuthService authService;
    private final UserDao userDao;
    private  final DemandeDao demandeDao;

    public List<VilleResponse> all() throws BusinessResourceException {
        log.info("VilleServiceImp::all");
        List<Ville> all = dao.findAll();
        List<VilleResponse> response;
        response = all.stream()
                .map(ville -> {
                    int totalDemandes = demandeDao.totalDemandeByVille(ville); // Ajout de la méthode totalDemandeByVille
                    int nombreJurys = ville.getTotalJury(); // Obtenez le nombre de jurys de l'objet Ville

                    // Calculer le rapport
                    double rapport = totalDemandes != 0 ? (double) nombreJurys / totalDemandes : 0;
                    rapport = Math.round(rapport * 100.0) / 100.0;// Si le total des demandes est différent de zéro, calculer le rapport, sinon, donner la valeur 0

                    VilleResponse villeResponse = mapper.toEntiteResponse(ville);
                    villeResponse.setTotalDemandes(totalDemandes); // Ajout du total des demandes dans l'objet VilleResponse
                    villeResponse.setRapportJuryDemande(rapport); // Ajout du rapport dans l'objet VilleResponse
                    return villeResponse;
                })
                .sorted(Comparator.comparingDouble(VilleResponse::getRapportJuryDemande).reversed()) // Ordonner les villes par rapport de façon décroissante
                .collect(Collectors.toList());

        return response;
    }
    @Override
    public List<VilleResponse> getVilleByAcademie(String idAcademie) throws BusinessResourceException {
        Long myId= Long.valueOf(idAcademie.trim());
        Academie academie;
        academie=academieDao.findById(myId)
                .orElseThrow(()->new RuntimeException("Académie non trouvée pour l'ID : " + idAcademie));
        List<Ville> villes=dao.findByAcademie(academie);
        List<VilleResponse> response;
        response= villes.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public List<VilleResponse> availableVillesForUserAndAcademy(String academieId) {
        Long myId= Long.valueOf(academieId.trim());
        Academie academie;
        academie=academieDao.findById(myId)
                .orElseThrow(()->new RuntimeException("Académie non trouvée pour l'ID : " + academieId));
        AppUser currentUser = authService.getCurrentUser();
        log.info("User: {}", currentUser.getUsername());
        log.info("Academie: {}", academie.getLibelleLong());
        List<Ville> villes=dao.availableVillesForUserAndAcademy(currentUser,academie);
        List<VilleResponse> response;
        response= villes.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        log.info("Result size: {}", response.size());
        return  response;
    }

    @Override
    public SimplePage<VilleResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("Liste des villes avec pagination. <all>");
        final Page<Ville> page = dao.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable
        );
    }

    @Override
    public Optional<VilleResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Ville one = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Ville avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Agen avec id: " + id + " trouvé. <oneById>");
            // Récupérer le total des demandes pour cette ville spécifique
            int totalDemandes = demandeDao.totalDemandeByVille(one);

            int nombreJurys = one.getTotalJury();
                // Calculer le rapport
                double rapport = totalDemandes != 0 ? (double) nombreJurys / totalDemandes : 0;
                rapport = Math.round(rapport * 100.0) / 100.0;// Si le total des demande
                // Créer le VilleResponse en incluant le total des demandes et le rapport
                VilleResponse villeResponse = mapper.toEntiteResponse(one);
                villeResponse.setTotalDemandes(totalDemandes);
                villeResponse.setRapportJuryDemande(rapport);
                Optional<VilleResponse> response;
                response = Optional.of(villeResponse);
                return response;
                // Si le total des demandes est zéro, renvoyer une valeur par défaut pour le rapport
        } catch (NumberFormatException e) {
            log.warn("Paramètre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramètre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public VilleResponse add(VilleRequest req) throws BusinessResourceException {
        try {
            log.info("Debug 001-add:  " + req.toString());
            Ville one = mapper.requestToEntity(req);
            log.info("Debug 001-req_to_entity:  " + one.toString());
            VilleResponse response = mapper.toEntiteResponse(dao.save(one));
            log.info("Ajout " + response.getLibelleLong() + " effectué avec succés. <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de creation Ville: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout Ville: Une erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un Ville: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public VilleResponse maj(VilleRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Ville villeOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Ville avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            //Ville annee = mapper.anneRequestToAnneeUp(villeOptional, req, userService.user());
            Ville oneBrute = mapper.requestToEntiteUp(villeOptional, req);
            VilleResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("Mise à jour " + response.getLibelleLong() + " effectuée avec succés. <maj>");
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <maj>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj Ville: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Maj imputation: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de mise à jour d'un Ville: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String del(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Ville oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Ville avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            dao.deleteById(myId);
            log.info("Ville avec id & matricule: " + id + " & " + oneBrute.getLibelleLong() + " supprimé avec succés. <del>");
            String response;
            response = "Imputation: " + oneBrute.getLibelleLong() + " supprimé avec succés. <del>";
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <del>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    public Optional<VilleAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Ville oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Ville avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Ville avec id: " + id + " trouvé. <auditOneById>");
           Optional<VilleAudit> response;
            response = Optional.ofNullable(mapper.toEntiteAudit(oneBrute, Long.valueOf("1"), Long.valueOf("1") ));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
        //return Optional.empty();
    }
    @Override
    public void verifyVilleUnique(String libelleLong) throws BusinessResourceException {
        String normalizedLibelleLong = libelleLong.replaceAll("\\s+", " ").trim();
        if(dao.findByLibelleLong(normalizedLibelleLong).isPresent()){
            throw new ResourceAlreadyExists("Le nom de la ville   existe déjà.");
        }

    }
    @Override
    public void verifyUniqueLibelleCourt(String libelleCourt) throws BusinessResourceException {
        String normalizedLibelleCourt = libelleCourt.replaceAll("\\s+", " ").trim();
        if(dao.findByLibelleCourt(normalizedLibelleCourt).isPresent()){
            throw new ResourceAlreadyExists("Le code de la ville   existe déjà.");
        }

    }
}
