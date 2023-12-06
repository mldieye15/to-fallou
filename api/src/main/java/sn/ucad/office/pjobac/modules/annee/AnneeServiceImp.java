package sn.ucad.office.pjobac.modules.annee;

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
import sn.ucad.office.pjobac.modules.annee.dto.AnneeAudit;
import sn.ucad.office.pjobac.modules.annee.dto.AnneeRequest;
import sn.ucad.office.pjobac.modules.annee.dto.AnneeResponse;
import sn.ucad.office.pjobac.modules.centre.annee.Annee;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnneeServiceImp implements AnneeService {
    private final AnneeMapper mapper;
    private final AnneeDao dao;

    @Override
    public List<AnneeResponse> all() throws BusinessResourceException {
        log.info("TypeSessionServiceImp::all");
        List<Annee> all = dao.findAll();
        List<AnneeResponse> response;
        response = all.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public List<AnneeResponse> anneeEncours() throws BusinessResourceException {
        Optional <Annee> anneeEncours= Optional.ofNullable(dao.findByEncoursTrue());
        if(anneeEncours.isPresent()){
            AnneeResponse response= mapper.toEntiteResponse(anneeEncours.get());
            return Collections.singletonList(response);
        }else {
            return Collections.emptyList();
        }
    }
    @Override
    public SimplePage<AnneeResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("TypeSessionServiceImp::all, pagination");
        final Page<Annee> page = dao.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable
        );
    }

    @Override
    public Optional<AnneeResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Annee one = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune annee avec " + id + " trouvée.", HttpStatus.NOT_FOUND)
                    );
            log.info("TypeSession avec id: " + id + " trouvé. <oneById>");
            Optional<AnneeResponse> response;
            response = Optional.ofNullable(mapper.toEntiteResponse(one));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public AnneeResponse add(AnneeRequest req) throws BusinessResourceException {
        try {
            log.info("Debug 001-add:  " + req.toString());
            Annee one = mapper.requestToEntity(req);
            log.info("Debug 001-req_to_entity:  " + one.toString());
            AnneeResponse response = mapper.toEntiteResponse(dao.save(one));
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
    public AnneeResponse maj(AnneeRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Annee anneeOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Academie avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            //Academie annee = mapper.anneRequestToAnneeUp(anneeOptional, req, userService.user());
            Annee oneBrute = mapper.requestToEntiteUp(anneeOptional, req);
            AnneeResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
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
            Annee oneBrute = dao.findById(myId)
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
    public Optional<AnneeAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Annee oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Academie avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Academie avec id: " + id + " trouvé. <auditOneById>");
           Optional<AnneeAudit> response;
            response = Optional.ofNullable(mapper.toEntiteAudit(oneBrute, Long.valueOf("1"), Long.valueOf("1") ));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
        //return Optional.empty();
    }

    @Override
    public void verifyAnneeUnique(String libelleLong) throws BusinessResourceException {
        if(dao.findByLibelleLong(libelleLong).isPresent()){
            throw new ResourceAlreadyExists("L' annee existe déjà.");
        }

    }

    @Override
    public void changerEtatAnnee(Long anneeId) {
        Annee annee= dao.findById(anneeId).orElse(null);
        if (annee!=null){
            Annee anneeEncours=dao.findByEncoursTrue();
            if (anneeEncours!=null && !anneeEncours.getId().equals(anneeId)){
                log.warn("Impossible de changer l'état de l'année avec l'ID " + anneeId + " car une autre année est déjà en cours.");
                return;
            }
            annee.setEncours(!annee.isEncours());
            dao.save(annee);
            log.info("État de l'année avec l'ID " + anneeId + " changer avec succès.");
        }else {
            log.warn("Année avec l'ID " +  anneeId + " non trouvée.");
        }
    }


}
