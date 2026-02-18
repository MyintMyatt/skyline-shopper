package com.orion.auth_service.controller;

import com.orion.auth_service.common.utils.ResponseUtils;
import com.orion.auth_service.service.RoleAndPermissionService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base.path}/private/v1/permission")
@RequiredArgsConstructor
public class PermissionController {
    private final RoleAndPermissionService roleAndPermissionService;
    @PostMapping("/create")
    public ResponseEntity<?> createPermission(
            @NotBlank(message = "Permission name must not be null or empty!")
            @RequestParam(name = "name") String name,
            @NotBlank(message = "description name must not be null or empty!")
            @RequestParam(name = "desc") String desc
    ){
        return ResponseUtils.createSimpleResponse(roleAndPermissionService.createPermission(name, desc));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getPermissionByName(
            @NotBlank(message = "Permission name must not be null or empty!")
            @RequestParam(name = "name") String name
    ){
        return ResponseUtils.createSimpleResponse(roleAndPermissionService.getPermissionByName(name));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPermission(){
        return ResponseUtils.createSimpleResponse(roleAndPermissionService.getAllPermission());
    }
}
