package com.orion.auth_service.security;

import com.orion.auth_service.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
    public String generateAccessToken(User user) {
        Map<String,String> claims = new HashMap<>();
        claims.put("role", user.getRole().getName());
        return Jwts.builder()
                .subject(user.getEmail())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TTL))
                .signWith(getSignedKey())
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String userEmail = extractUserEmail(token);
        return (userEmail.equals(userDetails.getUsername())  && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(final String token){
        return Jwts.parser()
                .verifyWith(getSignedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(final String token, Function<Claims, T> claimsResolver){
        return claimsResolver.apply(extractAllClaims(token));
    }
    public String extractUserEmail(final String token){
        return extractClaim(token,Claims::getSubject);
    }

    public String extractTokenFromCookie(HttpServletRequest request){
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()){
            if ("access.token".equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }

}
