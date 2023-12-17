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
import sn.ucad.office.pjobac.modules.demande.dto.DemandeAccepter;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeAudit;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeRequest;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeResponse;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemande;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemandeDao;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemandeServiceImp;
import sn.ucad.office.pjobac.modules.security.mail.MailService;
import sn.ucad.office.pjobac.modules.security.mail.NotificationEmail;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
    public List<DemandeResponse> allUsers() throws BusinessResourceException {
        return null;
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

}
