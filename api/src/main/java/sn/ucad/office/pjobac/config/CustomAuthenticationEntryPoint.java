package sn.ucad.office.pjobac.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        PrintWriter writer = response.getWriter();
        String message = "Authentification échouée";

        if (authException.getMessage().contains("User is disabled")) {
            message = "Votre compte est désactivé. Veuillez contacter l'administrateur.";
        } else if (authException.getMessage().contains("Bad credentials")) {
            message = "Email ou mot de passe incorrect.";
        }

        writer.write("{\"error\": \"" + message + "\"}");
        writer.flush();
    }
}
