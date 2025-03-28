package sn.ucad.office.pjobac.modules.security.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final InscriptionContentBuilder builderService;
    private final SpringTemplateEngine templateEngine;


    @Async//("threadPoolTaskExecutor")
    public void sendMail(NotificationEmail notificationEmail) throws MailException, InterruptedException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("pjbac@ucad.edu.sn");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());
        };

        try{
            mailSender.send(messagePreparator);
            log.info("Message d'activation de compte envoye avec succes.");
        } catch(MailException ex){
            log.error("Sending message: message non envoyé pour une erreur."+ex);
            throw new BusinessResourceException("SendMessError", "Erreur d'envoi du message.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(Exception ex){
            log.error("Sending message: Une erreur inatteandue est rencontrée.");
            throw new BusinessResourceException("SendMessError", "Erreur d'envoi du message.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @Async
    public void sendHtmlEmail(NotificationEmailHtml notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("pjbac@ucad.edu.sn");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());

            Context context = new Context();
            context.setVariables(notificationEmail.getEmailVariables());
            String htmlContent = templateEngine.process(notificationEmail.getTemplateName(), context);
            messageHelper.setText(htmlContent, true);
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Email de notification envoyé avec succès.");
        } catch (MailException ex) {
            // Log the error without throwing an exception to prevent blocking the main process
            log.error("Erreur lors de l'envoi de l'email de notification : " + ex.getMessage());
        }
    }
//    @Async
//    public void sendHtmlEmail(NotificationEmailHtml notificationEmail) throws MailException, InterruptedException {
//        MimeMessagePreparator messagePreparator = mimeMessage -> {
//            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
//            messageHelper.setFrom("pjbac@ucad.edu.sn");
//            messageHelper.setTo(notificationEmail.getRecipient());
//            messageHelper.setSubject(notificationEmail.getSubject());
//
//            Context context = new Context();
//            context.setVariables(notificationEmail.getEmailVariables());
//            String htmlContent = templateEngine.process(notificationEmail.getTemplateName(), context);
//            messageHelper.setText(htmlContent, true);
//        };
//        try {
//            mailSender.send(messagePreparator);
//            log.info("Email de notification envoyé avec succès.");
//        } catch (MailException ex) {
//            log.error("Erreur lors de l'envoi de l'email de notification : " + ex.getMessage());
//            throw new BusinessResourceException("SendMessError", "Erreur d'envoi du message.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}