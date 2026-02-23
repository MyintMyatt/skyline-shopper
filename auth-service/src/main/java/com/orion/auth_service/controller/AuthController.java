package com.orion.auth_service.controller;

import com.orion.auth_service.common.utils.ResponseUtils;
import com.orion.auth_service.dto.UserRequest;
import com.orion.auth_service.service.AuthService;
import com.orion.auth_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.base.path}/public/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Value("${jwt.access-token.ttl}")
    private Long ACCESS_TOKEN_TTL;
    @Value("${jwt.refresh-token.ttl}")
    private Long REFRESH_TOKEN_TTL;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest request) {
        return ResponseUtils.createSimpleResponse(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Email
            @NotBlank
            @RequestParam(name = "email") String email,

            @NotBlank
            @RequestParam(name = "pwd") String password,

            HttpServletRequest request
    ) {
        Map<String, String> map = authService.login(email, password, request);
        ResponseCookie accessTokenCookie = ResponseUtils.buildResponseCookieResponse("access-token", map.get("access-token"), "/", ACCESS_TOKEN_TTL);
        ResponseCookie refreshTokenCookie = ResponseUtils.buildResponseCookieResponse("refresh-token", map.get("refresh-token"), "/public/v1/auth/refresh", REFRESH_TOKEN_TTL);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(Map.of("message", "Login successful"));
    }

}
