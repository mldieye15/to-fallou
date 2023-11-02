package sn.ucad.office.pjobac.modules.security.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

@Service
@Slf4j
@RequiredArgsConstructor
public class InscriptionContentBuilder {

    private final TemplateEngine templateEngine;

    public String build(String message) {
        try{
            Context context = new Context();
            context.setVariable("message", message);
            return templateEngine.process("inscription", context);
        } catch(Exception ex){
            log.error("Build message: Une erreur inatteandue est rencontrée.");
            throw new BusinessResourceException("BuildMessError", "Erreur de construction du message.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

