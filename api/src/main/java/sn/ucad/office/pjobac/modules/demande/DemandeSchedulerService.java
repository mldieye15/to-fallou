package sn.ucad.office.pjobac.modules.demande;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemande;
import sn.ucad.office.pjobac.modules.etatDemande.EtatDemandeService;
import sn.ucad.office.pjobac.modules.security.mail.MailService;
import sn.ucad.office.pjobac.modules.security.mail.NotificationEmailHtml;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DemandeSchedulerService {
    private  final  DemandeDao dao;
    private final EtatDemandeService service;
    private final MailService mailService;

    @Scheduled(fixedRate = 600000) // exécute toutes les 10 minutes (en millisecondes)
    public void verifierEtMettreAJourDemandes() throws InterruptedException {
        List<Demande> demandes = dao.demandeAccepter();
        for (Demande demande : demandes) {
            LocalDateTime dateRejet = demande.getDateRejetDemande();
            LocalDateTime dateVerification = LocalDateTime.now();
            if (dateRejet != null && !dateRejet.isBefore(dateVerification)) {
                // logique de traitement  verifie si la date de rejet est égale ou postérieure à la date de vérification
                Optional<EtatDemande> optionalEtat = service.findByLibelleLong("EN ATTENTE");
                EtatDemande etatParDefaut = optionalEtat.
                        orElseThrow(() -> new BusinessResourceException("not-found", "Aucune etat avec le libellé EN ATTENTE trouvée.", HttpStatus.NOT_FOUND));
                demande.setEtatDemande(etatParDefaut);
                demande.setCentre(null);
                dao.save(demande);
                NotificationEmailHtml notificationEmail = new NotificationEmailHtml();
                notificationEmail.setSubject("Mail d'Obsoléte");
                notificationEmail.setRecipient(demande.getUser().getEmail());
                notificationEmail.setTemplateName("notificationDecliner.html"); // Ajoutez le nom du modèle Thymeleaf
                Map<String, Object> emailVariables = new HashMap<>();
                emailVariables.put("prenoms", demande.getUser().getPrenoms());
                emailVariables.put("nom", demande.getUser().getNom());
                emailVariables.put("academie", demande.getVille().getAcademie().getLibelleLong());
                emailVariables.put("ville", demande.getVille().getLibelleLong());
                notificationEmail.setEmailVariables(emailVariables);
                mailService.sendHtmlEmail(notificationEmail);
            }
        }
    }
}
