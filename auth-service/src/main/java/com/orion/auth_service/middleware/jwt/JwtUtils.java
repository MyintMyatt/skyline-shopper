package com.orion.auth_service.middleware.jwt;

import com.orion.auth_service.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtils {

    @Value("${jwt.key}")
    private String JWT_KEY;

    @Value("${jwt.access-token.ttl}")
    private Long ACCESS_TOKEN_TTL;

    @Value("${jwt.refresh-token.ttl}")
    private Long REFRESH_TOKEN_TTL;

    private SecretKey getSignedKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_KEY));
    }

    // TTL is different depends on ACCESS TOKEN and REFRESH TOKEN
    public String generateToken(User user, final Long TTL) {
        Map<String,String> claims = new HashMap<>();
        claims.put("role", user.getRole().getName());
        return Jwts.builder()
                .subject(user.getEmail())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TTL))
                .signWith(getSignedKey())
                .compact();
    }

}
