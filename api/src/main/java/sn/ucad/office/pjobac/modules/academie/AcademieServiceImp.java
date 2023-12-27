package sn.ucad.office.pjobac.modules.academie;

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

import sn.ucad.office.pjobac.modules.academie.dto.*;
import sn.ucad.office.pjobac.modules.demande.Demande;
import sn.ucad.office.pjobac.modules.demande.DemandeDao;
import sn.ucad.office.pjobac.modules.security.token.AuthService;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.ville.Ville;
import sn.ucad.office.pjobac.modules.ville.dto.VilleResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AcademieServiceImp implements AcademieService {
    private final AcademieMapper mapper;
    private final AcademieDao dao;
    private final AuthService authService;
    private  final DemandeDao demandeDao;

    public List<AcademieVille> academieWithVille() throws BusinessResourceException {
        log.info("AcademieServiceImp::academieWithVille");
        return  dao.academieWithVille();
    }

    @Override
    public List<AcademieResponse> all() throws BusinessResourceException {
        log.info("AcademieServiceImp::all");
        List<Academie> all = dao.findAll();
        List<AcademieResponse> response;
        response = all.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public List<AcademieResponse> availableAcademiesForUser( String demandeId) throws BusinessResourceException {
        Long myId= Long.valueOf(demandeId.trim());
        Demande demande;
        demande=demandeDao.findById(myId)
                .orElseThrow(()->new RuntimeException("Académie non trouvée pour l'ID : " + demandeId));
        AppUser currentUser = authService.getCurrentUser();
        List<Academie> academies=dao.availableAcademiesForUser(currentUser,demande);
        List<AcademieResponse> response;
        response= academies.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return  response;
    }
    @Override
    public SimplePage<AcademieResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("Liste des Annees avec pagination. <all>");
        final Page<Academie> page = dao.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable
        );
    }

    @Override
    public Optional<AcademieResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Academie one = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Academie avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Agen avec id: " + id + " trouvé. <oneById>");
            Optional<AcademieResponse> response;
            response = Optional.ofNullable(mapper.toEntiteResponse(one));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public AcademieResponse add(AcademieRequest req) throws BusinessResourceException {
        try {
            log.info("Debug 001-add:  " + req.toString());
            Academie one = mapper.requestToEntity(req);
            log.info("Debug 001-req_to_entity:  " + one.toString());
            AcademieResponse response = mapper.toEntiteResponse(dao.save(one));
            log.info("Ajout " + response.getLibelleLong() + " effectué avec succés. <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de creation Academie: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout Academie: Une erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un Academie: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public AcademieResponse maj(AcademieRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Academie academieOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Academie avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            //Academie annee = mapper.anneRequestToAnneeUp(academieOptional, req, userService.user());
            Academie oneBrute = mapper.requestToEntiteUp(academieOptional, req);
            AcademieResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("Mise à jour " + response.getLibelleLong() + " effectuée avec succés. <maj>");
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <maj>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj Academie: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Maj imputation: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de mise à jour d'un Academie: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String del(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Academie oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Academie avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            dao.deleteById(myId);
            log.info("Academie avec id & matricule: " + id + " & " + oneBrute.getLibelleLong() + " supprimé avec succés. <del>");
            String response;
            response = "Imputation: " + oneBrute.getLibelleLong() + " supprimé avec succés. <del>";
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <del>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<AcademieAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Academie oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Academie avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Academie avec id: " + id + " trouvé. <auditOneById>");
           Optional<AcademieAudit> response;
            response = Optional.ofNullable(mapper.toEntiteAudit(oneBrute, Long.valueOf("1"), Long.valueOf("1") ));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
        //return Optional.empty();
    }

    @Override
    public void verifyAcademieUnique(String libelleLong) throws BusinessResourceException {
        if(dao.findByLibelleLong(libelleLong).isPresent()){
            throw new ResourceAlreadyExists("Le nom de l' academie   existe déjà.");
        }

    }


}
