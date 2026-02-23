package com.orion.auth_service.common.utils;

import com.orion.auth_service.common.dto.ApiResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {
    public static ResponseEntity<?> createSimpleResponse(ApiResponse<?> apiResponse) {
        return ResponseEntity.status(apiResponse.getCode())
                .body(apiResponse);
    }

    public static ResponseCookie buildResponseCookieResponse(String name, String token,String path, Long TTL){
        return ResponseCookie.from(name, token)
                .httpOnly(false)
                .secure(false)
                .path(path)
                .sameSite("Strict")
                .maxAge(TTL / 1000) // divide by 1000 because TTL is millisecond
                .build();

    }
}
