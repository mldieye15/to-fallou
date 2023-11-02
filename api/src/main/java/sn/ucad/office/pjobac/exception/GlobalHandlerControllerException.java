package sn.ucad.office.pjobac.exception;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.RollbackException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice(basePackages = {"sn.ucad.officedubac.pjobac"})
public class GlobalHandlerControllerException extends ResponseEntityExceptionHandler {

    @InitBinder
    public void dataBinding(WebDataBinder binder) {
        //Vous pouvez intialiser n'importe quelle donnée ici

    }

    //
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BusinessResourceExceptionDTO> resourceNotFound(HttpServletRequest req, ResourceNotFoundException ex) {
        BusinessResourceExceptionDTO response = new BusinessResourceExceptionDTO();
        response.setErrorCode("NOT_FOUND");
        response.setErrorMessage(ex.getMessage());
        response.setRequestURL(req.getRequestURL().toString());
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<BusinessResourceExceptionDTO>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<BusinessResourceExceptionDTO> unauthorizedException(HttpServletRequest req, UnauthorizedException ex) {
        BusinessResourceExceptionDTO response = new BusinessResourceExceptionDTO();
        response.setErrorCode("UNAUTHORIZED");
        response.setErrorMessage(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setRequestURL(req.getRequestURL().toString());

        return new ResponseEntity<BusinessResourceExceptionDTO>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<BusinessResourceExceptionDTO> resourceAlreadyExists(HttpServletRequest req, ResourceAlreadyExists ex) {
        BusinessResourceExceptionDTO response = new BusinessResourceExceptionDTO();
        response.setErrorCode("CONFLICT");
        response.setErrorMessage(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setRequestURL(req.getRequestURL().toString());
        return new ResponseEntity<BusinessResourceExceptionDTO>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TransactionSystemException.class)
    protected ResponseEntity<List<String>> handleTransactionException(TransactionSystemException ex) throws Throwable {
        Throwable cause = ex.getCause();
        if (!(cause instanceof RollbackException)) throw cause;
        if (!(cause.getCause() instanceof ConstraintViolationException)) throw cause.getCause();
        ConstraintViolationException validationException = (ConstraintViolationException) cause.getCause();
        List<String> messages = validationException.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
    }

    @ModelAttribute
    public void globalAttributes(Model model) {
        model.addAttribute("technicalError", "Une erreur technique est survenue !");
    }

    @ExceptionHandler(BusinessResourceException.class)
    public ResponseEntity<BusinessResourceExceptionDTO> businessResourceError(HttpServletRequest req, BusinessResourceException ex) {
        BusinessResourceExceptionDTO businessResourceExceptionDTO = new BusinessResourceExceptionDTO();
        businessResourceExceptionDTO.setStatus(ex.getStatus());
        businessResourceExceptionDTO.setErrorCode(ex.getErrorCode());
        businessResourceExceptionDTO.setErrorMessage(ex.getMessage());
        businessResourceExceptionDTO.setRequestURL(req.getRequestURL().toString());
        return new ResponseEntity<BusinessResourceExceptionDTO>(businessResourceExceptionDTO, ex.getStatus());
    }

    @ExceptionHandler(JwtException.class)
     //@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
     public @ResponseBody
     ResponseEntity handleJwtException(HttpServletRequest req, JwtException ex) {
         BusinessResourceExceptionDTO response = new BusinessResourceExceptionDTO();
         response.setErrorCode("Token invalide ou expiré");
         response.setErrorMessage(ex.getLocalizedMessage());
         response.setRequestURL(req.getRequestURL().toString());
         response.setTimestamp(LocalDateTime.now());
         return new ResponseEntity<BusinessResourceExceptionDTO>(response, HttpStatus.UNPROCESSABLE_ENTITY);
     }
    @ExceptionHandler(Exception.class)//toutes les autres erreurs non gérées par le service sont interceptées ici
    public ResponseEntity<BusinessResourceExceptionDTO> unknowError(HttpServletRequest req, Exception ex) {
        BusinessResourceExceptionDTO response = new BusinessResourceExceptionDTO();
        response.setErrorCode("Technical Error");
        response.setErrorMessage(ex.getMessage());
        response.setRequestURL(req.getRequestURL().toString());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<BusinessResourceExceptionDTO>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
