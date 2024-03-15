package sn.ucad.office.pjobac.modules.typeSession;

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

import sn.ucad.office.pjobac.modules.typeSession.dto.TypeSessionAudit;
import sn.ucad.office.pjobac.modules.typeSession.dto.TypeSessionRequest;
import sn.ucad.office.pjobac.modules.typeSession.dto.TypeSessionResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TypeSessionServiceImp implements TypeSessionService {
    private final TypeSessionMapper mapper;
    private final TypeSessionDao dao;

    @Override
    public List<TypeSessionResponse> all() throws BusinessResourceException {
        log.info("TypeSessionServiceImp::all");
        List<TypeSession> all = dao.findAll();
        List<TypeSessionResponse> response;
        response = all.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public SimplePage<TypeSessionResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("TypeSessionServiceImp::all, pagination");
        final Page<TypeSession> page = dao.findAll(pageable);
        return new SimplePage<TypeSessionResponse>(page.getContent()
                .stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable
        );
    }

    @Override
    public Optional<TypeSessionResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            TypeSession one = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Session avec " + id + " trouvée.", HttpStatus.NOT_FOUND)
                    );
            log.info("Session avec id: " + id + " trouvé. <oneById>");
            Optional<TypeSessionResponse> response;
            response = Optional.ofNullable(mapper.toEntiteResponse(one));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public TypeSessionResponse add(TypeSessionRequest req) throws BusinessResourceException {
        try {
            log.info("Debug 001-add:  " + req.toString());
            TypeSession one = mapper.requestToEntity(req);
            log.info("Debug 001-req_to_entity:  " + one.toString());
            TypeSessionResponse response = mapper.toEntiteResponse(dao.save(one));
            log.info("Ajout " + response.getLibelleLong() + " effectué avec succés. <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de creation Session: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout Session: Une erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un Session: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public TypeSessionResponse maj(TypeSessionRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            TypeSession typeSessionOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Session avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            TypeSession oneBrute = mapper.requestToEntiteUp(typeSessionOptional, req);
            TypeSessionResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("Mise à jour " + response.getLibelleLong() + " effectuée avec succés. <maj>");
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <maj>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj Session: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Maj imputation: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de mise à jour d'un Session: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String del(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            TypeSession oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Session avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            dao.deleteById(myId);
            log.info("Session avec id & matricule: " + id + " & " + oneBrute.getLibelleLong() + " supprimé avec succés. <del>");
            String response;
            response = "Imputation: " + oneBrute.getLibelleLong() + " supprimé avec succés. <del>";
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <del>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<TypeSessionAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            TypeSession oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Session avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Session avec id: " + id + " trouvé. <auditOneById>");
           Optional<TypeSessionAudit> response;
            response = Optional.ofNullable(mapper.toEntiteAudit(oneBrute, Long.valueOf("1"), Long.valueOf("1") ));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public void verifyTypeSessionUnique(String libelleLong) throws BusinessResourceException {
        String normalizedLibelleLong = libelleLong.replaceAll("\\s+", " ").trim();
        if(dao.findByLibelleLong(normalizedLibelleLong).isPresent()){
            throw new ResourceAlreadyExists("L' annee existe déjà.");
        }

    }

    @Override
    public boolean verifyLibelleLongUniqueUp(String libelleLong, Long id) throws BusinessResourceException {
        Optional<TypeSession> existingLibelleLong = dao.findByLibelleLongAndIdNot(libelleLong,id);
        if (existingLibelleLong.isPresent()) {
            throw new ResourceAlreadyExists("Le libelle existe déjà pour un autre centre.");
        }
        return false;
    }


}
