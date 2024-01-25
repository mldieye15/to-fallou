package sn.ucad.office.pjobac.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sn.ucad.office.pjobac.exception.BusinessResourceException;
import sn.ucad.office.pjobac.utils.AuthEntryPointJwt;
import sn.ucad.office.pjobac.utils.JwtAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
public class  SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthEntryPointJwt unauthorizedHandler;

    /*@Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }*/
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http) throws Exception {
        // pas besoin de protection csrf
        //http.csrf().disable();
        http.cors().configurationSource(request-> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("*"));
            configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
            configuration.setAllowedHeaders(List.of("*"));
            return configuration;
        }).and().csrf().disable();
        http.headers().frameOptions().disable();
        http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .requestMatchers("/web/**",
                        "/pjobac/api/v1/etablissements/all",
                        "/pjobac/api/v1/fonctions/all",
                        "/pjobac/api/v1/users/**",
                        "/pjobac/api/v1/codifications/get-code",
                        "/pjobac/api/v1/codifications/send-code",
                        "/pjobac/api/v1/codifications/verify-code",
                        "/pjobac/api/v1/users/matricule-availability",
                        "/pjobac/api/v1/users/username-availability",
                        "/pjobac/api/v1/users/email-availability",
                        "/pjobac/api/v1/typeCentres/libelle-availability",
                        "/pjobac/api/v1/typeEtablissements/libelle-availability",
                        "/pjobac/api/v1/annees/libelle-availability",
                        "/pjobac/api/v1/codifications/code-availability",
                        "/pjobac/api/v1/codifications/email-availability",
                        "/pjobac/api/v1/typeSessions/libelle-availability",
                        "/pjobac/api/v1/fonctions/libelle-availability",
                        "/pjobac/api/v1/academies/libelle-availability",
                        "/pjobac/api/v1/centres/libelle-availability",
                        "/pjobac/api/auth/v*/inscription",
                        "/pjobac/api/auth/v1/reset-password",
                        "/pjobac/api/auth/v1/new-password",
                        "/pjobac/api/auth/v*/verif-token/**",
                        "/pjobac/api/auth/v1/connexion",
                        "/pjobac/api/auth/v*/refresh-token",
                        "/pjobac/api/auth/v*/deconnexion",
                        "/v*/api-docs",
                        "/v3/api-docs/**",
                        "/v*/api-docs/**",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-ui**"
                )
                .permitAll()
                .anyRequest()
                .authenticated()
        ;
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws BusinessResourceException, Exception {
        authBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
        ;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

        /*
        * HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods",
                "DELETE, GET, OPTIONS, POST, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization");
        * */
    }
}
