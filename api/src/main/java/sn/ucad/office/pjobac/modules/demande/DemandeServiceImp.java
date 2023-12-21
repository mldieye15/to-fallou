package sn.ucad.office.pjobac.modules.demande;

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
import sn.ucad.office.pjobac.modules.demande.dto.DemandeAccepter;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeAudit;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeRequest;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeResponse;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemande;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemandeDao;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemandeServiceImp;
import sn.ucad.office.pjobac.modules.security.mail.MailService;
import sn.ucad.office.pjobac.modules.security.mail.NotificationEmail;
import sn.ucad.office.pjobac.modules.ville.VilleDao;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DemandeServiceImp implements DemandeService {
    private final DemandeMapper mapper;
    private final DemandeDao dao;
    private final EtatDemandeDao etatDemandeDao;
    private final EtatDemandeServiceImp service;
    private  final MailService mailService;
    private final CentreDao centreDao;
    private final VilleDao villeDao;

    @Override
    public List<DemandeResponse> all() throws BusinessResourceException {
        log.info("DemandeServiceImp::all");
        List<Demande> all = dao.findAll();
        List<DemandeResponse> response;
        response = all.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public Map<Long, List<DemandeResponse>> allGroupedByUser() throws BusinessResourceException {
        log.info("DemandeServiceImp::allGroupedByUser");
        List<Demande> all = dao.findAll();
        Map<Long, List<DemandeResponse>> response;
        response = all.stream()
                .collect(Collectors.groupingBy(demande -> demande.getUser().getId(),
                        Collectors.mapping(mapper::toEntiteResponse, Collectors.toList())));

        return response;
    }

    @Override
    public List<DemandeResponse> getAllForUser(Long userId) throws BusinessResourceException {
        return null;
    }

    @Override
    public SimplePage<DemandeResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("Liste des Demandes avec pagination. <all>");
        final Page<Demande> page = dao.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable
        );
    }

    @Override
    public Optional<DemandeResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Demande one = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Demande avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Agen avec id: " + id + " trouvé. <oneById>");
            Optional<DemandeResponse> response;
            response = Optional.ofNullable(mapper.toEntiteResponse(one));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public DemandeResponse add(DemandeRequest req) throws BusinessResourceException {
        try {
            log.info("Debug 001-add:  " + req.toString());
            Demande one = mapper.requestToEntity(req);
            log.info("Debug 001-req_to_entity:  " + one.toString());
            DemandeResponse response = mapper.toEntiteResponse(dao.save(one));
            log.info("Ajout " + response.getId() + " effectué avec succés. <add>");
            return response;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de creation Demande: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout Demande: Une erreur inattandue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un Demande: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<DemandeResponse> addAll(List<DemandeRequest> req) throws BusinessResourceException {
        try {
            List<Demande> demandes = req.stream()
                    .map(request -> {
                        Demande demande = mapper.requestToEntity(request);
                        if (demande.getEtatDemande() == null) {
                            EtatDemande etatParDefaut = service.findIdByLibelleLong("EN ATTENTE")
                                    .map(id->{
                                        EtatDemande etatDemande = new EtatDemande();
                                        etatDemande.setId(id);
                                        return etatDemande;
                                    })
                                    .orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
                            demande.setEtatDemande(etatParDefaut);
                        }
                        return demande;
                    })
                    .collect(Collectors.toList());

            List<DemandeResponse> responses = dao.saveAll(demandes).stream()
                    .map(mapper::toEntiteResponse)
                    .collect(Collectors.toList());

            log.info("Ajout des demandes effectué avec succès. <addAll>");
            return responses;
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de création Demande: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Ajout Demande: Une erreur inattendue est rencontrée." + ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique de création d'un Demande.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    @Transactional(readOnly = false)
    public DemandeResponse maj(DemandeRequest req, String demandeId) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(demandeId.trim());
            Demande demandeOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Demande avec " + demandeId + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            //Demande annee = mapper.anneRequestToAnneeUp(demandeOptional, req, userService.user());
            Demande oneBrute = mapper.requestToEntiteUp(demandeOptional, req);
                EtatDemande etatParDefaut = service.findIdByLibelleLong("EN ATTENTE")
                        .map(id->{
                            EtatDemande etatDemande = new EtatDemande();
                            etatDemande.setId(id);
                            return etatDemande;
                        })
                        .orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
                oneBrute.setEtatDemande(etatParDefaut);

            DemandeResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("Mise à jour " + response.getId() + " effectuée avec succés. <maj>");
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + demandeId+ " non autorisé. <maj>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + demandeId + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (ResourceAlreadyExists | DataIntegrityViolationException e) {
            log.error("Erreur technique de maj Demande: donnée en doublon ou contrainte non respectée" + e.toString());
            throw new BusinessResourceException("data-error", "Donnée en doublon ou contrainte non respectée ", HttpStatus.CONFLICT);
        } catch (Exception ex) {
            log.error("Maj imputation: Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de mise à jour d'un Demande: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    @Transactional(readOnly = false)
    public String del(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Demande oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Demande avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            dao.deleteById(myId);
            log.info("Demande avec id & matricule: " + id + " & " + oneBrute.getId() + " supprimé avec succés. <del>");
            String response;
            response = "Imputation: " + oneBrute.getId() + " supprimé avec succés. <del>";
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <del>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Optional<DemandeAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Demande oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Demande avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Demande avec id: " + id + " trouvé. <auditOneById>");
           Optional<DemandeAudit> response;
            response = Optional.ofNullable(mapper.toEntiteAudit(oneBrute, Long.valueOf("1"), Long.valueOf("1") ));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }

    }
    @Override
    @Transactional(readOnly = false)
    public int countJuryAffecteByCentre(Long centreId) {
        return centreDao.totalJuryAffecteByCentre(centreId);
    }
    @Override
    @Transactional(readOnly = false)
    public void updateCentreTotalJuryAffecte(Long centreId) {
       int totalJuryAffecte =centreDao.totalJuryAffecteByCentre(centreId);
       centreDao.updateTotalJuryAffecte(centreId,totalJuryAffecte);

    }
//    @Override
//    @Transactional(readOnly = false)
//    public int countJuryAffecteByVille(Long villeId) {
//        return villeDao.totalJuryAffecteByVille(villeId);
//    }
    @Override
    @Transactional(readOnly = false)
    public void updateVilleTotalJuryAffecte(Long villeId) {
        EtatDemande etatvalide = service.findIdByLibelleLong("VALIDE")
                .map(id->{
                    EtatDemande etatDemande = new EtatDemande();
                    etatDemande.setId(id);
                    return etatDemande;
                })
                .orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé Valide trouvée.", HttpStatus.NOT_FOUND));
        Long myId= etatvalide.getId();
      int  totalJuryAffecte=villeDao.totalJuryAffecteByVille(villeId,myId);
      villeDao.updateTotalJuryAffecte(villeId,totalJuryAffecte);


    }
    @Override
    @Transactional(readOnly = false)
    public void updateVilleTotalDemandeAccepte(Long villeId) {
        int totalDemandeAccepte=villeDao.getTotalDemandeAccepteByVilleId(villeId);
        villeDao.updateDemandeAccepte(villeId,totalDemandeAccepte);

    }

    @Override
    public void demandeObseleteByVille(Long villeId) throws NumberFormatException, BusinessResourceException {
        EtatDemande etatObsolete = service.findIdByLibelleLong("OBSOLETE")
                .map(id->{
                    EtatDemande etatDemande = new EtatDemande();
                    etatDemande.setId(id);
                    return etatDemande;
                })
                .orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
        EtatDemande etatEnAttente = service.findIdByLibelleLong("EN ATTENTE")
                .map(id->{
                    EtatDemande etatDemande = new EtatDemande();
                    etatDemande.setId(id);
                    return etatDemande;
                })
                .orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));

        dao.demandeObselete(villeId, etatObsolete, etatEnAttente);
    }

    @Override
    public void rejeterDemande(Long userId) throws NumberFormatException, BusinessResourceException {
        EtatDemande enAttente= service.findIdByLibelleLong("EN ATTENTE")
                .map(id->{
                    EtatDemande etatDemande = new EtatDemande();
                    etatDemande.setId(id);
                    return etatDemande;
                })
                .orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
        EtatDemande rejeteEtat = service.findIdByLibelleLong("REJETE")
                .map(id->{
                    EtatDemande etatDemande = new EtatDemande();
                    etatDemande.setId(id);
                    return etatDemande;
                })
                .orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));

        dao.rejeterDemande(userId, rejeteEtat ,enAttente);
    }

    @Override
    @Transactional(readOnly = false)
    public DemandeResponse accepterDemande(DemandeAccepter req, String demandeId) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(demandeId.trim());
            Demande demandeOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Demande avec " + demandeId + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            Demande oneBrute = mapper.accepterToEntiteUp(demandeOptional, req);
            EtatDemande etatAccepter = service.findIdByLibelleLong("ACCEPTE")
                    .map(id->{
                        EtatDemande etatDemande = new EtatDemande();
                        etatDemande.setId(id);
                        return etatDemande;
                    })
                    .orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
            oneBrute.setEtatDemande(etatAccepter);
            DemandeResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            updateCentreTotalJuryAffecte(oneBrute.getCentre().getId());
            updateVilleTotalDemandeAccepte(oneBrute.getCentre().getVille().getId());
            Integer nombreJury= oneBrute.getCentre().getNombreJury();
            Integer nombreJuryAffecte=oneBrute.getCentre().getNombreJuryAffecte();
            Integer totalJury=oneBrute.getCentre().getVille().getTotalJury();
            Integer totalDemandeAccepte=oneBrute.getCentre().getVille().getTotalDemandeAccepte();
            if (nombreJury != null && nombreJuryAffecte != null) {
                int quota = nombreJury - nombreJuryAffecte -1;
                log.info("Calcule du quota: {}", quota);
                if (quota == 0) {
                   centreDao.updateCentreQuota(oneBrute.getCentre().getId());
                }
            }
            if (totalJury != null && totalDemandeAccepte != null) {
                int quotaAccepte = totalJury - totalDemandeAccepte -1;
                log.info("Calcule du quotaAccepte: {}", quotaAccepte);
                if (quotaAccepte == 0) {
                   villeDao.updateVilleQuotaDemandeAccepteTrue(oneBrute.getCentre().getVille().getId());
                }
            }
            NotificationEmail notificationEmail= new NotificationEmail();
            notificationEmail.setSubject("code d'inscription");
            notificationEmail.setRecipient(oneBrute.getUser().getEmail());
            notificationEmail.setBody("Votre demande a ete accepté au centre d' écrit du "
                    + oneBrute.getCentre().getLibelleLong()
                    +"vous avec " + oneBrute.getSession().getDelaisValidation()+ "pour le valider");
            mailService.sendMail(notificationEmail);
            log.info("Demande " + response.getId() + " accepetée avec succés. <accepterDemande>");
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + demandeId+ " non autorisé. <accepterDemande>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + demandeId+ " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("Une erreur inattandue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique d'acceptation d'un Demande: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    @Transactional(readOnly = false)
    public DemandeResponse validerDemande(String demandeId) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(demandeId.trim());
            Demande demandeOptional = dao.findById(myId)
                    .orElseThrow(() -> new BusinessResourceException("not-found", "Aucun Demande avec l'ID " + demandeId + " trouvé.", HttpStatus.NOT_FOUND));

            EtatDemande etatValide = service.findIdByLibelleLong("VALIDE")
                    .map(id -> {
                        EtatDemande etatDemande = new EtatDemande();
                        etatDemande.setId(id);
                        return etatDemande;
                    })
                    .orElseThrow(() -> new BusinessResourceException("not-found", "Aucune état avec le libellé VALIDE trouvé.", HttpStatus.NOT_FOUND));

            demandeOptional.setEtatDemande(etatValide);

            DemandeResponse response = mapper.toEntiteResponse(dao.save(demandeOptional));
            updateVilleTotalJuryAffecte(demandeOptional.getVille().getId());
            rejeterDemande(demandeOptional.getUser().getId());
            Integer totalJuryAffecte=demandeOptional.getVille().getTotalJuryAffecte();
            Integer totalJury=demandeOptional.getVille().getTotalJury();
            if (totalJury != null && totalJuryAffecte != null) {
                int quota = totalJury - totalJuryAffecte -1;
                log.info("Calcule du quota: {}", quota);
                if (quota == 0) {
                    villeDao.updateVilleQuotaTrue(demandeOptional.getVille().getId());
                    demandeObseleteByVille(demandeOptional.getVille().getId());
                }
            }
            NotificationEmail notificationEmail= new NotificationEmail();
            notificationEmail.setSubject("code d'inscription");
            notificationEmail.setRecipient(demandeOptional.getUser().getEmail());
            notificationEmail.setBody("Bravo vous etes president d'un jury dans le centre d' écrit du "
                    + demandeOptional.getCentre().getLibelleLong()
                    );
            mailService.sendMail(notificationEmail);

            log.info("Demande " + response.getId() + " validée avec succès. <validerDemande>");
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramètre id " + demandeId + " non autorisé. <validerDemande>.");
            throw new BusinessResourceException("not-valid-param", "Paramètre " + demandeId + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("Une erreur inattendue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique de validation d'une Demande avec l'ID " + demandeId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
