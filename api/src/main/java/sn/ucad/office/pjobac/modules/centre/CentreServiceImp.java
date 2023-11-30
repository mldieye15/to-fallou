package sn.ucad.office.pjobac.modules.centre;

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
import sn.ucad.office.pjobac.modules.centre.dto.CentreAudit;
import sn.ucad.office.pjobac.modules.centre.dto.CentreRequest;
import sn.ucad.office.pjobac.modules.centre.dto.CentreResponse;
import sn.ucad.office.pjobac.modules.jury.JuryService;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CentreServiceImp implements CentreService {
    private final CentreMapper mapper;
    private final CentreDao dao;
    private final JuryService juryService;

    @Override
    public List<CentreResponse> all() throws BusinessResourceException {
        log.info("CentreServiceImp::all");
        List<Centre> all = dao.findAll();
        List<CentreResponse> response;
        response = all.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
    }
//    @Override
//    public List<CentreResponse> all() throws BusinessResourceException {
//        log.info("CentreServiceImp::all");
//        List<Centre> all = dao.findAll();
//        List<CentreResponse> response;
//        response = all.stream()
//                .map(centre -> {
//                    int nombreJury= juryService.countJuryByCentre(centre.getId());
//                    centre.setNombreJury(nombreJury);
//                    return mapper.toEntiteResponse(centre);
//                })
//                .collect(Collectors.toList());
//        return response;
//    }

    @Override
    public SimplePage<CentreResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("Liste des Centres avec pagination. <all>");
        final Page<Centre> page = dao.findAll(pageable);
        return new SimplePage<CentreResponse>(page.getContent()
                .stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable
        );
    }

    @Override
    @Transactional(readOnly = false)
    public Optional<CentreResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Centre one = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Centre avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Centre avec id: " + id + " trouvé. <oneById>");
            int nombreJury = juryService.countJuryByCentre(myId);
            one.setNombreJury(nombreJury);
            Optional<CentreResponse> response;
            response = Optional.ofNullable(mapper.toEntiteResponse(one));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public CentreResponse add(CentreRequest req) throws BusinessResourceException {
        try {
            log.info("Debug 001-add:  " + req.toString());
            Centre one = mapper.requestToEntity(req);
            log.info("Debug 001-req_to_entity:  " + one.toString());
            CentreResponse response = mapper.toEntiteResponse(dao.save(one));
            log.info("Ajout " + response.getLibelleLong() + " effectué avec succés. <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de creation Centre: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout Centre: Une erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un Centre: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public CentreResponse maj(CentreRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Centre centreOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Centre avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            Centre oneBrute = mapper.requestToEntiteUp(centreOptional, req);
            CentreResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("Mise à jour " + response.getLibelleLong() + " effectuée avec succés. <maj>");
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <maj>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj Centre: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Maj imputation: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de mise à jour d'un Centre: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String del(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Centre oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Centre avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
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
    public Optional<CentreAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Centre oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Centre avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Centre avec id: " + id + " trouvé. <auditOneById>");
           Optional<CentreAudit> response;
            response = Optional.ofNullable(mapper.toEntiteAudit(oneBrute, Long.valueOf("1"), Long.valueOf("1") ));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void verifyCentreUnique(String libelleLong) throws BusinessResourceException {
        if(dao.findByLibelleLong(libelleLong).isPresent()){
            throw new ResourceAlreadyExists("Le centre existe déjà.");
        }

    }


}
