package com.orion.auth_service.common.utils;

import com.orion.auth_service.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {
    public static ResponseEntity<?> createSimpleResponse(ApiResponse<?> apiResponse) {
        return ResponseEntity.status(apiResponse.getCode())
                .body(apiResponse);
    }
}
