package openminder.emeal.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import openminder.emeal.domain.account.AccountPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.Date;

@Component
public class JwtTokenUtil implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    String secret = Encoders.BASE64.encode(key.getEncoded());

    public String generateToken(Authentication authentication) {
        AccountPrincipal accountPrincipal = (AccountPrincipal) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(Long.toString(accountPrincipal.getAccountId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(key).compact();
    }

    public Long getUserIdFromJWT(String token) {
//        Claims claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            System.out.println("validateToken: " + authToken);
//            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(authToken);
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException exception) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException exception) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException exception) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException exception) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException exception) {
            logger.error("JWT claims string is empty");
        }
        return false;
    }
}
