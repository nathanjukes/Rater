package Rater.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil implements Serializable {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private final int EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 Hours in MS
    private static final long serialVersionUID = 8742614023571274712L;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
       // claims.put("orgId", orgName);

        return Jwts
                .builder()
                .setSubject(userPrincipal.getUsername())
                //.setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractOrgName(String token) {
        // You can add a custom claim for organization ID when creating the token.
        // For example, if you add it as "orgId" in the token claims.
        return Long.parseLong((String) extractClaim(token, claims -> claims.get("orgName")));
    }

    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return getParser().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    public String getUsernameFromToken(String token) {
        return getParser().parseClaimsJws(token).getBody().getSubject();
    }

    public String parseJwt(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(7);
        }
        return null;
    }

    public boolean validateJwtToken(String token) {
        try {
            parseTokenClaims(token);
            return true;
        } catch (MalformedJwtException e) {
           // logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
           // logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
           // logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
           // logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    private void parseTokenClaims(String token) {
        getParser().parseClaimsJws(token);
    }

    private JwtParser getParser() {
        return Jwts.parserBuilder().setSigningKey(key()).build();
    }
}
