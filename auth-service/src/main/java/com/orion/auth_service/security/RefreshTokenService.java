package com.orion.auth_service.security;

import com.orion.auth_service.entity.Session;
import com.orion.auth_service.entity.User;
import com.orion.auth_service.repo.SessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final SessionRepository sessionRepository;
    @Value("${jwt.refresh-token.ttl}")
    private Long REFRESH_TOKEN_TTL;

    private String hashToken(String token){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hashToken = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashToken);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("error hashing token : " + e);
        }
    }

    public String generateRefreshToken(User user, HttpServletRequest request){

        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
//        User user = jwtUtils.extractTokenFromCookie(request);
        String rawToken = UUID.randomUUID().toString();

        Session session = Session.builder()
                .user(user)
                .refreshToken(hashToken(rawToken))
                .expiredAt(Instant.now().plusMillis(REFRESH_TOKEN_TTL))
//                .userAgent(userAgent)
                .build();

        sessionRepository.save(session);
        return rawToken;
    }


    public boolean verifyRefreshToken(String rawToken){
        String hashedResult = hashToken(rawToken);

        return sessionRepository.findByRefreshToken(hashedResult)
                .map(token -> token.getExpiredAt().isAfter(Instant.now()))
                .orElse(false);
    }

}
