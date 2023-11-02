package sn.ucad.office.pjobac.modules.typeEtablissement;

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
import sn.ucad.office.pjobac.modules.typeEtablissement.dto.TypeEtablissementAudit;
import sn.ucad.office.pjobac.modules.typeEtablissement.dto.TypeEtablissementRequest;
import sn.ucad.office.pjobac.modules.typeEtablissement.dto.TypeEtablissementResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TypeEtablissementServiceImp implements TypeEtablissementService {
    private final TypeEtablissementMapper mapper;
    private final TypeEtablissementDao dao;

    @Override
    public List<TypeEtablissementResponse> all() throws BusinessResourceException {
        log.info("TypeEtablissementServiceImp::all");
        List<TypeEtablissement> all = dao.findAll();
        List<TypeEtablissementResponse> response = all.stream()
                .map(one -> mapper.toEntiteResponse(one))
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public SimplePage<TypeEtablissementResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("Liste des type de etablissement avec pagination. <all>");
        final Page<TypeEtablissement> page = dao.findAll(pageable);
        return new SimplePage<TypeEtablissementResponse>(page.getContent()
                .stream()
                .map(item -> mapper.toEntiteResponse(item))
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable
        );
    }

    @Override
    public Optional<TypeEtablissementResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            TypeEtablissement one = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Academie avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("typeEtablissement avec id: " + id + " trouvé. <oneById>");
            Optional<TypeEtablissementResponse> response = Optional.ofNullable(mapper.toEntiteResponse(one));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public TypeEtablissementResponse add(TypeEtablissementRequest req) throws BusinessResourceException {
        try {
            log.info("Debug 001-add:  " + req.toString());
            TypeEtablissement one = mapper.requestToEntity(req);
            log.info("Debug 001-req_to_entity:  " + one.toString());
            TypeEtablissementResponse response = mapper.toEntiteResponse(dao.save(one));
            log.info("Ajout " + response.getLibelleLong() + " effectué avec succés. <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de creation typeEtablissement: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout typeEtablissement: Une erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un typeEtablissement: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public TypeEtablissementResponse maj(TypeEtablissementRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            TypeEtablissement typeEtablissementOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun typeEtablissement avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            //Academie annee = mapper.anneRequestToAnneeUp(typeEtablissementOptional, req, userService.user());
            TypeEtablissement oneBrute = mapper.requestToEntiteUp(typeEtablissementOptional, req);
            TypeEtablissementResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("Mise à jour " + response.getLibelleLong() + " effectuée avec succés. <maj>");
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <maj>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj typeEtablissement: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Maj imputation: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de mise à jour d'un typeEtablissement: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String del(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            TypeEtablissement oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun typeEtablissement avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            dao.deleteById(myId);
            log.info("typeEtablissement avec id & matricule: " + id + " & " + oneBrute.getLibelleLong() + " supprimé avec succés. <del>");
            String response = "Imputation: " + oneBrute.getLibelleLong() + " supprimé avec succés. <del>";
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <del>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<TypeEtablissementAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            TypeEtablissement oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune typeEtablissement avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("typeEtablissement avec id: " + id + " trouvé. <auditOneById>");
           Optional<TypeEtablissementAudit> response = Optional.ofNullable(mapper.toEntiteAudit(oneBrute, Long.valueOf("1"), Long.valueOf("1") ));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
        //return Optional.empty();
    }





}
