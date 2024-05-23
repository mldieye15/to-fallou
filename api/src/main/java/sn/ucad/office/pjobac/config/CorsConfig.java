package sn.ucad.office.pjobac.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

//    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*")); // Autoriser toutes les origines (domaines)
        config.addAllowedMethod("*"); // Autoriser toutes les méthodes (GET, POST, PUT, DELETE, etc.)
        config.addAllowedHeader("*"); // Autoriser tous les en-têtes
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

