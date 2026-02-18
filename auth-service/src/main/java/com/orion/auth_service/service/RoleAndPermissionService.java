package com.orion.auth_service.service;

import com.orion.auth_service.common.dto.ApiResponse;
import com.orion.auth_service.entity.Permission;
import com.orion.auth_service.entity.Role;
import com.orion.auth_service.repo.PermissionRepository;
import com.orion.auth_service.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleAndPermissionService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    // create role
    @Transactional
    public ApiResponse<Role> createRole(final String name, final String desc) {
        Role role = Role.builder()
                .name(name)
                .description(desc)
                .build();

        role = roleRepository.save(role);
        return ApiResponse.<Role>builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message("Role created successfully!")
                .content(role)
                .build();
    }

    // get role by name
    @Transactional(readOnly = true) // without this, will throw Large Objects(@Lob) may not be used in auto-commit mode
    public ApiResponse<Role> getRoleByName(final String name) {
        Role role = roleRepository.findByNameLike("%" + name + "%").orElseThrow(() -> new NoSuchElementException("role not found with name : " + name));
        return ApiResponse.<Role>builder()
                .code(HttpStatus.OK.value())
                .success(true)
                .message("role by your search keyword : " + name)
                .content(role)
                .build();
    }

    // get all role
    @Transactional(readOnly = true)
    public ApiResponse<Set<Role>> getAllRole() {
        Iterator<Role> roles = roleRepository.findAll().iterator();
        Set<Role> roleList = new HashSet<>();
        while (roles.hasNext()) {
            roleList.add(roles.next());
        }
        return ApiResponse.<Set<Role>>builder()
                .code(HttpStatus.OK.value())
                .success(true)
                .message("all role lists")
                .content(roleList)
                .build();
    }

    // create permission
    @Transactional
    public ApiResponse<Permission> createPermission(final String name, final String desc) {
        Permission permission = Permission.builder()
                .name(name)
                .description(desc)
                .build();

        permission = permissionRepository.save(permission);
        return ApiResponse.<Permission>builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message("Permission created successfully!")
                .content(permission)
                .build();
    }


    // get permission by name
    @Transactional(readOnly = true) // without this, will throw Large Objects(@Lob) may not be used in auto-commit mode
    public ApiResponse<Permission> getPermissionByName(final String name) {
        Permission permission = permissionRepository.findByNameLike("%" + name + "%").orElseThrow(() -> new NoSuchElementException("Permission not found with name : " + name));
        return ApiResponse.<Permission>builder()
                .code(HttpStatus.OK.value())
                .success(true)
                .message("permission by your search keyword : " + name)
                .content(permission)
                .build();
    }

    // get all permissions
    @Transactional(readOnly = true)
    public ApiResponse<Set<Permission>> getAllPermission() {
        Iterator<Permission> permissions = permissionRepository.findAll().iterator();
        Set<Permission> permissionList = new HashSet<>();
        while (permissions.hasNext()) {
            permissionList.add(permissions.next());
        }
        return ApiResponse.<Set<Permission>>builder()
                .code(HttpStatus.OK.value())
                .success(true)
                .message("all Permission lists")
                .content(permissionList)
                .build();
    }
}
