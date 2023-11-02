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
import sn.ucad.office.pjobac.exception.BusinessResourceException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final InscriptionContentBuilder builderService;


    @Async//("threadPoolTaskExecutor")
    public void sendMail(NotificationEmail notificationEmail) throws MailException, InterruptedException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("cheikhtidianethioune98@gmail.com");
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
}