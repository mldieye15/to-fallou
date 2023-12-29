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
import sn.ucad.office.pjobac.modules.security.token.AuthService;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.security.user.UserDao;
import sn.ucad.office.pjobac.modules.ville.Ville;
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
    private final AuthService authService;
    private final UserDao userDao;

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
    public List<DemandeResponse> allForUser() throws BusinessResourceException {
        try {
            AppUser currentUser = authService.getCurrentUser();
            List<Demande> demandes = dao.findByUser(currentUser);
            if (demandes.isEmpty()) {
                log.warn("Aucune demande trouvée pour l'utilisateur avec l'ID : {}", currentUser.getId());
                throw new BusinessResourceException("not-found", "Aucune demande trouvée pour l'utilisateur.", HttpStatus.NOT_FOUND);
            }
            List<DemandeResponse> response;
            response = demandes.stream()
                    .map(mapper::toEntiteResponse)
                    .collect(Collectors.toList());
            return response;
        } catch (Exception ex) {
            log.error("Erreur lors de la récupération des demandes pour l'utilisateur : {}", ex.getMessage());
            throw new BusinessResourceException("technical-error", "Erreur technique lors de la récupération des demandes.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
                            () -> new BusinessResourceException("not-found", "Aucune Demande avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
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
                        AppUser currentUser = authService.getCurrentUser();
                        demande.setUser(currentUser);
                        Optional<EtatDemande> optionalEtat = service.findByLibelleLong("EN ATTENTE");
                        EtatDemande etatParDefaut = optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
                        demande.setEtatDemande(etatParDefaut);
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
            Demande oneBrute = mapper.requestToEntiteUp(demandeOptional, req);
            Optional<EtatDemande> optionalEtat = service.findByLibelleLong("EN ATTENTE");
            EtatDemande etatParDefaut = optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
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
    public int totalJuryAffecteByVille(String villeId) throws NumberFormatException, BusinessResourceException {
        Long myId = Long.valueOf(villeId.trim());
        Ville ville = villeDao.findById(myId)
                .orElseThrow(
                        () -> new BusinessResourceException("not-found", "Aucune Ville avec " + villeId+ " trouvé.", HttpStatus.NOT_FOUND)
                );
        log.info("Ville avec id: " + villeId + " trouvé. <auditOneById>");
        return villeDao.totalJuryAffecteByVille(ville);
    }
    @Override
    public void demandeObseleteByVille(Long villeId) throws NumberFormatException, BusinessResourceException {
        Optional<EtatDemande> optionalObselete = service.findByLibelleLong("OBSOLETE");
        EtatDemande etatObsolete = optionalObselete.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
        Optional<EtatDemande> optionalEtat = service.findByLibelleLong("EN ATTENTE");
        EtatDemande etatEnAttente = optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
        dao.demandeObselete(villeId, etatObsolete, etatEnAttente);
    }
    @Override
    public void rejeterDemande(Long userId) throws NumberFormatException, BusinessResourceException {
        Optional<EtatDemande> optionalEtat = service.findByLibelleLong("VALIDE");
        EtatDemande valider = optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
        Optional<EtatDemande> optionalRejeter= service.findByLibelleLong("REJETE");
        EtatDemande rejeteEtat = optionalRejeter.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
        dao.rejeterDemande(userId, rejeteEtat ,valider);
    }
    @Override
    public boolean hasAcceptedDemande(String userId) {
        Long myId = Long.valueOf(userId.trim());
        AppUser entiteOptional = userDao.findById(myId)
                .orElseThrow(
                        () -> new BusinessResourceException("not-found", "User avec " + userId+ " non trouvé(e).", HttpStatus.NOT_FOUND)
                );return dao.hasAcceptedDemande(entiteOptional);
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
            Optional<EtatDemande> optionalEtat = service.findByLibelleLong("ACCEPTE");
            EtatDemande etatAccepter= optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
            oneBrute.setEtatDemande(etatAccepter);

            DemandeResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            NotificationEmail notificationEmail= new NotificationEmail();
            notificationEmail.setSubject("Mail d'acceptation");
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
            Optional<EtatDemande> optionalEtat = service.findByLibelleLong("VALIDE");
            EtatDemande etatValide= optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));

            demandeOptional.setEtatDemande(etatValide);

            DemandeResponse response = mapper.toEntiteResponse(dao.save(demandeOptional));
            rejeterDemande(demandeOptional.getUser().getId());
            int totalJuryAffecte=villeDao.totalJuryAffecteByVille(demandeOptional.getVille());
            int totalJury=demandeOptional.getVille().getTotalJury();
                int quota = totalJury - totalJuryAffecte ;
                log.info("Calcule du quota: {}", quota);
                if (quota == 0) {
                    demandeObseleteByVille(demandeOptional.getVille().getId());
                }

            NotificationEmail notificationEmail= new NotificationEmail();
            notificationEmail.setSubject("Mail d'acceptation");
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
    @Override
    public boolean quotaAccepteByVille(String villeId) {
        try {
            Long myId = Long.valueOf(villeId.trim());
            Ville ville = villeDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Ville avec " + villeId+ " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Ville avec id: " + villeId + " trouvé. <auditOneById>");
            int accepte= villeDao.totalDemandeAccepteByVille(ville);
            int totalJury= ville.getTotalJury();
            log.info("totalJury: " + totalJury + " trouvé. <auditOneById>");
            log.info("TotalAccepte: " + accepte + " trouvé. <auditOneById>");
            return accepte < totalJury;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " +villeId+ " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + villeId + " non autorisé.", HttpStatus.BAD_REQUEST);
        }

    }

}
