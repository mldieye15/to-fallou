package sn.ucad.office.pjobac.modules.demande;

import jakarta.validation.constraints.Null;
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
import sn.ucad.office.pjobac.modules.centre.Centre;
import sn.ucad.office.pjobac.modules.centre.CentreDao;
import sn.ucad.office.pjobac.modules.demande.dto.*;
import sn.ucad.office.pjobac.modules.detailsCandidat.DetailsCandidat;
import sn.ucad.office.pjobac.modules.detailsCandidat.DetailsCandidatDao;
import sn.ucad.office.pjobac.modules.detailsCandidat.DetailsCandidatService;
import sn.ucad.office.pjobac.modules.detailsCandidat.OrdreArriveService;
import sn.ucad.office.pjobac.modules.detailsCandidat.dto.DetailsCandidatRequest;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemande;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemandeServiceImp;
import sn.ucad.office.pjobac.modules.security.mail.MailService;
import sn.ucad.office.pjobac.modules.security.mail.NotificationEmailHtml;
import sn.ucad.office.pjobac.modules.security.token.AuthService;
import sn.ucad.office.pjobac.modules.security.user.AppUser;
import sn.ucad.office.pjobac.modules.security.user.UserDao;
import sn.ucad.office.pjobac.modules.session.Session;
import sn.ucad.office.pjobac.modules.session.SessionDao;
import sn.ucad.office.pjobac.modules.typeSession.dto.VilleResponse;
import sn.ucad.office.pjobac.modules.ville.Ville;
import sn.ucad.office.pjobac.modules.ville.VilleDao;
import sn.ucad.office.pjobac.utils.EncryptionUtils;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DemandeServiceImp implements DemandeService {
    private final DemandeMapper mapper;
    private final DemandeDao dao;
    private  final CentreDao centreDao;
    private final EtatDemandeServiceImp service;
    private  final MailService mailService;
    private final SessionDao sessionDao;
    private final VilleDao villeDao;
    private final AuthService authService;
    private final UserDao userDao;
    private final DetailsCandidatService candidatService;
    private final DetailsCandidatDao candidatDao;
    private final  NotificationObseleteDemande notificationObselete;
    private final OrdreArriveService ordreArrive;

//    @Override
//    public List<DemandeResponse> all() throws BusinessResourceException {
//        log.info("DemandeServiceImp::all");
//        List<Demande> all = dao.findAll();
//        List<DemandeResponse> response;
//        response = all.stream()
//                .map(mapper::toEntiteResponse)
//                .collect(Collectors.toList());
//        return response;
//    }

//    @Override
//    public List<DemandeDetailsCandidatResponse> allWithAffectable() throws BusinessResourceException {
//        log.info("DemandeServiceImp::all");
//        List<DemandeDetailsCandidat> all = dao.demandesWithAffectable();
//        return all.stream()
//                .map(mapper::mapToResponse)
//                .collect(Collectors.toList());
//    }
@Override
public Map<Long, List<DemandeDetailsCandidatResponse>> all() throws BusinessResourceException {
    log.info("DemandeServiceImp::allGroupedByUser");
    List<Demande> all = dao.findAll();
    Map<Long, List<DemandeDetailsCandidatResponse>> response;
    response = all.stream()
            .collect(Collectors.groupingBy(demande -> demande.getUser().getId(),
                    Collectors.mapping( demande -> {
                        DetailsCandidat detailsCandidat= candidatDao.detailsForUser(demande.getUser());
                        return  mapper.mapToResponse(demande,detailsCandidat);
                    }, Collectors.toList())));

    return response;
}

    @Override
    public List<DemandeResponse> allValider() throws BusinessResourceException {
        log.info("DemandeServiceImp::allValider");
        List<Demande> all = dao.allDemandeValider();
        List<DemandeResponse> response;
        response = all.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public Map<Long, List<DemandeDetailsCandidatResponse>> allGroupedByUser() throws BusinessResourceException {
        log.info("DemandeServiceImp::allGroupedByUser");
        List<Demande> all = dao.demandeBySession();
        Map<Long, List<DemandeDetailsCandidatResponse>> response;
        response = all.stream()
                .collect(Collectors.groupingBy(demande -> demande.getUser().getId(),
                        Collectors.mapping( demande -> {
                                    DetailsCandidat detailsCandidat= candidatDao.detailsForUserAndSession(demande.getUser());
                                    return  mapper.mapToResponse(demande,detailsCandidat);
                                }, Collectors.toList())));

        return response;
    }
    @Override
    public List<DemandeDetailsCandidatResponse> demandeByVille(String villeId) throws BusinessResourceException {
        log.info("DemandeServiceImp::demandeByVille");
        Long myId = Long.valueOf(villeId.trim());
        Ville ville = villeDao.findById(myId)
                .orElseThrow(
                        () -> new BusinessResourceException("not-found", "Aucun Ville avec " + villeId + " trouvé.", HttpStatus.NOT_FOUND)
                );
        // Récupérer toutes les demandes par ville
        List<Demande> all = dao.demandeByVille(ville);
        List<DemandeDetailsCandidatResponse> response;
        response = all.stream()
                .map(demande -> {
                    DetailsCandidat detailsCandidat = candidatDao.detailsForUserAndSession(demande.getUser());
                    DemandeDetailsCandidatResponse demandeResponse;
                    demandeResponse = mapper.mapToResponse(demande, detailsCandidat);
                    return demandeResponse;
                })
                .sorted(Comparator.comparing(DemandeDetailsCandidatResponse::getNote).reversed())
                .collect(Collectors.toList());
        for (int i = 0; i < response.size(); i++) {
            response.get(i).setOrdreArrivee(i + 1);
        }
        return response;
    }
    @Override
    public List<DemandeDetailsCandidatResponse> allObsolete() throws BusinessResourceException {

        List<Demande> all = dao.allDemandeObselete();
        List<DemandeDetailsCandidatResponse> response;
        response = all.stream()
                .map(demande -> {
                    DetailsCandidat detailsCandidat = candidatDao.detailsForUserAndSession(demande.getUser());
                    DemandeDetailsCandidatResponse demandeResponse;
                    demandeResponse = mapper.mapToResponse(demande, detailsCandidat);
                    return demandeResponse;
                })
                .sorted(Comparator.comparing(DemandeDetailsCandidatResponse::getOrdreArrivee))
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public Map<Long, List<DemandeResponse>> allGroupedByUserAndSession(String sessionId) throws BusinessResourceException {
        Long myId = Long.valueOf(sessionId.trim());
        Session session = sessionDao.findById(myId)
                .orElseThrow(
                        () -> new BusinessResourceException("not-found", "Aucun Session avec " + sessionId + " trouvé.", HttpStatus.NOT_FOUND)
                );
        // Récupérer toutes les demandes par ville
                log.info("DemandeServiceImp::allGroupedByUser");
               List<Demande> all = dao.demandeBySession(session);
                Map<Long, List<DemandeResponse>> response;
                 response = all.stream()
                .filter(demande -> demande.getUser() != null) // Filtrer les demandes où l'utilisateur n'est pas null
                .collect(Collectors.groupingBy(
                        demande -> demande.getUser().getId(),
                        Collectors.mapping(mapper::toEntiteResponse, Collectors.toList())
                ));
        return response;
            }

    @Override
    public List<DemandeDetailsCandidatResponse> demandeByCentre(String centreId) throws BusinessResourceException {
        log.info("DemandeServiceImp::demandeByCentre");
        Long myId = Long.valueOf(centreId.trim());
        Centre centre = centreDao.findById(myId)
                .orElseThrow(
                        () -> new BusinessResourceException("not-found", "Aucun Centre avec " + centreId + " trouvé.", HttpStatus.NOT_FOUND)
                );
        // Récupérer toutes les demandes par centre
        List<Demande> all = dao.demandeByCentre(centre);
        List<DemandeDetailsCandidatResponse> response;
        response = all.stream()
                .map(demande -> {
                    DetailsCandidat detailsCandidat = candidatDao.detailsForUserAndSession(demande.getUser());
                    DemandeDetailsCandidatResponse demandeResponse;
                    demandeResponse = mapper.mapToResponse(demande, detailsCandidat);
                    return demandeResponse;
                })
                .sorted(Comparator.comparing(DemandeDetailsCandidatResponse::getNote).reversed())
                .collect(Collectors.toList());
        for (int i = 0; i < response.size(); i++) {
            response.get(i).setOrdreArrivee(i + 1);
        }
        return response;
    }
    @Override
    public Map<Long, List<DemandeDetailsCandidatResponse>> demandeBySession(String sessionId) throws BusinessResourceException {
        log.info("DemandeServiceImp::demandeBySession");
        Long myId = Long.valueOf(sessionId.trim());
        Session session = sessionDao.findById(myId)
                .orElseThrow(
                        () -> new BusinessResourceException("not-found", "Aucun Session avec " + sessionId + " trouvé.", HttpStatus.NOT_FOUND)
                );
        // Récupérer toutes les demandes par ville
        List<Demande> all = dao.demandeBySession(session);
        Map<Long, List<DemandeDetailsCandidatResponse>> response;
        response = all.stream()
                .collect(Collectors.groupingBy(demande -> demande.getUser().getId(),
                        Collectors.mapping( demande -> {
                            DetailsCandidat detailsCandidat= candidatDao.detailsForUserAndSession(demande.getUser());
                            return  mapper.mapToResponse(demande,detailsCandidat);
                        }, Collectors.toList())));
        return response;
    }
//    @Override
//    public List<DemandeResponse> allForUser() throws BusinessResourceException {
//        try {
//            AppUser currentUser = authService.getCurrentUser();
//            List<Demande> demandes = dao.findByUser(currentUser);
//            if (demandes.isEmpty()) {
//                log.warn("Aucune demande trouvée pour l'utilisateur avec l'ID : {}", currentUser.getId());
//                throw new BusinessResourceException("not-found", "Aucune demande trouvée pour l'utilisateur.", HttpStatus.NOT_FOUND);
//            }
//            List<DemandeResponse> response;
//            response = demandes.stream()
//                    .map(mapper::toEntiteResponse)
//                    .collect(Collectors.toList());
//            return response;
//        } catch (Exception ex) {
//            log.error("Erreur lors de la récupération des demandes pour l'utilisateur : {}", ex.getMessage());
//            throw new BusinessResourceException("technical-error", "Erreur technique lors de la récupération des demandes.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
@Override
public List<DemandeResponse> allForUser() throws BusinessResourceException {
    try {
        AppUser currentUser = authService.getCurrentUser();
        List<Demande> demandes = dao.findByUser(currentUser);
        List<DemandeResponse> response = demandes.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        if (response.isEmpty()) {
            log.warn("Aucune demande trouvée pour l'utilisateur avec l'ID : {}", currentUser.getId());
        }
        return response;
    } catch (Exception ex) {
        log.error("Erreur lors de la récupération des demandes pour l'utilisateur : {}", ex.getMessage());
        throw new BusinessResourceException("technical-error", "Erreur technique lors de la récupération des demandes.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
    @Override
    public List<DemandeResponse> recaptDemandes() throws BusinessResourceException {
        List<Demande> demandes = dao.allDemandeValider();
        List<DemandeResponse> response;
        response = demandes.stream()
                .map(this::toDemandeResponseWithDecryption)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public List<DemandeResponse> allDemandeProposer() throws BusinessResourceException {
        List<Demande> demandes = dao.allDemandeProposer();
        List<DemandeResponse> response;
        response = demandes.stream()
                .map(this::toDemandeResponseWithDecryption)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public List<DemandeResponse> allDemandeAccepter() throws BusinessResourceException {
        List<Demande> demandes = dao.demandeAccepter();
        List<DemandeResponse> response;
        response = demandes.stream()
                .map(this::toDemandeResponseWithDecryption)
                .collect(Collectors.toList());
        return response;
    }

    private DemandeResponse toDemandeResponseWithDecryption(Demande demande) {
        DemandeResponse response = mapper.toEntiteResponse(demande);
        AppUser user = demande.getUser();

        try {
            String codeBanqueDecrypte = EncryptionUtils.decrypt(user.getCodeBanque());
            String codeAgenceDecrypte = EncryptionUtils.decrypt(user.getCodeAgence());
            String numeroCompteDecrypte = EncryptionUtils.decrypt(user.getNumeroCompte());
            String cleRibDecrypte = EncryptionUtils.decrypt(user.getCleRib());

            response.getUser().setCodeBanque(codeBanqueDecrypte);
            response.getUser().setCodeAgence(codeAgenceDecrypte);
            response.getUser().setNumeroCompte(numeroCompteDecrypte);
            response.getUser().setCleRib(cleRibDecrypte);
        } catch (Exception e) {
            // Gérer les exceptions de déchiffrement ici
            e.printStackTrace();
            // Vous pouvez définir des valeurs par défaut ou laisser les champs non définis
        }

        return response;
    }

    @Override
    public List<DemandeResponse> demandeObseleteByVille(String villeId) throws BusinessResourceException {
        Long myId= Long.valueOf(villeId.trim());
        Ville ville;
        ville=villeDao.findById(myId)
                .orElseThrow(()->new RuntimeException("Ville non trouvée pour l'ID : " + villeId));
        List<Demande> demandes=dao.demandeObseleteByVille(ville);
        List<DemandeResponse> response;
        response= demandes.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
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
    @Transactional(readOnly = false)
    @Override
    public List<DemandeResponse> addAll(List<DemandeRequest> req) throws BusinessResourceException {
        try {
            String currentUserId = authService.getCurrentUser().getId().toString();
            if (candidatService.userHasAlreadyApplied(currentUserId)) {
                throw new BusinessResourceException("already-applied", "L'utilisateur a déjà candidaté.", HttpStatus.BAD_REQUEST);
            } else {
                // Mapper les demandes
                List<Demande> demandes = req.stream()
                        .map(request -> {
                            Demande demande = mapper.requestToEntity(request);
                            AppUser currentUser = authService.getCurrentUser();
                            demande.setUser(currentUser);
                            Optional<EtatDemande> optionalEtat = service.findByLibelleLong("en attente");
                            EtatDemande etatParDefaut = optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
                            demande.setEtatDemande(etatParDefaut);
                            return demande;
                        })
                        .collect(Collectors.toList());

                // Sauvegarder les demandes
                List<DemandeResponse> responses = dao.saveAll(demandes).stream()
                        .map(mapper::toEntiteResponse)
                        .collect(Collectors.toList());
                // Ajouter les détails du candidat seulement après le succès de la sauvegarde des demandes
                if (!responses.isEmpty()) {
                    DetailsCandidatRequest detailsRequest = new DetailsCandidatRequest();
                    candidatService.add(detailsRequest);
                    log.info("Ajout des demandes effectué avec succès. <addAll>");
                }
                return responses;
            }
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
            Optional<EtatDemande> optionalEtat = service.findByLibelleLong("en attente");
            EtatDemande etatParDefaut = optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
                oneBrute.setEtatDemande(etatParDefaut);

            DemandeResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
//            ordreArrive.updateOrderByVille();
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
    public DemandeResponse maj2(DemandeRequestUpdate req, String demandeId) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(demandeId.trim());
            Demande demandeOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Demande avec " + demandeId + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            Demande oneBrute = mapper.updateToEntiteUp(demandeOptional, req);
            DemandeResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
//            ordreArrive.updateOrderByVille();
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
    public void accepterDemande(List<Long> demandeIds) throws BusinessResourceException {
        try {
            if (demandeIds == null || demandeIds.isEmpty()) {
                throw new BusinessResourceException("not-valid-param",
                        "Aucun ID de demande fourni.", HttpStatus.BAD_REQUEST);
            }

            // Récupérer les demandes existantes en une seule requête
            List<Demande> demandes = dao.findAllById(demandeIds);

            if (demandes.isEmpty()) {
                throw new BusinessResourceException("not-found",
                        "Aucune demande trouvée pour les IDs fournis.", HttpStatus.NOT_FOUND);
            }

            // Récupérer l'état "acceptée"
            EtatDemande etatAccepter = service.findByLibelleLong("acceptée")
                    .orElseThrow(() -> new BusinessResourceException("not-found",
                            "Aucun état avec le libellé 'acceptée' trouvé.", HttpStatus.NOT_FOUND));

            // Mise à jour des demandes
            for (Demande demande : demandes) {
                demande.setEtatDemande(etatAccepter);

                // Définir la date de rejet
                LocalDateTime dateRejet = LocalDateTime.now(ZoneOffset.UTC)
                        .plusHours(demande.getSession().getDelaisValidation());
                demande.setDateRejetDemande(dateRejet);
            }

            // Sauvegarde en masse en une seule transaction
            dao.saveAll(demandes);

            // Envoi des emails après la sauvegarde des données
            for (Demande demande : demandes) {
                try {
                    sendEmailToDemande(demande);
                    Thread.sleep(1500); // Pause de 500 ms entre chaque email
                } catch (Exception e) {
                    log.warn("Échec de l'envoi d'email à l'utilisateur {} : {}", demande.getUser().getEmail(), e.getMessage());
                }
            }
            log.info(demandes.size() + " demandes acceptées avec succès.");

        } catch (BusinessResourceException e) {
            log.warn("Erreur lors de l'acceptation des demandes : " + e.getMessage());
            throw e;
        } catch (Exception ex) {
            log.error("Erreur technique lors de l'acceptation des demandes.", ex);
            throw new BusinessResourceException("technical-error",
                    "Erreur technique lors de l'acceptation des demandes.", HttpStatus.INTERNAL_SERVER_ERROR);
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
        Optional<EtatDemande> optionalObselete = service.findByLibelleLong("obsolète");
        EtatDemande etatObsolete = optionalObselete.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
        Optional<EtatDemande> optionalEtat = service.findByLibelleLong("en attente");
        EtatDemande etatEnAttente = optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
        dao.demandeObselete(villeId, etatObsolete, etatEnAttente);
    }
    @Override
    public void rejeterDemande(Long userId) throws NumberFormatException, BusinessResourceException {
        Optional<EtatDemande> optionalEtat = service.findByLibelleLong("validée");
        EtatDemande valider = optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
        Optional<EtatDemande> optionalRejeter= service.findByLibelleLong("rejetée");
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
    public boolean hasPropositionDemande(String userId) {
        Long myId = Long.valueOf(userId.trim());
        AppUser entiteOptional = userDao.findById(myId)
                .orElseThrow(
                        () -> new BusinessResourceException("not-found", "User avec " + userId+ " non trouvé(e).", HttpStatus.NOT_FOUND)
                );return dao.hasPropositionDemande(entiteOptional);
    }

    //    @Override
//    @Transactional(readOnly = false)
//    public DemandeResponse accepterDemande(DemandeAccepter req, String demandeId) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
//        try {
//            Long myId = Long.valueOf(demandeId.trim());
//            Demande demandeOptional = dao.findById(myId)
//                    .orElseThrow(
//                            () -> new BusinessResourceException("not-found", "Aucun Demande avec " + demandeId + " trouvé.", HttpStatus.NOT_FOUND)
//                    );
//            Demande oneBrute = mapper.accepterToEntiteUp(demandeOptional, req);
//            Optional<EtatDemande> optionalEtat = service.findByLibelleLong("acceptée");
//            EtatDemande etatAccepter= optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
//            oneBrute.setEtatDemande(etatAccepter);
//            int delaisValidation;
//            delaisValidation = oneBrute.getSession().getDelaisValidation();
//            LocalDateTime dateRejet = LocalDateTime.now(ZoneOffset.UTC).plusHours(delaisValidation);
//            oneBrute.setDateRejetDemande(dateRejet);
//            DemandeResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
//            NotificationEmailHtml notificationEmail = new NotificationEmailHtml();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy HH:mm:ss", Locale.FRANCE);
//            ZonedDateTime zonedDateTime = oneBrute.getDateRejetDemande().atZone(ZoneOffset.UTC);
//            String formattedDateTime = zonedDateTime.format(formatter) + " UTC";
//            notificationEmail.setSubject("Demande à valider");
//            notificationEmail.setRecipient(oneBrute.getUser().getEmail());
//            notificationEmail.setTemplateName("notificationAccepter.html"); // Ajoutez le nom du modèle Thymeleaf
//            Map<String, Object> emailVariables = new HashMap<>();
//            emailVariables.put("prenoms", oneBrute.getUser().getPrenoms());
//            emailVariables.put("nom", oneBrute.getUser().getNom());
//            emailVariables.put("dateRejetDemande", formattedDateTime);
//            emailVariables.put("academie", oneBrute.getVille().getAcademie().getLibelleLong());
//            emailVariables.put("ville", oneBrute.getVille().getLibelleLong());
//            emailVariables.put("centre", oneBrute.getCentre().getLibelleLong());
//            notificationEmail.setEmailVariables(emailVariables);
//            mailService.sendHtmlEmail(notificationEmail);
//            log.info("Demande" + response.getId() + " accepetée avec succés. <accepterDemande>");
//            return response;
//        } catch (NumberFormatException e) {
//            log.warn("Paramétre id " + demandeId+ " non autorisé. <accepterDemande>.");
//            throw new BusinessResourceException("not-valid-param", "Paramétre " + demandeId+ " non autorisé.", HttpStatus.BAD_REQUEST);
//        } catch (Exception ex) {
//            log.error("Une erreur inattandue est rencontrée." + ex.toString());
//            throw new BusinessResourceException("technical-error", "Erreur technique d'acceptation d'un Demande: " + req.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
    private void sendEmailToDemande(Demande demande) throws BusinessResourceException {
        try {
            // Création du contenu de l'email
            NotificationEmailHtml notificationEmail = new NotificationEmailHtml();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy HH:mm:ss", Locale.FRANCE);
            ZonedDateTime zonedDateTime = demande.getDateRejetDemande().atZone(ZoneOffset.UTC);
            String formattedDateTime = zonedDateTime.format(formatter) + " UTC";

            notificationEmail.setSubject("Demande à valider");
            notificationEmail.setRecipient(demande.getUser().getEmail());
            notificationEmail.setTemplateName("notificationAccepter.html");

            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("prenoms", demande.getUser().getPrenoms());
            emailVariables.put("nom", demande.getUser().getNom());
            emailVariables.put("dateRejetDemande", formattedDateTime);
            emailVariables.put("academie", demande.getVille().getAcademie().getLibelleLong());
            emailVariables.put("ville", demande.getVille().getLibelleLong());
            emailVariables.put("centre", demande.getCentre().getLibelleLong());

            notificationEmail.setEmailVariables(emailVariables);

            // Envoi de l'email
            mailService.sendHtmlEmail(notificationEmail);
        } catch (Exception e) {
            throw new BusinessResourceException("email-error",
                    "Erreur lors de l'envoi de l'email pour la demande : " + demande.getId(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public DemandeResponse proposition(DemandeAccepter req, String demandeId) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(demandeId.trim());
            Demande demandeOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Demande avec " + demandeId + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            Demande oneBrute = mapper.accepterToEntiteUp(demandeOptional, req);
//            Optional<EtatDemande> optionalEtat = service.findByLibelleLong("acceptée");
//            EtatDemande etatAccepter= optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
//            oneBrute.setEtatDemande(etatAccepter);
//            int delaisValidation;
//            delaisValidation = oneBrute.getSession().getDelaisValidation();
//            LocalDateTime dateRejet = LocalDateTime.now(ZoneOffset.UTC).plusHours(delaisValidation);
//            oneBrute.setDateRejetDemande(dateRejet);
            oneBrute.setProposition(req.getCentre() != null);
            DemandeResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
//            NotificationEmailHtml notificationEmail = new NotificationEmailHtml();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy HH:mm:ss", Locale.FRANCE);
//            ZonedDateTime zonedDateTime = oneBrute.getDateRejetDemande().atZone(ZoneOffset.UTC);
//            String formattedDateTime = zonedDateTime.format(formatter) + " UTC";
//            notificationEmail.setSubject("Demande à valider");
//            notificationEmail.setRecipient(oneBrute.getUser().getEmail());
//            notificationEmail.setTemplateName("notificationAccepter.html"); // Ajoutez le nom du modèle Thymeleaf
//            Map<String, Object> emailVariables = new HashMap<>();
//            emailVariables.put("prenoms", oneBrute.getUser().getPrenoms());
//            emailVariables.put("nom", oneBrute.getUser().getNom());
//            emailVariables.put("dateRejetDemande", formattedDateTime);
//            emailVariables.put("academie", oneBrute.getVille().getAcademie().getLibelleLong());
//            emailVariables.put("ville", oneBrute.getVille().getLibelleLong());
//            emailVariables.put("centre", oneBrute.getCentre().getLibelleLong());
//            notificationEmail.setEmailVariables(emailVariables);
//            mailService.sendHtmlEmail(notificationEmail);
            log.info("Demande" + response.getId() + " accepetée avec succés. <accepterDemande>");
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
    public void annulerDemande(String demandeId) throws BusinessResourceException {
        try {
            Long myId = Long.valueOf(demandeId.trim());
            Demande demandeOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Demande avec " + demandeId + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            Optional<EtatDemande> optionalEtat = service.findByLibelleLong("rejetée");
            EtatDemande etatRejeter = optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune état avec le libellé REJETÉE trouvée.", HttpStatus.NOT_FOUND));
            Optional<EtatDemande> optionalEnAttente = service.findByLibelleLong("en attente");
            EtatDemande enAttente = optionalEnAttente.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune état avec le libellé REJETÉE trouvée.", HttpStatus.NOT_FOUND));
            Optional<EtatDemande> optionalObsolete = service.findByLibelleLong("obsolète");
            EtatDemande obsolete = optionalObsolete.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune état avec le libellé REJETÉE trouvée.", HttpStatus.NOT_FOUND));
            dao.annulerDemande(etatRejeter, myId);
            dao.pendingDemande(demandeOptional.getVille().getId(),enAttente,obsolete);
            log.info("Demande avec l'ID " + demandeId + " rejetée avec succès.");
        } catch (NumberFormatException e) {
            log.warn("Paramètre id " + demandeId + " non autorisé. <nonAffectableDemande>.");
            throw new BusinessResourceException("not-valid-param", "Paramètre " + demandeId + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (BusinessResourceException ex) {
            // Si une exception de type BusinessResourceException est levée, la re-levée sans la capturer
            throw ex;
        } catch (Exception ex) {
            log.error("Une erreur inattendue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique lors du rejet de la demande avec l'ID " + demandeId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public void nonAffectableDemande(String demandeId) throws BusinessResourceException {
        try {
            Long myId = Long.valueOf(demandeId.trim());
            Demande demandeOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Demande avec " + demandeId + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            Optional<EtatDemande> optionalEtat = service.findByLibelleLong("rejetée");
            EtatDemande etatRejeter = optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune état avec le libellé REJETÉE trouvée.", HttpStatus.NOT_FOUND));
            dao.nonAffectable(etatRejeter, myId);
            rejeterDemande(demandeOptional.getUser().getId());
            NotificationEmailHtml notificationEmail = new NotificationEmailHtml();
            notificationEmail.setSubject("Non affectable");
            notificationEmail.setRecipient(demandeOptional.getUser().getEmail());
            notificationEmail.setTemplateName("notificationRejeter.html"); // Ajoutez le nom du modèle Thymeleaf
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("prenoms", demandeOptional.getUser().getPrenoms());
            emailVariables.put("nom", demandeOptional.getUser().getNom());
            emailVariables.put("annee", demandeOptional.getSession().getAnnee().getLibelleLong());
            notificationEmail.setEmailVariables(emailVariables);
            mailService.sendHtmlEmail(notificationEmail);
            log.info("Demande avec l'ID " + demandeId + " rejetée avec succès.");
        } catch (NumberFormatException e) {
            log.warn("Paramètre id " + demandeId + " non autorisé. <nonAffectableDemande>.");
            throw new BusinessResourceException("not-valid-param", "Paramètre " + demandeId + " non autorisé.", HttpStatus.BAD_REQUEST);
        } catch (BusinessResourceException ex) {
            // Si une exception de type BusinessResourceException est levée, la re-levée sans la capturer
            throw ex;
        } catch (Exception ex) {
            log.error("Une erreur inattendue est rencontrée." + ex.toString());
            throw new BusinessResourceException("technical-error", "Erreur technique lors du rejet de la demande avec l'ID " + demandeId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public DemandeResponse affecterJury(DemandeAffecterJury req, String demandeId) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(demandeId.trim());
            Demande demandeOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Demande avec " + demandeId + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            Demande oneBrute = mapper.affecterJuryToEntiteUp(demandeOptional, req);
            DemandeResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
            log.info("Demande " + response.getId() + " affecter avec succés. <accepterDemande>");
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
            Optional<EtatDemande> optionalEtat = service.findByLibelleLong("validée");
            EtatDemande etatValide= optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
            LocalDateTime dateValidation = LocalDateTime.now(ZoneOffset.UTC);

            demandeOptional.setEtatDemande(etatValide);
            demandeOptional.setDateConfirmationDemande(dateValidation);
            DemandeResponse response = mapper.toEntiteResponse(dao.save(demandeOptional));
            rejeterDemande(demandeOptional.getUser().getId());
            int totalJuryAffecte=villeDao.totalJuryAffecteByVille(demandeOptional.getVille());
            int totalJury=demandeOptional.getVille().getTotalJury();
                int quota = totalJury - totalJuryAffecte ;
                log.info("Calcule du quota: {}", quota);
                if (quota == 0) {
                    demandeObseleteByVille(demandeOptional.getVille().getId());
                    notificationObselete.notification(demandeOptional.getVille().getId().toString());
                }

            NotificationEmailHtml notificationEmail = new NotificationEmailHtml();
            notificationEmail.setSubject("Mail de validation");
            notificationEmail.setRecipient(demandeOptional.getUser().getEmail());
            notificationEmail.setTemplateName("notificationValider.html"); // Ajoutez le nom du modèle Thymeleaf
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("prenoms", demandeOptional.getUser().getPrenoms());
            emailVariables.put("nom", demandeOptional.getUser().getNom());
            emailVariables.put("session", demandeOptional.getSession().getLibelleLong());
            emailVariables.put("academie", demandeOptional.getVille().getAcademie().getLibelleLong());
            emailVariables.put("ville", demandeOptional.getVille().getLibelleLong());
            emailVariables.put("centre", demandeOptional.getCentre().getLibelleLong());
            notificationEmail.setEmailVariables(emailVariables);
            mailService.sendHtmlEmail(notificationEmail);
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
    @Transactional(readOnly = false)
    public DemandeResponse validerDemandeSecondary(String demandeId) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            log.info("planification secondaire");
            Long myId = Long.valueOf(demandeId.trim());
            Demande demandeOptional = dao.findById(myId)
                    .orElseThrow(() -> new BusinessResourceException("not-found", "Aucun Demande avec l'ID " + demandeId + " trouvé.", HttpStatus.NOT_FOUND));
            Optional<EtatDemande> optionalEtat = service.findByLibelleLong("validée");
            EtatDemande etatValide= optionalEtat.orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
            LocalDateTime dateValidation = LocalDateTime.now(ZoneOffset.UTC);

            demandeOptional.setEtatDemande(etatValide);
            demandeOptional.setDateConfirmationDemande(dateValidation);
            DemandeResponse response = mapper.toEntiteResponse(dao.save(demandeOptional));
            rejeterDemande(demandeOptional.getUser().getId());
            int totalJuryAffecte=villeDao.totalJuryAffecteByVilleSecondary(demandeOptional.getVille());
            final int totalJury=1;
            int quota = totalJury - totalJuryAffecte ;
            log.info("Calcule du quota secondary: {}", quota);
            if (quota == 0) {
                demandeObseleteByVille(demandeOptional.getVille().getId());
                notificationObselete.notification(demandeOptional.getVille().getId().toString());
            }
            NotificationEmailHtml notificationEmail = new NotificationEmailHtml();
            notificationEmail.setSubject("Mail de validation");
            notificationEmail.setRecipient(demandeOptional.getUser().getEmail());
            notificationEmail.setTemplateName("notificationValider.html"); // Ajoutez le nom du modèle Thymeleaf
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("prenoms", demandeOptional.getUser().getPrenoms());
            emailVariables.put("nom", demandeOptional.getUser().getNom());
            emailVariables.put("session", demandeOptional.getSession().getLibelleLong());
            emailVariables.put("academie", demandeOptional.getVille().getAcademie().getLibelleLong());
            emailVariables.put("ville", demandeOptional.getVille().getLibelleLong());
            emailVariables.put("centre", demandeOptional.getCentre().getLibelleLong());
            notificationEmail.setEmailVariables(emailVariables);
            mailService.sendHtmlEmail(notificationEmail);
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
            int accepte= villeDao.totalDemandeAccepteOrValideByVille(ville);
            int totalJury= ville.getTotalJury();
            log.info("totalJury: " + totalJury + " trouvé. <auditOneById>");
            log.info("TotalAccepte: " + accepte + " trouvé. <auditOneById>");
            return accepte < totalJury;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " +villeId+ " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + villeId + " non autorisé.", HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public boolean quotaPropositionByVille(String villeId) {
        try {
            Long myId = Long.valueOf(villeId.trim());
            Ville ville = villeDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Ville avec " + villeId+ " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Ville avec id: " + villeId + " trouvé. <auditOneById>");
            int accepte= villeDao.totalDemandePropositionByVille(ville);
            int totalJury= ville.getTotalJury();
            log.info("totalJury: " + totalJury + " trouvé. <auditOneById>");
            log.info("TotalAccepte: " + accepte + " trouvé. <auditOneById>");
            return accepte < totalJury;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " +villeId+ " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + villeId + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public boolean quotaAccepteByVilleSecondary(String villeId) {
        try {
            log.info("quota ville administratif");
            Long myId = Long.valueOf(villeId.trim());
            Ville ville = villeDao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Ville avec " + villeId+ " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Ville avec id: " + villeId + " trouvé. <quota ville>");
            int accepte= villeDao.totalDemandeAccepteOrValideByVilleSecondary(ville);
            final int totalJury = 1;
            log.info("totalJury: " + totalJury + " trouvé. <quota ville>");
            log.info("TotalAccepte: " + accepte + " trouvé. <quota ville>");
            return accepte < totalJury;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " +villeId+ " non autorisé. <quota ville>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + villeId + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

//    @Override
//    public void updateOrderByVille() throws BusinessResourceException {
//        log.info("DemandeServiceImp::updateOrderByVille");
//        // Récupérer toutes les demandes avec les détails de chaque candidat
//        List<Demande> all = dao.findAll();
//        // Regrouper les demandes par ville
//        Map<Long, List<DemandeDetailsCandidatResponse>> groupedByVille = all.stream()
//                .collect(Collectors.groupingBy(demande -> demande.getVille().getId(),
//                        Collectors.mapping(demande -> mapper.mapToResponse(demande,
//                                candidatDao.detailsForUser(demande.getUser())), Collectors.toList())));
//        // Pour chaque ville, trier les candidats par note et mettre à jour l'ordre d'arrivée
//        groupedByVille.forEach((city, candidates) -> {
//            candidates.sort(Comparator.comparingInt(DemandeDetailsCandidatResponse::getNote).reversed());
//            // Mettre à jour l'ordre d'arrivée
//            int order = 1;
//            for (DemandeDetailsCandidatResponse candidate : candidates) {
//                Demande demande = dao.findById(candidate.getDemandeId()).orElse(null);
//                if (demande != null) {
//                    demande.setOrdreArrivee(order++);
//                    dao.save(demande);
//                }
//            }
//        });
//
//    }

}
