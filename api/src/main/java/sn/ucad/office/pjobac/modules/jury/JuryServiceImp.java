package sn.ucad.office.pjobac.modules.jury;

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

import sn.ucad.office.pjobac.modules.centre.CentreDao;
import sn.ucad.office.pjobac.modules.jury.dto.JuryAudit;
import sn.ucad.office.pjobac.modules.jury.dto.JuryRequest;
import sn.ucad.office.pjobac.modules.jury.dto.JuryResponse;
import sn.ucad.office.pjobac.modules.ville.VilleDao;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JuryServiceImp implements JuryService {
    private final JuryMapper mapper;
    private final JuryDao dao;
    private final CentreDao centreDao;
    private final VilleDao villeDao;

    @Override
    public List<JuryResponse> all() throws BusinessResourceException {
        log.info("JuryServiceImp::all");
        List<Jury> all = dao.findAll();
        List<JuryResponse> response;
        response = all.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public SimplePage<JuryResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("Liste des jurys avec pagination. <all>");
        final Page<Jury> page = dao.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable
        );
    }

    @Override
    @Transactional(readOnly = false)
    public Optional<JuryResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Jury one = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Jury avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Jury avec id: " + id + " trouvé. <oneById>");
            Optional<JuryResponse> response;
            response = Optional.ofNullable(mapper.toEntiteResponse(one));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public JuryResponse add(JuryRequest req) throws BusinessResourceException {
        try {
            log.info("Debug 001-add:  " + req.toString());
            Jury one = mapper.requestToEntity(req);
            log.info("Debug 001-req_to_entity:  " + one.toString());
            JuryResponse response = mapper.toEntiteResponse(dao.save(one));
            updateCentreTotalJury(one.getCentre().getId());
            updateVilleTotalJury(one.getCentre().getVille().getId());
            log.info("Ajout " + response.getNumero() + " effectué avec succés. <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de creation Jury: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout Jury: Une erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un Jury: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public JuryResponse maj(JuryRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Jury juryOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Jury avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );

            Jury oneBrute = mapper.requestToEntiteUp(juryOptional, req);
            JuryResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("Mise à jour " + response.getNumero() + " effectuée avec succés. <maj>");
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <maj>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj Jury: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Maj imputation: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de mise à jour d'un Jury: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String del(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Jury oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Jury avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            dao.deleteById(myId);
            updateCentreTotalJury(oneBrute.getCentre().getId());
            updateVilleTotalJury(oneBrute.getCentre().getVille().getId());
            log.info("Jury avec id & numero: " + id + " & " + oneBrute.getNumero() + " supprimé avec succés. <del>");
            String response;
            response = "Imputation: " + oneBrute.getNumero() + " supprimé avec succés. <del>";
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <del>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<JuryAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Jury oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Jury avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Jury avec id: " + id + " trouvé. <auditOneById>");
           Optional<JuryAudit> response;
            response = Optional.ofNullable(mapper.toEntiteAudit(oneBrute, Long.valueOf("1"), Long.valueOf("1") ));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public int countJuryByCentre(Long centreId) {

        return centreDao.totalJuryByCentre(centreId);
    }

    @Override
    public void updateCentreTotalJury(Long centreId) {
        int totalJury = centreDao.totalJuryByCentre(centreId);
        centreDao.updateTotalJury(centreId, totalJury);

    }

    @Override
    public int countJuryByVille(Long villeId) {
        return villeDao.getTotalJuryByVilleId(villeId);
    }

    @Override
    public void updateVilleTotalJury(Long villeId) {
        int totalJury=villeDao.getTotalJuryByVilleId(villeId);
        villeDao.updateTotalJury(villeId,totalJury);

    }


}
