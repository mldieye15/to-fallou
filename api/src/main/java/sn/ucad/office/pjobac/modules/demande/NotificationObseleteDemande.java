package sn.ucad.office.pjobac.modules.demande;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sn.ucad.office.pjobac.modules.security.mail.MailService;
import sn.ucad.office.pjobac.modules.security.mail.NotificationEmailHtml;
import sn.ucad.office.pjobac.modules.ville.Ville;
import sn.ucad.office.pjobac.modules.ville.VilleDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationObseleteDemande {
    private final DemandeDao dao;
    private final MailService mailService;
    private final VilleDao villeDao;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notification(String villeId) throws InterruptedException {
        Long myId = Long.valueOf(villeId.trim());
        Ville ville;
        ville = villeDao.findById(myId)
                .orElseThrow(() -> new RuntimeException("Ville non trouvée pour l'ID : " + villeId));
        List<Demande> demandes = dao.demandeObseleteByVille(ville);
        for (Demande demande : demandes) {
            NotificationEmailHtml notificationEmail = new NotificationEmailHtml();
            notificationEmail.setSubject("Demande obsolète");
            notificationEmail.setRecipient(demande.getUser().getEmail());
            notificationEmail.setTemplateName("notificationObsolet.html"); // Ajoutez le nom du modèle Thymeleaf
            Map<String, Object> emailVariables = new HashMap<>();
            emailVariables.put("prenoms", demande.getUser().getPrenoms());
            emailVariables.put("nom", demande.getUser().getNom());
            emailVariables.put("academie", demande.getVille().getAcademie().getLibelleLong());
            emailVariables.put("ville", demande.getVille().getLibelleLong());
            notificationEmail.setEmailVariables(emailVariables);
            mailService.sendHtmlEmail(notificationEmail);
        }
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notificationUpdate() throws InterruptedException{
        List<Demande> demandes = dao.allDemandeObselete();
        for (Demande demande : demandes) {
            NotificationEmailHtml notificationEmail = new NotificationEmailHtml();
            notificationEmail.setSubject("Demande à modifier");
            notificationEmail.setRecipient(demande.getUser().getEmail());
            notificationEmail.setTemplateName("notificationUpdateObsolete.html"); // Ajoutez le nom du modèle Thymeleaf
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
