package sn.ucad.office.pjobac.modules.detailsCandidat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.exception.ResourceAlreadyExists;
import sn.ucad.office.pjobac.modules.annee.Annee;
import sn.ucad.office.pjobac.modules.annee.AnneeDao;
import sn.ucad.office.pjobac.modules.demande.DemandeDao;
import sn.ucad.office.pjobac.modules.demande.DemandeMapper;
import sn.ucad.office.pjobac.modules.detailsCandidat.dto.*;
import sn.ucad.office.pjobac.modules.security.token.AuthService;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.ville.Ville;
import sn.ucad.office.pjobac.modules.ville.VilleDao;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Lazy
@Slf4j
@RequiredArgsConstructor
public class DetailsCandidatServiceImp implements DetailsCandidatService {
    private final DetailsCandidatMapper mapper;
    private final DetailsCandidatDao dao;
    private final AuthService authService;
    private final AnneeDao anneeDao;
    private  final VilleDao villeDao;
    private final DemandeMapper demandeMapper;
    private final OrdreArriveService ordreArrive;
    @Override
    public List<DetailsCandidatResponse> all() throws BusinessResourceException {
        log.info("AcademieServiceImp::all");
        List<DetailsCandidat> all = dao.findAll();
        List<DetailsCandidatResponse> response;
        response = all.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public SimplePage<DetailsCandidatResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("Liste des Annees avec pagination. <all>");
        final Page<DetailsCandidat> page = dao.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable
        );
    }

    @Override
    public Optional<DetailsCandidatResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            DetailsCandidat one = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Academie avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Agen avec id: " + id + " trouvé. <oneById>");
            Optional<DetailsCandidatResponse> response;
            response = Optional.ofNullable(mapper.toEntiteResponse(one));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public DetailsCandidatResponse add(DetailsCandidatRequest req) throws BusinessResourceException {
        try {
            log.info("Debug 001-add:  " + req.toString());
            DetailsCandidat one = mapper.requestToEntity(req);
            AppUser currentUser = authService.getCurrentUser();
            Annee annee= anneeDao.findByEncoursTrue();
            int noteFonction= currentUser.getFonction().getNombrePoint();
            int noteEtablissementProvenance=currentUser.getEtablissement().getTypeEtablissement().getNombrePoint();
            one.setCandidat(currentUser);
            one.setNoteEtablissementProvenance(noteEtablissementProvenance);
            one.setNoteFonction(noteFonction);
            one.setAnnee(annee);
            log.info("Debug 001-req_to_entity:  " + one.toString());
            DetailsCandidatResponse response = mapper.toEntiteResponse(dao.save(one));
            log.info("Ajout " + response.getCandidat().getPrenoms() + " effectué avec succés. <add>");
            dao.updateNoteBy(currentUser);
            ordreArrive.updateOrderByVille();
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
    public DetailsCandidatResponse maj(DetailsCandidatRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            DetailsCandidat detailsCandidatOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Academie avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            DetailsCandidat oneBrute = mapper.requestToEntiteUp(detailsCandidatOptional, req);
            DetailsCandidatResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("Mise à jour " + response.getAppreciation() + " effectuée avec succés. <maj>");
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

    @Transactional(readOnly = false)
    @Override
    public DetailsCandidatResponse note(DetailsCandidatNoteRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            DetailsCandidat detailsCandidatOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Academie avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            DetailsCandidat oneBrute = mapper.requestNoteToEntiteUp(detailsCandidatOptional, req);
            DetailsCandidatResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("appreciation " + response.getAppreciation() + " effectuée avec succés. <appreciation>");
            dao.updateNote(detailsCandidatOptional);
            ordreArrive.updateOrderByVille();
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <appreciation>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique l'appreciation: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Maj imputation: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de  l'appreciation: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional(readOnly = false)
    @Override
    public DetailsCandidatResponse malus(DetailsCandidatMalusRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            DetailsCandidat detailsCandidatOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Academie avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            DetailsCandidat oneBrute = mapper.requestMalusToEntiteUp(detailsCandidatOptional, req);
            DetailsCandidatResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("Mise à jour " + response.getAppreciation() + " effectuée avec succés. <maj>");
            dao.updateNote(detailsCandidatOptional);
            ordreArrive.updateOrderByVille();
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
    @Transactional(readOnly = false)
    @Override
    public DetailsCandidatResponse bonus(DetailsCandidatBonusRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            DetailsCandidat detailsCandidatOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Academie avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            DetailsCandidat oneBrute = mapper.requestBonusToEntiteUp(detailsCandidatOptional, req);
            DetailsCandidatResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("Mise à jour " + response.getAppreciation() + " effectuée avec succés. <maj>");
            dao.updateNote(detailsCandidatOptional);
            ordreArrive.updateOrderByVille();
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
            DetailsCandidat oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Academie avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            dao.deleteById(myId);
            log.info("Academie avec id & matricule: " + id + " & " + oneBrute.getId() + " supprimé avec succés. <del>");
            String response;
            response = "Imputation: " + oneBrute.getId() + " supprimé avec succés. <del>";
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <del>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<DetailsCandidatAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            DetailsCandidat oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Academie avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Academie avec id: " + id + " trouvé. <auditOneById>");
           Optional<DetailsCandidatAudit> response;
            response = Optional.ofNullable(mapper.toEntiteAudit(oneBrute, Long.valueOf("1"), Long.valueOf("1") ));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Integer maxNoteByVille(String villeId) {
        try {
            Long myId = Long.valueOf(villeId.trim());
            Ville ville = villeDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Ville avec " + villeId+ " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Ville avec id: " + villeId + " trouvé. <auditOneById>");
            return dao.maxNote(ville);
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " +villeId+ " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + villeId + " non autorisé.", HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public void nonAffectable(Long candidatId) {
        DetailsCandidat candidat = dao.findById(candidatId).orElse(null);

        if (candidat != null) {
            if (candidat.isAffectable()) {
                try {
                    candidat.setAffectable(!candidat.isAffectable());
                    dao.save(candidat);
                    log.info("Affectabilité avec l'ID " + candidatId + " changée avec succès.");
                } catch (Exception e) {
                    log.error("Erreur lors de la sauvegarde de l'état d'affectabilité pour l'ID " + candidatId, e);
                }
            } else {
                log.warn("L'état d'affectabilité pour l'ID " + candidatId + " est déjà false. Aucun changement effectué.");
            }
        } else {
            log.warn("Candidat avec l'ID " + candidatId + " non trouvé.");
        }
    }


//    @Override
//    public void updateOrderByVille() throws BusinessResourceException {
//            log.info("DemandeServiceImp::updateOrderByVille");
//            // Récupérer toutes les demandes avec les détails de chaque candidat
//            List<Demande> all = demandeDao.findAll();
//            // Regrouper les demandes par ville
//            Map<Long, List<DemandeDetailsCandidatResponse>> groupedByVille = all.stream()
//                    .collect(Collectors.groupingBy(demande -> demande.getVille().getId(),
//                            Collectors.mapping(demande -> demandeMapper.mapToResponse(demande,
//                                    dao.detailsForUser(demande.getUser())), Collectors.toList())));
//            // Pour chaque ville, trier les candidats par note et mettre à jour l'ordre d'arrivée
//            groupedByVille.forEach((city, candidates) -> {
//                candidates.sort(Comparator.comparingInt(DemandeDetailsCandidatResponse::getNote).reversed());
//                // Mettre à jour l'ordre d'arrivée
//                int order = 1;
//                for (DemandeDetailsCandidatResponse candidate : candidates) {
//                    Demande demande = demandeDao.findById(candidate.getDemandeId()).orElse(null);
//                    if (demande != null) {
//                        demande.setOrdreArrivee(order++);
//                        demandeDao.save(demande);
//                    }
//                }
//            });
//    }

}
