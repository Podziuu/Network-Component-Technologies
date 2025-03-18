package pl.lodz.p.edu.rest.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.lodz.p.edu.rest.model.user.Role;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration;

    public String generateToken(String login, String id, Role role) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .subject(login)
                .claim("role", role.name())
                .claim("userId", id)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String getLogin(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getRole(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return claimsJws.getBody().get("role", String.class);
    }

    public boolean validateToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        try {
          Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
          return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
