package sn.ucad.office.pjobac.modules.etatDemande;

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

import sn.ucad.office.pjobac.modules.etatDemande.dto.EtatDemandeAudit;
import sn.ucad.office.pjobac.modules.etatDemande.dto.EtatDemandeRequest;
import sn.ucad.office.pjobac.modules.etatDemande.dto.EtatDemandeResponse;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EtatDemandeServiceImp implements EtatDemandeService {
    private final EtatDemandeMapper mapper;
    private final EtatDemandeDao dao;

    @Override
    public List<EtatDemandeResponse> all() throws BusinessResourceException {
        log.info("EtatDemandeServiceImp::all");
        List<EtatDemande> all = dao.findAll();
        List<EtatDemandeResponse> response;
        response = all.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public SimplePage<EtatDemandeResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("Liste des EtatDemandes avec pagination. <all>");
        final Page<EtatDemande> page = dao.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable
        );
    }

    @Override
    public Optional<EtatDemandeResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            EtatDemande one = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun EtatDemande avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Agen avec id: " + id + " trouvé. <oneById>");
            Optional<EtatDemandeResponse> response;
            response = Optional.ofNullable(mapper.toEntiteResponse(one));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public EtatDemandeResponse add(EtatDemandeRequest req) throws BusinessResourceException {
        try {
            log.info("Debug 001-add:  " + req.toString());
            EtatDemande one = mapper.requestToEntity(req);
            log.info("Debug 001-req_to_entity:  " + one.toString());
            EtatDemandeResponse response = mapper.toEntiteResponse(dao.save(one));
            log.info("Ajout " + response.getLibelleLong() + " effectué avec succés. <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de creation EtatDemande: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout EtatDemande: Une erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un EtatDemande: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public EtatDemandeResponse maj(EtatDemandeRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            EtatDemande etatDemandeOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun EtatDemande avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            //EtatDemande annee = mapper.anneRequestToAnneeUp(etatDemandeOptional, req, userService.user());
            EtatDemande oneBrute = mapper.requestToEntiteUp(etatDemandeOptional, req);
            EtatDemandeResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("Mise à jour " + response.getLibelleLong() + " effectuée avec succés. <maj>");
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <maj>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj EtatDemande: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Maj imputation: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de mise à jour d'un EtatDemande: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String del(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            EtatDemande oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun EtatDemande avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            dao.deleteById(myId);
            log.info("EtatDemande avec id & matricule: " + id + " & " + oneBrute.getLibelleLong() + " supprimé avec succés. <del>");
            String response;
            response = "Imputation: " + oneBrute.getLibelleLong() + " supprimé avec succés. <del>";
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <del>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<EtatDemandeAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            EtatDemande oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune EtatDemande avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("EtatDemande avec id: " + id + " trouvé. <auditOneById>");
           Optional<EtatDemandeAudit> response;
            response = Optional.ofNullable(mapper.toEntiteAudit(oneBrute, Long.valueOf("1"), Long.valueOf("1") ));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<Long> findIdByLibelleLong(String libelleLong) throws BusinessResourceException {
        Optional<EtatDemande> etatDemande;
        etatDemande = Optional.ofNullable(dao.findByLibelleLong(libelleLong).orElseThrow(
                () -> new BusinessResourceException("not-found", "Aucun EtatDemande avec " + libelleLong + " trouvé.", HttpStatus.NOT_FOUND)
        ));
        return etatDemande.map(EtatDemande::getId);
    }

    @Override
    public Optional<EtatDemande> findByLibelleLong(String libelleLong) throws BusinessResourceException {
        try {
            Optional<EtatDemande> response = dao.findByLibelleLong(libelleLong);
            log.info("EtatDemande avec libelleLong: " + libelleLong + " trouvé. <etatDemandeBy libelleLong>");
            return response;
        } catch (Exception ex) {
            log.error("EtatDemande by libelleLong: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("not-found", "EtatDemande avec libelleLong: " + libelleLong + " non trouvé(e).", HttpStatus.NOT_FOUND);
        }
    }


}
