package com.orion.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank
    @Size(min = 3, message = "username must have 3 characters at least.")
    private String username;

    @NotBlank
    @Email(message = "wrong email format")
    private String email;

    @NotBlank
    @Size(min = 6, message = "password mush have 6 characters at least.")
    private String password;

    private Long roleId; //nullable coz if null, set user role as default

}
