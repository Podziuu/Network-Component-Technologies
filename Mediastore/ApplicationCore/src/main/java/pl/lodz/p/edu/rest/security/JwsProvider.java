package pl.lodz.p.edu.rest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;

@Service
public class JwsProvider {

    @Value("${app.jws.secret}")
    private String secret;

    public String generateJws(String userId) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }

    public boolean validateJws(String jws, String expectedUserId) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jws)
                    .getPayload();
            String userId = claims.get("userId", String.class);
            return expectedUserId.equals(userId);
        } catch (Exception e) {
            return false;
        }
    }
}
