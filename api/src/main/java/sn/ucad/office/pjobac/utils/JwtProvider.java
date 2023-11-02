package sn.ucad.office.pjobac.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

import static java.util.Date.from;

@Service
@Slf4j
public class JwtProvider {

    private KeyStore keyStore;
    @Value("${jwtexpirationtime}")
    private Long jwtexpirationtime;

    @Value("${jksfilepassword}")
    private String jksfilepassword;

    @PostConstruct
    public void init(){
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/startapi.jks");
            keyStore.load(resourceAsStream, this.getJksfilepassword().toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            log.warn("Certificat | Algorithme | .jks invalide.");
            throw new BusinessResourceException("NotValidCertifey","Erreur sur le certificat.");
        }
        //this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(from(Instant.now()))
                .claim("roles", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .signWith(this.getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtexpirationtime)))
                .compact()
                ;
    }

    public String generateTokenWithUserName(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(from(Instant.now()))
                //.claim("roles", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtexpirationtime)))
                .compact();
    }

    public boolean validateToken(String jwt) throws ExpiredJwtException, BusinessResourceException, IOException {
        try {
            //Jwts.parser().setSigningKey(this.getPublickey()).parseClaimsJws(jwt);
            Jwts.parserBuilder().setSigningKey(this.getPublickey()).build().parseClaimsJws(jwt);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            log.warn("Token expire.");
            return false;
            //throw new BusinessResourceException("expired-token", "Token expiré ou invalide.", HttpStatus.GATEWAY_TIMEOUT);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.warn("Token invalide ou expiré.");
            return false;
        }

    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.getPublickey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                ;

        return claims.getSubject();
    }
    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("startapi", this.getJksfilepassword().toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            log.warn("Erreur sur le cle prive.");
            throw new BusinessResourceException("NotValidKey","Erreur sur le certificat.");
        }
    }

    private PublicKey getPublickey() {
        try {
            return keyStore.getCertificate("startapi").getPublicKey();
        } catch (KeyStoreException e) {
            log.warn("Erreur sur le cle public.");
            throw new BusinessResourceException("NotValidKey","Erreur sur le certificat.");
        }
    }

    public Long getJwtexpirationtime() {
        return jwtexpirationtime;
    }
    public String getJksfilepassword(){
        return jksfilepassword;
    }
}