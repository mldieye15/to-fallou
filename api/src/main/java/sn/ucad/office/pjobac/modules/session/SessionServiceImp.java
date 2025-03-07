package sn.ucad.office.pjobac.modules.session;

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

import sn.ucad.office.pjobac.modules.demande.NotificationObseleteDemande;
import sn.ucad.office.pjobac.modules.demande.dto.DemandeDetailsCandidatResponse;
import sn.ucad.office.pjobac.modules.session.dto.SessionAudit;
import sn.ucad.office.pjobac.modules.session.dto.SessionRequest;
import sn.ucad.office.pjobac.modules.session.dto.SessionResponse;
import sn.ucad.office.pjobac.utils.SimplePage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionServiceImp implements SessionService {
    private final SessionMapper mapper;
    private final SessionDao dao;
    private final NotificationObseleteDemande obseleteDemande;

    @Override
    public List<SessionResponse> all() throws BusinessResourceException {
        log.info("SessionServiceImp::all");
        List<Session> all = dao.findAll();
        all.sort(Comparator.comparing(Session::getId, Comparator.reverseOrder()));
        List<SessionResponse> response;
        response = all.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
    }
    @Override
    public List<SessionResponse> sessionsArchive() throws BusinessResourceException {
        log.info("SessionServiceImp::all");
        List<Session> all = dao.sessionsArchive();
        all.sort(Comparator.comparing(Session::getId, Comparator.reverseOrder()));
        List<SessionResponse> response;
        response = all.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public List<SessionResponse> allActiveSessions() throws BusinessResourceException {
        log.info("SessionServiceImp::all");
        List<Session> allSessions = dao.findAll();
        List<Session> activeSessions;
        activeSessions = allSessions.stream()
                .filter(session ->
                                LocalDateTime.now().isAfter(session.getDateOuvertureDepotCandidature().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                                && LocalDateTime.now().isBefore(session.getDateClotureDepotCandidature().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()))
                .collect(Collectors.toList());

        // on mappe les sessions actives en SessionResponse
        List<SessionResponse> response;
        response = activeSessions.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public SimplePage<SessionResponse> all(Pageable pageable) throws BusinessResourceException {
        log.info("Liste des Sessions avec pagination. <all>");
        final Page<Session> page = dao.findAll(pageable);
        return new SimplePage<>(page.getContent()
                .stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList()),
                page.getTotalElements(), pageable
        );
    }

    @Override
    public Optional<SessionResponse> oneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Session one = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Session avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Session avec id: " + id + " trouvé. <oneById>");
            Optional<SessionResponse> response;
            response = Optional.ofNullable(mapper.toEntiteResponse(one));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <oneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public SessionResponse add(SessionRequest req) throws BusinessResourceException {
        try {
            log.info("Debug 001-add:  " + req.toString());
            Session one = mapper.requestToEntity(req);
            log.info("Debug 001-req_to_entity:  " + one.toString());
            SessionResponse response = mapper.toEntiteResponse(dao.save(one));
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
    public SessionResponse maj(SessionRequest req, String id) throws NumberFormatException, NoSuchElementException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Session sessionOptional = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucun Session avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            //Session annee = mapper.anneRequestToAnneeUp(sessionOptional, req, userService.user());
            Session oneBrute = mapper.requestToEntiteUp(sessionOptional, req);
            SessionResponse response = mapper.toEntiteResponse(dao.save(oneBrute));
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
            Session oneBrute = dao.findById(myId)
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
    public Optional<SessionAudit> auditOneById(String id) throws NumberFormatException, BusinessResourceException {
        try {
            Long myId = Long.valueOf(id.trim());
            Session oneBrute = dao.findById(myId)
                    .orElseThrow(
                            () -> new BusinessResourceException("not-found", "Aucune Session avec " + id + " trouvé.", HttpStatus.NOT_FOUND)
                    );
            log.info("Session avec id: " + id + " trouvé. <auditOneById>");
           Optional<SessionAudit> response;
            response = Optional.ofNullable(mapper.toEntiteAudit(oneBrute, Long.valueOf("1"), Long.valueOf("1") ));
            return response;
        } catch (NumberFormatException e) {
            log.warn("Paramétre id " + id + " non autorisé. <auditOneById>.");
            throw new BusinessResourceException("not-valid-param", "Paramétre " + id + " non autorisé.", HttpStatus.BAD_REQUEST);
        }
        //return Optional.empty();
    }

    @Override
    public void changerEtatSession(Long sessionId) {
        Session session = dao.findById(sessionId).orElse(null);
        if (session != null) {
            session.setOuvert(!session.isOuvert());
            dao.save(session);
            log.info("État de la session avec l'ID " + sessionId + " changé avec succès.");

            // Mettre à jour les autres sessions si la session est ouverte
            if (session.isOuvert()) {
                List<Session> otherSessions = dao.findAllByIdNot(sessionId);
                for (Session otherSession : otherSessions) {
                    otherSession.setOuvert(false);
                    dao.save(otherSession);
                    log.info("État de la session avec l'ID " + otherSession.getId() + " changé avec succès.");
                }
            }
        } else {
            log.warn("Session avec l'ID " + sessionId + " non trouvée.");
        }
    }
    @Override
    public void changerEtatCandidature(Long sessionId) {
        Session session = dao.findById(sessionId).orElse(null);
        if (session != null) {
            if (session.isOuvert()){
                session.setCandidature(!session.isCandidature());
                dao.save(session);
                log.info("État de la candidature de la session avec l'ID " + sessionId + " changer avec succès.");
            }
            else {
                log.warn("Impossible de changer l'état de la candidature pour la session avec l'ID " + sessionId + " car la session est fermée.");
            }
        } else {
            log.warn("Session avec l'ID " + sessionId + " non trouvée.");
        }
    }

    @Override
    @Transactional
    public void changerEtatModification(Long sessionId) throws InterruptedException {
        Session session = dao.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session avec l'ID " + sessionId + " non trouvée."));
        boolean etatPrecedent = session.isModification();
        // Inverser l'état actuel de la session
        boolean nouvelEtat = !etatPrecedent;
        session.setModification(nouvelEtat);
        dao.save(session);
        // Envoyer la notification par e-mail uniquement si l'état précédent était false et le nouvel état est true
//        if (!etatPrecedent && (obseleteDemande != null)) {
//                obseleteDemande.notificationUpdate();
//
//        }
        // Journaliser le changement d'état
        log.info("État de la candidature de la session avec l'ID " + sessionId + " changé avec succès. Nouvel état : " + nouvelEtat);
    }

    @Override
    public void changerEtatPhaseTwo(Long sessionId) throws InterruptedException {
        Session session = dao.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session avec l'ID " + sessionId + " non trouvée."));
        boolean etatPrecedent = session.isPhaseTwo();
        // Inverser l'état actuel de la session
        boolean nouvelEtat = !etatPrecedent;
        session.setPhaseTwo(nouvelEtat);
        dao.save(session);
        // Envoyer la notification par e-mail uniquement si l'état précédent était false et le nouvel état est true
//        if (!etatPrecedent && (obseleteDemande != null)) {
//            obseleteDemande.notificationPhaseTw();
//
//        }
        // Journaliser le changement d'état
        log.info("État de la candidature de la session avec l'ID " + sessionId + " changé avec succès. Nouvel état : " + nouvelEtat);

    }
    @Override
    public List<SessionResponse> findEnCoursSession()throws BusinessResourceException {
        List<Session> sessions = dao.findEnCoursSession();
        List<SessionResponse> responses;
        responses = sessions.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return responses;
    }
    @Override
    public List<SessionResponse> findSessionsOuvertes() throws BusinessResourceException {
        List<Session> sessions = dao.findSessionsOuvertes();
        List<SessionResponse> responses;
        responses = sessions.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public List<SessionResponse> findCandidaturesOuvertes() throws BusinessResourceException {
        List<Session> sessions = dao.findCandidaturesOuvertes();
        List<SessionResponse> responses;
        responses = sessions.stream()
                .map(mapper::toEntiteResponse)
                .collect(Collectors.toList());
        return responses;
    }
}
