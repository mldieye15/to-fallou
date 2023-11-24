package sn.ucad.office.pjobac.modules.typeCentre;

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
import sn.ucad.office.pjobac.modules.typeCentre.dto.TypeCentreAudit;
import sn.ucad.office.pjobac.modules.typeCentre.dto.TypeCentreRequest;
import sn.ucad.office.pjobac.modules.typeCentre.dto.TypeCentreResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TypeCentreServiceImp implements TypeCentreService {
    private final TypeCentreMapper mapper;
    private final TypeCentreDao dao;

    @Override
    public List<TypeCentreResponse> all() throws BusinessResourceException {
        log.info("TypeCentreServiceImp::all");
        List<TypeCentre> all = dao.findAll();
        List<TypeCentreResponse> response = all.stream()
                .map(one -> mapper.toEntiteResponse(one))
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public SimplePage<TypeCentreResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("Liste des typeCentres avec pagination. <all>");
        final Page<TypeCentre> page = dao.findAll(pageable);
        return new SimplePage<TypeCentreResponse>(page.getContent()
                .stream()
                .map(item -> mapper.toEntiteResponse(item))
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable
        );
    }

    @Override
    public Optional<TypeCentreResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            TypeCentre one = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun TypeCentre avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Agen avec id: " + id + " trouvé. <oneById>");
            Optional<TypeCentreResponse> response = Optional.ofNullable(mapper.toEntiteResponse(one));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public TypeCentreResponse add(TypeCentreRequest req) throws BusinessResourceException {
        try {
            log.info("Debug 001-add:  " + req.toString());
            TypeCentre one = mapper.requestToEntity(req);
            log.info("Debug 001-req_to_entity:  " + one.toString());
            TypeCentreResponse response = mapper.toEntiteResponse(dao.save(one));
            log.info("Ajout " + response.getLibelleLong() + " effectué avec succés. <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de creation TypeCentre: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout TypeCentre: Une erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un TypeCentre: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public TypeCentreResponse maj(TypeCentreRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            TypeCentre typeCentreOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun TypeCentre avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            //TypeCentre typeCentre = mapper.anneRequestTotypeCentreUp(typeCentreOptional, req, userService.user());
            TypeCentre oneBrute = mapper.requestToEntiteUp(typeCentreOptional, req);
            TypeCentreResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("Mise à jour " + response.getLibelleLong() + " effectuée avec succés. <maj>");
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <maj>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj TypeCentre: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Maj imputation: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de mise à jour d'un TypeCentre: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String del(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            TypeCentre oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun TypeCentre avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            dao.deleteById(myId);
            log.info("TypeCentre avec id & matricule: " + id + " & " + oneBrute.getLibelleLong() + " supprimé avec succés. <del>");
            String response;
            response = "Imputation: " + oneBrute.getLibelleLong() + " supprimé avec succés. <del>";
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <del>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<TypeCentreAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            TypeCentre oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune TypeCentre avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("TypeCentre avec id: " + id + " trouvé. <auditOneById>");
            Optional<TypeCentreAudit> response;
            response = (Optional<TypeCentreAudit>) Optional.ofNullable(mapper.toEntiteAudit(oneBrute, Long.valueOf("1"), Long.valueOf("1") ));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
        //return Optional.empty();
    }

    @Override
    public void verifyLibelleUnique(String libelleLong) throws BusinessResourceException {
        if (dao.findByLibelleLong(libelleLong).isPresent()){
            throw new ResourceAlreadyExists("Ce type de centre est deja existe déjà");
        }

    }


}
