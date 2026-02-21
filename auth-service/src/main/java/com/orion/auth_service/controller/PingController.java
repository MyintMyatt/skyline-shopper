package com.orion.auth_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.base.path}/public/v1/ping")
public class PingController {


    @GetMapping
    public Map<String,String> pingServer(@RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent){
        Map<String,String > map = new HashMap<>();
        map.put("msg", "server is running at 8081");
        map.put("user-agent" , userAgent);
        return map;
    }

    public String getClientPublicIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // The first IP in the list is the original client's public IP
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr(); // Fallback if no proxy is used
    }

}
