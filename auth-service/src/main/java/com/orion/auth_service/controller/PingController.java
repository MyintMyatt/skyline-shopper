package com.orion.auth_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("${api.base.path}/v1/ping")
public class PingController {
    @GetMapping
    public Map<String,String> pingServer(){
        return Map.of("msg","server is running at 8080");
    }
}
