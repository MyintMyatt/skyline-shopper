package com.orion.auth_service.controller;

import com.orion.auth_service.common.utils.ResponseUtils;
import com.orion.auth_service.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base.path}/private/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getUserByEmail(@NotBlank @RequestParam(name = "email") String email){
        return ResponseUtils.createSimpleResponse(userService.getUserByEmail(email));
    }
}
