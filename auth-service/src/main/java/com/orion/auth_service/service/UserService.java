package com.orion.auth_service.service;

import com.orion.auth_service.common.dto.ApiResponse;
import com.orion.auth_service.dto.UserRequest;
import com.orion.auth_service.dto.UserResponse;
import com.orion.auth_service.entity.Permission;
import com.orion.auth_service.entity.Role;
import com.orion.auth_service.entity.User;
import com.orion.auth_service.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleAndPermissionService roleAndPermissionService;

    @Transactional
    public ApiResponse<UserResponse> registerUser(UserRequest userReq) {
        // check email is existed or not
        if (userRepository.findByEmail(userReq.getEmail()).isPresent()){
            throw new RuntimeException("User already existed with this email : " + userReq.getEmail());
        }
        Role role = roleAndPermissionService.getRoleById(userReq.getRoleId());
        User user = transformUserReqToUser(userReq);
        user.setRole(role);
        user = userRepository.save(user);
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.CREATED.value())
                .success(true)
                .message("User created successfully!")
                .content(transformUserEntityToResponse(user))
                .build();
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<UserResponse>> getAllUser(){
        return null;
    }

    @Transactional(readOnly = true)
    public ApiResponse<UserResponse> getUserByEmail(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("user not found!"));
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .success(true)
                .message("successfully retrieved")
                .content(transformUserEntityToResponse(user))
                .build();
    }



    private User transformUserReqToUser(UserRequest req) {
        return User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .build();
    }

    public UserResponse transformUserEntityToResponse(User user) {
        return new UserResponse(
                user.getUsername(),
                user.getEmail(),
                user.getIsVerified(),
                user.getIsActive(),
                user.getRole().getName(),
                user.getRole().getPermissions().stream().map(Permission::getName).collect(Collectors.toSet()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }


}
