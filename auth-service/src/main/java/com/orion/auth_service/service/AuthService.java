package com.orion.auth_service.service;

import com.orion.auth_service.common.constant.AppConstants;
import com.orion.auth_service.common.dto.ApiResponse;
import com.orion.auth_service.entity.User;
import com.orion.auth_service.repo.UserRepository;
import com.orion.auth_service.security.JwtService;
import com.orion.auth_service.security.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public Map<String,String> login(final String email, final String password, HttpServletRequest request){
        Map<String, String> map = new HashMap<>();
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        if (auth.isAuthenticated()){
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("user not found!"));
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = refreshTokenService.generateRefreshToken(user, request);
            map.put(AppConstants.TokenType.ACCESS.getValue(),accessToken);
            map.put(AppConstants.TokenType.REFRESH.getValue(), refreshToken);
            return map;
        }
        throw new RuntimeException("email and password is incorrect.");
    }

//    @Transactional
//    public void refresh(HttpServletRequest request){
//        String rawRefreshToken = jwtService.extractTokenFromCookie(request, AppConstants.TokenType.REFRESH);
//        System.err.println(rawRefreshToken);
//        boolean isCorrect = refreshTokenService.verifyRefreshToken(rawRefreshToken);
//        if (isCorrect){
//            Map<String, String> map = new HashMap<>();
//            String accessToken = jwtService.generateAccessToken(user);
//            String refreshToken = refreshTokenService.generateRefreshToken(user, request);
//            map.put(AppConstants.TokenType.ACCESS.getValue(),accessToken);
//            map.put(AppConstants.TokenType.REFRESH.getValue(), refreshToken);
//            return map;
//        }
//    }

}
