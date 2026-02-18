package com.orion.auth_service.controller;

import com.orion.auth_service.common.utils.ResponseUtils;
import com.orion.auth_service.service.RoleAndPermissionService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base.path}/private/v1/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleAndPermissionService roleAndPermissionService;
    @PostMapping("/create")
    public ResponseEntity<?> createRole(
            @NotBlank(message = "role name must not be null or empty!")
            @RequestParam(name = "name") String name,
            @NotBlank(message = "description name must not be null or empty!")
            @RequestParam(name = "desc") String desc
    ){
        return ResponseUtils.createSimpleResponse(roleAndPermissionService.createRole(name, desc));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getRoleByName(
            @NotBlank(message = "role name must not be null or empty!")
            @RequestParam(name = "name") String name
    ){
        return ResponseUtils.createSimpleResponse(roleAndPermissionService.getRoleByName(name));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRole(){
        return ResponseUtils.createSimpleResponse(roleAndPermissionService.getAllRole());
    }
}
