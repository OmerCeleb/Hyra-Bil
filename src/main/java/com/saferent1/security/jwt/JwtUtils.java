package com.saferent1.security.jwt;

import com.saferent1.exception.message.ErrorMessage;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${saferent1.app.jwtSecret}")
    private String jwtSecret;
    @Value("${saferent1.app.jwtExpirationMs}")
    private Long jwtExpirationMs;


    // !!! generate JWT token
    public String generateJwtToken(UserDetails userDetails) {
        return Jwts.builder().
                setSubject(userDetails.getUsername()).
                setIssuedAt(new Date()).
                setExpiration(new Date(new Date().getTime() + jwtExpirationMs)).
                signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

    }

    //!!! Metod för att komma åt e-postinformation från JWT-token
    //varför jag vill nå eftersom e-post är unikt för användare.
    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).
                parseClaimsJws(token).
                getBody().
                getSubject();

        // En data läggs in som ett ämne, medan vi genererar e-postinformationen eftersom vi vill att den ska fungera med e-post.
        // Vi ställer in det här, vi fick e-postinformationen genom att säga getSubject() och lindade den upp och ner.

    }


    //!!! JWT validate== Jag fick jwt-token med begäran, så vi måste validera token.
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).
                    parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            logger.error(String.format(
                    ErrorMessage.JWTTOKEN_ERROR_MESSAGE, e.getMessage()));
        }
        return false;

    }

}
