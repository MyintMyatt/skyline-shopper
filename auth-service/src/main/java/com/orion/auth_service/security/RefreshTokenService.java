package com.orion.auth_service.security;

import com.orion.auth_service.common.constant.AppConstants;
import com.orion.auth_service.entity.Session;
import com.orion.auth_service.entity.User;
import com.orion.auth_service.repo.SessionRepository;
import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final SessionRepository sessionRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
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
        String rawToken = UUID.randomUUID().toString();
        Session session = Session.builder()
                .user(user)
                .refreshToken(hashToken(rawToken))
                .expiredAt(Instant.now().plusMillis(REFRESH_TOKEN_TTL))
                .userAgent(userAgent)
                .build();

        sessionRepository.save(session);
        return rawToken;
    }


    @Transactional(readOnly = true)
    public Map<String,String> verifyRefreshToken(HttpServletRequest request){
        String rawRefreshToken = jwtService.extractTokenFromCookie(request, AppConstants.TokenType.REFRESH);
        System.err.println(rawRefreshToken);
        String hashedResult = hashToken(rawRefreshToken);
//        return sessionRepository.findByRefreshToken(hashedResult)
//                .map(token -> token.getExpiredAt().isAfter(Instant.now()) && !token.getIsRevoked())
//                .orElse(false);
        Session session = sessionRepository.findByRefreshToken(hashedResult)
                .filter(token -> token.getExpiredAt().isAfter(Instant.now()) && !token.getIsRevoked()).orElseThrow(() -> new InvalidRequestStateException("invalid refresh token!"));
        if (session.getIsRevoked()){
            Map<String, String> map = new HashMap<>();
            String accessToken = jwtService.generateAccessToken(session.getUser());
            String refreshToken = refreshTokenService.generateRefreshToken(session.getUser(), request);
            map.put(AppConstants.TokenType.ACCESS.getValue(),accessToken);
            map.put(AppConstants.TokenType.REFRESH.getValue(), refreshToken);
            return map;
        }
        throw new RuntimeException("invalid refresh token");
    }

}
